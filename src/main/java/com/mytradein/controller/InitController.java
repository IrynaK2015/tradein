package com.mytradein.controller;

import com.mytradein.model.*;
import com.mytradein.service.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class InitController
{
    final private BrandModelService brandModelService;

    private final AuthUserService authUserService;

    private final AuthtokenService authtokenService;

    private final PasswordEncoder passwordEncoder;

    public InitController(BrandModelService brandModelService,
                          AuthUserService authUserService,
                          AuthtokenService authtokenService,
                          PasswordEncoder passwordEncoder) {
        this.brandModelService = brandModelService;
        this.authUserService = authUserService;
        this.authtokenService = authtokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/init")
    public String loadInitData(Model model) {
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            return "redirect:/offers/list?status=" + OfferStatuses.NEW.name();
        }

        List<String> errors = new ArrayList<>();
        List<String> success = new ArrayList<>();
        boolean isIgnored = true;

        if (!authUserService.isAdminExists()) {
            try {
                isIgnored = false;
                String defaultAuth = "admin";
                createDefaultAdmin(defaultAuth);
                success.add("Default Supervisor created");
                model.addAttribute("defaultAuth", defaultAuth);
            } catch (Exception e) {
                errors.add("Default admin can't be created, " + e.getMessage());
            }
        }
        else success.add("At least one Supervisor existing");

        if (!brandModelService.isBrandsLoaded()) {
            isIgnored = false;
            try {
                initBrandModelLoad();
                success.add("Brands and models loaded");
            } catch (Exception e) {
                errors.add("Brand init load failed, " + e.getMessage());
            }
        }
        else success.add("Brands and models exist");

        String apiLogin = Constants.API_LOGIN;
        String apiPassword = Constants.API_PASSWORD;
        if (!authtokenService.isApiTokenConfigured()) {
            isIgnored = false;
            try {
                createApiToken(apiLogin, apiPassword);
                success.add("Rest API account created, please save it :  " + apiLogin + " / " + apiPassword);
            } catch (Exception e) {
                errors.add("API account wasn't created, " + e.getMessage());
            }
        } else success.add("Rest API account  exist, " + apiLogin + " / " + apiPassword);

        if (isIgnored) {
            return "/redirect:/login";
        }

        model.addAttribute("errors", errors);
        model.addAttribute("success", success);
        model.addAttribute("defaultAuth", "admin");

        return "init";
    }


    private void initBrandModelLoad() {
        Brand apple = new Brand("Apple");
        brandModelService.addBrand(apple);
        brandModelService.addModel(new Brandmodel(apple, "iPhone 16e"));
        brandModelService.addModel(new Brandmodel(apple, "iPhone 16 Plus"));
        brandModelService.addModel(new Brandmodel(apple, "iPhone 15 Pro Max"));
        brandModelService.addModel(new Brandmodel(apple, "iPhone 15 Plus"));
        brandModelService.addModel(new Brandmodel(apple, "iPhone 14 Pro Max"));

        Brand samsung = new Brand("Samsung");
        brandModelService.addBrand(samsung);
        brandModelService.addModel(new Brandmodel(samsung, "Galaxy F16"));
        brandModelService.addModel(new Brandmodel(samsung, "Galaxy A56"));
        brandModelService.addModel(new Brandmodel(samsung, "Galaxy S25"));
        brandModelService.addModel(new Brandmodel(samsung, "Galaxy S25 Ultra"));
        Brandmodel model = new Brandmodel(samsung, "Galaxy F06 5G");
        model.setEnabled(false);
        brandModelService.addModel(model);

        Brand lg = new Brand("LG");
        brandModelService.addBrand(lg);
        brandModelService.addModel(new Brandmodel(lg, "G7+"));
        brandModelService.addModel(new Brandmodel(lg, "Q7+"));
        brandModelService.addModel(new Brandmodel(lg, "V30+"));
        brandModelService.addModel(new Brandmodel(lg, "DESCOVER G6"));

        Brand nokia = new Brand("Nokia");
        nokia.setEnabled(false);
        brandModelService.addBrand(nokia);
    }

    private void createDefaultAdmin(String defaultUsername) {
        AuthUser user = new AuthUser(
                defaultUsername, passwordEncoder.encode(defaultUsername), "fake@nomail", AuthRoles.ADMIN.name(), "default admin"
        );
        authUserService.saveUser(user);
    }

    private void createApiToken(String apiLogin, String apiPassword) {
        LocalDate expired = LocalDate.now().plusMonths(6);
        Authtoken apiToken = new Authtoken(
            TokenTypes.API.name(),
                apiLogin,
                java.sql.Date.valueOf(expired)
        );

        apiToken.setPassword(Utilities.encodeSha256(apiPassword));
        authtokenService.saveAuthToken(apiToken);
    }
}
