package com.mytradein.controller;

import com.mytradein.dto.OfferDTO;
import com.mytradein.model.*;
import com.mytradein.security.AuthenticationFacade;
import com.mytradein.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/offer")
public class OfferController {
    private final OfferService offerService;

    private final CustomerService customerService;

    private final AuthUserService authUserService;

    private final BrandModelService brandModelService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    private Seller seller;


    public OfferController(OfferService offerService,
                           CustomerService customerService,
                           AuthUserService authUserService,
                           BrandModelService brandModelService,
                           AuthenticationFacade authenticationFacade) {
        this.offerService = offerService;
        this.customerService = customerService;
        this.authUserService = authUserService;
        this.brandModelService = brandModelService;
        this.authenticationFacade = authenticationFacade;
    }

    @RequestMapping(method = RequestMethod.GET, value="/list")
    public String offerList(@RequestParam Map<String, String> parameters,
                            @RequestParam(required = false, defaultValue = "0") Integer page,
                            Model model) {
        if (page < 0) page = 0;
        else if (page > 0) page--;
        initSeller();
        model.addAttribute("isAdmin", seller.isSupervisor());

        Map<String, Object> searchParameters = convertSearchParameters(parameters);
        Page offersPage = offerService.searchOffers(
                searchParameters,
                PageRequest.of(page, Constants.ITEMS_PER_PAGE, Sort.Direction.DESC, "id")
        );

        model.addAttribute("offersPage", offersPage);

        List<Integer> pageNumbers = Utilities.getPageNumbers(offersPage);
        model.addAttribute("pageNumbers", pageNumbers);

        Map<Long, String> customerMap = new HashMap<>();
        if (searchParameters.containsKey("customerid")) {
            Customer customer = (Customer)searchParameters.get("customerid");
            customerMap.put(customer.getId(), customer.getFirstname() + " " + customer.getLastname());
        }
        model.addAttribute("customerMap", customerMap);
        model.addAttribute("customerid", parameters.getOrDefault("customerid", null));

        model.addAttribute("sellerMap", getSellersMap());
        model.addAttribute("sellerid", parameters.getOrDefault("sellerid", null));

        model.addAttribute("searchStatus", parameters.getOrDefault("status", null));
        model.addAttribute("offers", offersPage.getContent());
        model.addAttribute("username", seller.getAuthUser().getFullName());

        return "offers";
    }

    @GetMapping("")
    public String newOffer(Model model,
                           @RequestParam(required = false, defaultValue = "0") Long customerid) {
        initSeller();
        if (customerid < 0) customerid = 0L;
        Customer customer = customerService.findCustomerById(customerid);
        if (Objects.isNull(customer)) {
            return "redirect:/error404/customer";
        }
        model.addAttribute("customer", customer);
        model.addAttribute("offerDTO", new OfferDTO());
        model.addAttribute("modelList", getSortedUiBrandModels());
        model.addAttribute("username", seller.getAuthUser().getFullName());
        model.addAttribute("sellerid", seller.getSellerId());

        return "offer_new";
    }

    @PostMapping("")
    public String saveNewOffer(@Valid @ModelAttribute("offerDTO") OfferDTO offerDTO,
                               BindingResult bindingResult,
                               Model model) {
        initSeller();
        Brandmodel brandmodel = null;
        Customer customer = customerService.findCustomerById(Long.parseLong(offerDTO.getCustomerid()));
        if (Objects.isNull(customer)) {
            return "redirect:/error404/customer";
        }
        model.addAttribute("customer", customer);

        if (!bindingResult.hasErrors()) {
            Offer foundOffer = offerService.findByImei(offerDTO.getImei());
            if (!Objects.isNull(foundOffer))
                bindingResult.rejectValue("imei", "error.imei", "Device with emei already exists");

            brandmodel = brandModelService.getModel(Long.parseLong(offerDTO.getBrandmodelid()));
            if (Objects.isNull(brandmodel))
                bindingResult.rejectValue("brandmodelid", "error.brandmodelid", "Model not found");
        }
        model.addAttribute("username", seller.getAuthUser().getFullName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("modelList", getSortedUiBrandModels());

            return "offer_new";
        }
        Offer offer = new Offer(
                brandmodel, seller.getAuthUser(), customer, offerDTO.getImei(), Integer.parseInt(offerDTO.getConstractionyear()),
                offerDTO.getCondition(), offerDTO.getColor(), Integer.parseInt(offerDTO.getCapacity()), offerDTO.getDesireddevice()
        );
        offer.setCustomerPrice(BigDecimal.valueOf(Double.parseDouble(offerDTO.getCustomerprice())));
        offer.setPurchasePrice(BigDecimal.valueOf(0));
        offerService.save(offer);

        return "redirect:/offer/list?customerid=" + customer.getId() + "&status=" + OfferStatuses.NEW.name();
    }

    @GetMapping("/{id}")
    public String editOffer(@PathVariable(value = "id") Long offerId,
                            @RequestParam(required = false, defaultValue = "") String msg,
                            Model model) {
        initSeller();
        if (offerId < 0) offerId = 0L;
        Offer offer = offerService.findById(offerId);
        if (Objects.isNull(offer)) {
            return "redirect:/error404/offer";
        }
        if (seller.isManager() && !Objects.equals(offer.getAuthuser().getId(), seller.getAuthUser().getId())) {
            return "redirect:/error403/offer";
        }
        model.addAttribute("error", getErrorMessage(msg));
        model.addAttribute("customer", offer.getCustomer());
        model.addAttribute("offerDTO", new OfferDTO(offer));
        model.addAttribute("modelList", getSortedUiBrandModels());
        model.addAttribute("isAdmin", seller.isSupervisor());
        model.addAttribute("username", seller.getAuthUser().getFullName());
        Map<String, String> offerMap = new HashMap<>();
        offerMap.put(offer.getOfferStatus(), OfferStatuses.valueOf(offer.getOfferStatus()).getLabel());
        model.addAttribute("offerMap", offerMap);
        Map nextStatuses = offerService.getNextOfferStatuses(offer.getOfferStatus());
        model.addAttribute(
                "allowProcess",
                !offer.getOfferStatus().equals(OfferStatuses.PROCESSING.name())
                        && nextStatuses.containsKey(OfferStatuses.PROCESSING.name())
        );
        model.addAttribute(
            "allowApprove",
            seller.isSupervisor()
                &&!offer.getOfferStatus().equals(OfferStatuses.APPROVED.name())
                && nextStatuses.containsKey(OfferStatuses.APPROVED.name())
        );
        model.addAttribute(
            seller.isSupervisor()
                && !offer.getOfferStatus().equals(OfferStatuses.REJECTED.name())
                && nextStatuses.containsKey(OfferStatuses.REJECTED.name())
        );
        model.addAttribute(
            "allowPurchase",
                seller.isSupervisor()
                && !offer.getOfferStatus().equals(OfferStatuses.PURCHASED.name())
                && nextStatuses.containsKey(OfferStatuses.PURCHASED.name())
        );

        return "offer";
    }

    @PostMapping("/save")
    public String saveOffer(@Valid @ModelAttribute("offerDTO") OfferDTO offerDTO,
                            BindingResult bindingResult,
                            Model model) {
        initSeller();
        Brandmodel brandmodel = null;
        Offer offer = offerService.findById(Long.parseLong(offerDTO.getId()));
        if (Objects.isNull(offer)) {
            return "redirect:/error404/offer";
        }
        if (seller.isManager() && !Objects.equals(offer.getAuthuser().getId(), seller.getAuthUser().getId())) {
            return "redirect:/error403/offer";
        }
        model.addAttribute("customer", offer.getCustomer());

        if (!bindingResult.hasErrors()) {
            if (!offer.getImei().equals(offerDTO.getImei())) {
                Offer foundOffer = offerService.findByImei(offerDTO.getImei());
                if (!Objects.isNull(foundOffer) && offer.getId() != foundOffer.getId())
                    bindingResult.rejectValue("imei", "error.imei", "Device with emei already exists");
                else
                    offer.setImei(offerDTO.getImei());
            }
            if (!offer.getBrandmodel().getId().toString().equals(offerDTO.getBrandmodelid())) {
                brandmodel = brandModelService.getModel(Long.parseLong(offerDTO.getBrandmodelid()));
                if (Objects.isNull(brandmodel))
                    bindingResult.rejectValue("brandmodelid", "error.brandmodelid", "Model not found");
                else
                    offer.setBrandmodel(brandmodel);
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("customer", offer.getCustomer());
            model.addAttribute("modelList", getSortedUiBrandModels());
            model.addAttribute("isAdmin", seller.isSupervisor());

            return "offer";
        }

        offer.setConstructionYear(Integer.parseInt(offerDTO.getConstractionyear()));
        offer.setCondition(offerDTO.getCondition());
        offer.setColor(offerDTO.getColor());
        offer.setCapacity(Integer.parseInt(offerDTO.getCapacity()));
        offer.setDesiredDevice(offerDTO.getDesireddevice());
        offer.setOfferStatus(offerDTO.getOfferstatus());
        offer.setCustomerPrice(BigDecimal.valueOf(Double.parseDouble(offerDTO.getCustomerprice())));
        offer.setPurchasePrice(BigDecimal.valueOf(Double.parseDouble(offerDTO.getPurchaseprice())));
        offerService.save(offer);

        return "redirect:/offer/list?customerid=" + offer.getCustomer().getId();
    }

    @PostMapping("/status")
    public String statusOffer(@RequestParam Long id,
                              @RequestParam String newStatus,
                              Model model) {
        initSeller();
        Offer offer = offerService.findById(id);
        if (Objects.isNull(offer)) {
            return "redirect:/error404/offer";
        }
        if (seller.isManager()) {
            return "redirect:/error403/offer";
        }
        Map nextStatuses = offerService.getNextOfferStatuses(offer.getOfferStatus());
        if (!nextStatuses.containsKey(newStatus)) {
            return "redirect:/offer/" + offer.getId() + "?msg=o1";
        }
        if ((newStatus.equalsIgnoreCase(OfferStatuses.APPROVED.name())
                || newStatus.equalsIgnoreCase(OfferStatuses.PURCHASED.name()))
                    && offer.getPurchasePrice().compareTo(BigDecimal.ZERO) == 0
        ) {
            return "redirect:/offer/" + offer.getId() + "?msg=o2";
        }
        if (seller.isManager() && !newStatus.equalsIgnoreCase(OfferStatuses.PROCESSING.name()))
            return "redirect:/error403/offer";

        offer.setOfferStatus(newStatus);
        offerService.save(offer);

        return "redirect:/offer/" + offer.getId();
    }

    @PostMapping("/{id}/delete")
    public String deleteOffer(@PathVariable @Pattern(regexp="[1-9][0-9]+") Long id,
                              Model model) {
        initSeller();
        Offer offer = offerService.findById(id);
        if (Objects.isNull(offer)) {
            return "redirect:/error404/offer";
        }
        if (seller.isManager() && !Objects.equals(offer.getAuthuser().getId(), seller.getAuthUser().getId())) {
            return "redirect:/error403/offer";
        }
        if (!offer.getOfferStatus().equalsIgnoreCase(OfferStatuses.NEW.name())) {
            return "redirect:/offer/" + offer.getId() + "?msg=o3";
        }

        offerService.delete(offer);

        return "redirect:/offer/list?sellerid=" + seller.getAuthUser().getId();
    }

    private void initSeller() {
        User currentUser = (User)authenticationFacade.getAuthentication().getPrincipal();
        this.seller = new Seller(currentUser, authUserService.findByLogin(currentUser.getUsername()));
    }

    private Map getSortedUiBrandModels() {
        List<Brandmodel> modelList = brandModelService.findAllBrandmodels();

        Collections.sort(modelList, new Comparator<>() {
            @Override
            public int compare(Brandmodel s1, Brandmodel s2) {
                if (!s1.getBrand().getBrandname().equalsIgnoreCase(s2.getBrand().getBrandname()))
                    return s1.getBrand().getBrandname().compareToIgnoreCase(s2.getBrand().getBrandname());
                else
                    return s1.getModelname().compareToIgnoreCase(s2.getModelname());
            }
        });
        Map uiModelList = new HashMap<Long, String>();
        for (Brandmodel brandmodel : modelList) {
            uiModelList.put(brandmodel.getId(), brandmodel.getBrand().getBrandname() + " : " +  brandmodel.getModelname());
        }

        return uiModelList;
    }


    private Map<Long, String> getSellersMap() {
        Map<Long, String> sellerMap = new HashMap<>();
        if (seller.isManager()) {
            sellerMap.put(seller.getAuthUser().getId(), seller.getAuthUser().getFullName());

       } else {
            List<AuthUser> sellers = authUserService.getAllUsers();
            for (AuthUser sellerItem : sellers) {
                sellerMap.put(sellerItem.getId(), sellerItem.getFullName());
            }
        }

        return sellerMap;
    }


    private Map<String, Object> convertSearchParameters(Map<String, String> parameters) {
        Map<String, Object> sqlParameters = new HashMap<>();
        if (parameters.containsKey("status") && !parameters.get("status").isEmpty())
            sqlParameters.put("status", parameters.get("status"));

        if (parameters.containsKey("customerid") && !parameters.get("customerid").isEmpty()) {
            Long customerId = Long.parseLong(parameters.get("customerid"));
            Customer customer = customerService.findCustomerById(customerId);
            if (!Objects.isNull(customer))
                sqlParameters.put("customerid", customer);
        }

        if (seller.isManager()) {
            sqlParameters.put("sellerid", seller.getAuthUser());
            if (parameters.containsKey("sellerid"))
                parameters.remove("sellerid");
            else
                parameters.put("sellerid", seller.getAuthUser().getId().toString());
        } else if (parameters.containsKey("sellerid") && !parameters.get("sellerid").isEmpty()) {
            Long sellerId = Long.parseLong(parameters.get("sellerid"));
            if (sellerId == seller.getAuthUser().getId())
                sqlParameters.put("sellerid", seller.getAuthUser());
            else {
                AuthUser seller = authUserService.findById(sellerId);
                if (!Objects.isNull(seller))
                    sqlParameters.put("sellerid", seller);
            }
        }

        return sqlParameters;
    }

    private String getErrorMessage(String msg) {
        return switch (msg) {
            case "o1" -> "Change status is forbidden";
            case "o2" -> "Please set purchase price first";
            case "o3" -> "Offer can't be deleted";
            default -> "";
        };
    }
}
