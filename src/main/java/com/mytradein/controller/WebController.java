package com.mytradein.controller;

import com.mytradein.service.Utilities;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class WebController {
    public WebController() {
    }

    @GetMapping("/")
    public String onIndex(Model model) {

        return "index";
    }

    @GetMapping("/error404/{entry}")
    public String notFound(@PathVariable String entry,
                           Model model) {
        model.addAttribute("entry", entry);
        return "404";
    }

    @GetMapping("/error403/{entry}")
    public String operationForbidden(@PathVariable String entry,
                                     Model model) {
        model.addAttribute("entry", entry);
        return "403";
    }

    @GetMapping("/error409/{entry}")
    public String conflictResourceStatus(@PathVariable String entry,
                                         @RequestParam String msg,
                                         Model model) {
        model.addAttribute("entry", entry);
        model.addAttribute("msg", msg);

        return "409";
    }

    @ModelAttribute("username")
    public String getUsername() {
        return Utilities.getCurrentUser().getUsername();
    }

    @GetMapping("/error422/{entry}")
    public String unprocessableEntity(@PathVariable String entry,
                                      @RequestParam String msg,
                                      Model model) {
        model.addAttribute("entry", entry);
        model.addAttribute("msg", msg);

        return "422";
    }
}
