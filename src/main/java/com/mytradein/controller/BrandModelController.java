package com.mytradein.controller;

import com.mytradein.model.Brand;
import com.mytradein.model.Brandmodel;
import com.mytradein.service.BrandModelService;
import com.mytradein.service.OfferService;
import com.mytradein.service.Utilities;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/brand")
public class BrandModelController {

    final private BrandModelService brandModelService;

    final private OfferService offerService;

    public BrandModelController(BrandModelService brandModelService,
                                OfferService offerService) {
        this.brandModelService = brandModelService;
        this.offerService = offerService;
    }

    @GetMapping("/list")
    public String brandList(Model model) {
        List<Brand> brands = brandModelService.findAllBrands();
        model.addAttribute("brands", brands);
        model.addAttribute("isAdmin", Utilities.isCurrentUserSupervisor());

        return "brands";
    }

    @PostMapping("/save")
    public String brandSave(@RequestParam Long id,
                            @RequestParam String enable,
                            Model model) {
        if (!Utilities.isCurrentUserSupervisor()) {
            return "redirect:/brand/list";
        }
        if (id < 0) id = 0L;
        try {
            Brand brand = brandModelService.getBrand(id);
            if (Objects.isNull(brand)) {
                return "redirect:/error404/brand";
            }
            switch (enable) {
                case "Y":
                    if (!brandModelService.isBrandmodelEnabledExist(brand))
                        return "redirect:/error409/brand?msg=no models enabled";

                    brandModelService.enableBrand(brand);
                    break;
                case "N":
                    brandModelService.disableBrand(brand);
                    break;
            }

            return "redirect:/brand/list";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            List<Brand> brands = brandModelService.findAllBrands();
            model.addAttribute("brands", brands);

            return "brands";
        }
    }

    @PostMapping("/new")
    public String createBrand(@RequestParam String brandname,
                              Model model) {
        if (!Utilities.isCurrentUserSupervisor()) {
            return "redirect:/brand/list";
        }
        try {
            Brand foundBrand = brandModelService.findBrandByName(brandname);
            if (!Objects.isNull(foundBrand)) {
                return "redirect:/error409/brand?msg=already exists";
            }
            Brand brand = new Brand(brandname);
            brand.setEnabled(false);
            brandModelService.addBrand(brand);

            return "redirect:/brand/list";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());

            return "brands";
        }
    }

    @PostMapping("/delete")
    public String deleteBrand(@RequestParam Long id,
                              Model model) {
        if (!Utilities.isCurrentUserSupervisor()) {
            return "redirect:/brand/list";
        }
        if (id < 0) id = 0L;
        try {
            Brand brand = brandModelService.getBrand(id);
            if (Objects.isNull(brand))
                return "redirect:/error404/brand";

            if (brandModelService.isModelsFoundInBrand(brand))
                return "redirect:/error403/brand";

            brandModelService.deleteBrand(brand);

            return "redirect:/brand/list";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            List<Brand> brands = brandModelService.findAllBrands();
            model.addAttribute("brands", brands);

            return "brands";
        }
    }

    @GetMapping("/{id}/list")
    public String brandmodelList(@PathVariable("id") Long brandId,
                                 Model model) {
        Brand brand = brandModelService.getBrand(brandId);
        if (Objects.isNull(brand))
            return "redirect:/error404/brand";

        model.addAttribute("brand", brand);
        model.addAttribute("isAdmin", Utilities.isCurrentUserSupervisor());

        return "brandmodels";
    }

    @PostMapping("/model/save")
    public String brandmodelSave(@RequestParam Long id,
                                 @RequestParam String enable,
                                 Model model) {
        if (!Utilities.isCurrentUserSupervisor()) {
            return "redirect:/brand/list";
        }
        if (id < 0) id = 0L;
        Brandmodel brandmodel = brandModelService.getModel(id);
        if (Objects.isNull(brandmodel))
            return "redirect:/error404/model";
        try {
            switch (enable) {
                case "Y":
                    brandModelService.enableModel(brandmodel);
                    break;
                case "N":
                    brandModelService.disableModel(brandmodel);
                    Brand brand = brandmodel.getBrand();
                    if (!brandModelService.isBrandmodelEnabledExist(brand))
                        brandModelService.disableBrand(brand);
                    break;
            }

            return "redirect:/brand/" + brandmodel.getBrand().getId() + "/list";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("brand", brandmodel.getBrand());

            return "brandmodels";
        }
    }

    @PostMapping("/model/new")
    public String createBrandModel(@RequestParam String modelname,
                                   @RequestParam Long brandId,
                                   Model model) {
        if (!Utilities.isCurrentUserSupervisor()) {
            return "redirect:/brand/list";
        }
        Brand brand = brandModelService.getBrand(brandId);
        if (Objects.isNull(brand))
            return "redirect:/error404/brand";

        Brandmodel foundModel = brandModelService.findModelByName(modelname);
        if (!Objects.isNull(foundModel))
            return "redirect:/error409/brand?msg=already exists";

        try {
            Brandmodel brandmodel = new Brandmodel(brand, modelname);
            brandmodel.setEnabled(false);
            brandModelService.addModel(brandmodel);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
        model.addAttribute("brand", brand);

        return "brandmodels";
    }

    @PostMapping("/model/delete")
    public String deleteBrandModel(@RequestParam Long id,
                              Model model) {
        if (!Utilities.isCurrentUserSupervisor()) {
            return "redirect:/brand/list";
        }
        Brandmodel brandmodel = brandModelService.getModel(id);
        if (Objects.isNull(brandmodel))
            return "redirect:/error404/model";

        if (offerService.isModelOffersFound(brandmodel))
            return "redirect:/error409/model?msg=offer exist";

        brandModelService.deleteModel(brandmodel);

        return "redirect:/brand/" + brandmodel.getBrand().getId() + "/list";
    }

    @ModelAttribute("username")
    public String getUsername() {
        return Utilities.getCurrentUser().getUsername();
    }
}
