package com.mytradein.controller;

import com.mytradein.dto.CustomerDTO;
import com.mytradein.model.Customer;
import com.mytradein.service.CustomerService;
import com.mytradein.service.Constants;
import com.mytradein.service.OfferService;
import com.mytradein.service.Utilities;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    private final OfferService offerService;

    public CustomerController(CustomerService customerService,
                              OfferService offerService) {
        this.customerService = customerService;
        this.offerService = offerService;
    }

    @GetMapping("/list")
    public String customerList(Model model,
                               @RequestParam(required = false, defaultValue = "0") Integer page) {
        if (page < 0) page = 0;
        else if (page > 0) page--;

        Page customersPage = customerService.findAllCustomers(
                PageRequest.of(page, Constants.ITEMS_PER_PAGE, Sort.Direction.ASC, "firstname")
        );
        model.addAttribute("customers", customersPage.getContent());
        model.addAttribute("customersPage", customersPage);

        List<Integer> pageNumbers = Utilities.getPageNumbers(customersPage);
        if (Objects.nonNull(pageNumbers))   model.addAttribute("pageNumbers", pageNumbers);

        model.addAttribute("newCustomer", new CustomerDTO());

        return "customers";
    }

    @PostMapping("/new")
    public String createCustomer(@Valid @ModelAttribute("newCustomer") CustomerDTO customerDTO,
                                 BindingResult bindingResult,
                                 Model model) {
        Customer customer = null;
        if (!bindingResult.hasErrors()) {
            customer = customerDTO.getCustomer();
            if (Objects.nonNull(customerService.findCustomerByEmail(customer.getEmail()))) {
                bindingResult.rejectValue("email", "Customer with email already exists");
            }
            if (Objects.nonNull(customerService.findCustomerByPhone(customer.getPhone()))) {
                bindingResult.rejectValue("phone", "Customer with phone already exists");
            }
        }
        if (bindingResult.hasErrors()) {

            return "customers";
        } else {
            customerService.addCustomer(customer);

            return "redirect:/customer/list";
        }
    }

    @GetMapping("/{id}")
    public String customerForm(@PathVariable(value = "id") Long customerId,
                               Model model) {
        if (customerId < 0) customerId = 0L;
        Customer customer = customerService.findCustomerById(customerId);
        if (Objects.isNull(customer))
            return "redirect:/error404/customer";

        model.addAttribute("customerDTO", new CustomerDTO(customer));

        return "customer";
    }

    @PostMapping("/save")
    public String customerSave(@Valid @ModelAttribute("customerDTO") CustomerDTO customerDTO,
                               BindingResult bindingResult,
                               Model model) {
        Long customerId = Long.parseLong(customerDTO.getId());
        if (customerId < 0) customerId = 0L;
        Customer oldCustomer = customerService.findCustomerById(customerId);
        if (Objects.isNull(oldCustomer))
            return "redirect:/error404/customer";

        Customer customer = customerDTO.getCustomer();
        customer.setId(customerId);
        if (!bindingResult.hasErrors()) {
            if (!customer.getEmail().equals(oldCustomer.getEmail()) && Objects.nonNull(customerService.findCustomerByEmail(customer.getEmail()))) {
                bindingResult.rejectValue("email", "error.email", "Customer with email already exists");
            }
            if (!Objects.equals(customer.getPhone(), oldCustomer.getPhone())  && Objects.nonNull(customerService.findCustomerByPhone(customer.getPhone()))) {
                bindingResult.rejectValue("phone", "error.phone", "Customer with phone already exists");
            }
        }
        if (bindingResult.hasErrors()) {

            return "customer";
        } else {
            customerService.saveCustomer(customer);

            return "redirect:/customer/" + customerId;
        }
    }

    @PostMapping("/{id}/delete")
    public String customerDelete(@PathVariable(value = "id") Long customerId,
                                 Model model) {
        if (!Utilities.isCurrentUserSupervisor())
            return "redirect:/error403/customer";

        if (customerId < 0) customerId = 0L;
        Customer customer = customerService.findCustomerById(customerId);
        if (Objects.isNull(customer))
            return "redirect:/error404/customer";

        if (offerService.countCustomerOffers(customer) > 0)
            return "redirect:/error409/customer?msg=offers exist";

        customerService.deleteCustomer(customer);

        return "redirect:/customer/list";
    }

    @ModelAttribute("username")
    public String getUsername() {
        return Utilities.getCurrentUser().getUsername();
    }
}
