package com.mytradein.controller;

import com.mytradein.model.AuthUser;
import com.mytradein.model.Authtoken;
import com.mytradein.service.AuthUserService;
import com.mytradein.service.AuthtokenService;
import com.mytradein.service.Constants;
import com.mytradein.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;

@Controller
public class TokenController {
    private final AuthUserService authUserService;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;
    private final AuthtokenService authtokenService;

    public TokenController(AuthUserService authUserService,
                           PasswordEncoder passwordEncoder,
                           EmailService emailService, AuthtokenService authtokenService) {
        this.authUserService = authUserService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.authtokenService = authtokenService;
    }

    @PostMapping("/remind")
    public String sendRemind(@RequestParam String email, Model model) {
        AuthUser authUser = authUserService.findByEmail(email);
        if (Objects.isNull(authUser)) {
            return "redirect:/error404/customer";
        }
        Authtoken currentToken = authtokenService.getActiveAuthToken(authUser.getUsername());
        if (Objects.isNull(currentToken)) {
            currentToken = authtokenService.createAuthToken(authUser.getUsername());
        }
        model.addAttribute("freeText", sendEmail(authUser, currentToken));

        return "index";
    }

    @GetMapping("/remind/{token}")
    public String reminderForm(@PathVariable("token") String token,
                               Model model) {
        if (token.isEmpty()) {
            return "redirect:/error404/token";
        }
        Authtoken foundToken = authtokenService.getActiveTokenByValue(token);
        if (Objects.isNull(foundToken)) {
            return "redirect:/error404/token";
        }
        AuthUser authUser = authUserService.findByLogin(foundToken.getLogin());
        if (Objects.isNull(authUser)) {
            return "redirect:/error404/customer";
        }
        model.addAttribute("token", token);

        return "remind";
    }

    @PostMapping("/remind/save")
    public String saveNewPassword(@RequestParam String password,
                                  @RequestParam String password1,
                                  @RequestParam String token,
                                  Model model
    ) {
        if (token.isEmpty()) {
            return "redirect:/error404/token";
        }
        Authtoken foundToken = authtokenService.getActiveTokenByValue(token);
        if (Objects.isNull(foundToken)) {
            return "redirect:/error404/token";
        }
        AuthUser authUser = authUserService.findByLogin(foundToken.getLogin());
        if (Objects.isNull(authUser)) {
            return "redirect:/error404/customer";
        }
        if (!password.equals(password1)) {
            model.addAttribute("error", "Passwords aren't equal");

            return "remind";
        }
        String passHash = passwordEncoder.encode(password);
        authUser.setPassword(passHash);
        authUserService.saveUser(authUser);
        authtokenService.deactivateToken(foundToken);

        return "redirect:/login";
    }

    private String sendEmail(AuthUser authUser, Authtoken token) {
        try {
            emailService.sendSimpleMessage(
                authUser.getEmail(),
                Constants.REMINDER_SUBJECT,
                String.format(
                    Constants.REMINDER_MESSAGE,
                    authUser.getFullName(),
                    ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/remind/" + token.getTokenValue(),
                    token.getExpired().toString()
                )
            );

            return "Sent successfully";
        } catch (Exception ex) {
            return "Error " + ex.getMessage();
        }
    }
}
