package com.mytradein.controller;

import com.mytradein.model.AuthRoles;
import com.mytradein.model.AuthUser;
import com.mytradein.model.Seller;
import com.mytradein.service.*;
import com.mytradein.security.AuthenticationFacade;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.User;

import java.util.*;

@Controller
public class UserController {
    private final AuthUserService authUserService;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    private final Map<String, String> roleMap = new HashMap<>();

    public UserController(AuthUserService authUserService,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService,
                          AuthenticationFacade authenticationFacade) {
        this.authUserService = authUserService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.authenticationFacade = authenticationFacade;
    }

    @GetMapping("/user/list")
    public String getAuthUserList(Model model) {
        Seller seller = getSeller();
        if (!seller.isSupervisor())
            return "redirect:/user/" + seller.getSellerId();

        List<AuthUser> users = authUserService.getAllUsers();
        users.forEach(authUser -> authUser.setRole(getRoleLabel(authUser.getRole())));
        model.addAttribute("authUsers", users);

        return "users";
    }

    @GetMapping("/user")
    public String newAuthUser(Model model) {
        Seller seller = getSeller();

        return seller.isSupervisor() ? "user_new" : "redirect:/user/" + seller.getSellerId();
    }

    @PostMapping("/user")
    public String createAuthUser(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String password1,
                                 @RequestParam String email,
                                 @RequestParam String role,
                                 @RequestParam String fullname,
                                             Model model) {
        Seller seller = getSeller();
        if (!seller.isSupervisor())
            return "redirect:/user/" + seller.getSellerId();

        AuthUser user = new AuthUser();
        try {
            validateUsername(user, username);
            user.setUsername(username);
            validateEmail(user, email);
            user.setEmail(email);
            validateRole(user, role);
            user.setRole(role);
            user.setFullName(fullname);
            validatePassword(user, password, password1);
            String passHash = passwordEncoder.encode(password);
            user.setPassword(passHash);
            authUserService.saveUser(user);

            sendEmail(user, model);

            return "redirect:/user/list";

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());

            return "user_new";
        }
    }

    @GetMapping("/user/{id}")
    public String editAuthUser(@PathVariable Long id,
                               Model model) {
        if (id < 0) id = 0L;
        Seller seller = getSeller();
        if (!seller.isSupervisor() && !seller.getSellerId().equals(id))
            return "redirect:/error403/user";

        AuthUser user = authUserService.findById(id);
        if (Objects.isNull(user))
            return "redirect:/error404/user";

        model.addAttribute("authUser", user);
        model.addAttribute("rolePermitted", seller.isSupervisor());
        model.addAttribute("roleTitle", getRoleLabel(user.getRole()));

        return "user";
    }

    @PostMapping("/user/{id}")
    public String saveAuthUser(@PathVariable(value = "id") Long id,
                               @RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String role,
                               @RequestParam String password,
                               @RequestParam String password1,
                               @RequestParam String fullname,
                               Model model) {
        if (id < 0) id = 0L;
        Seller seller = getSeller();
        if (!seller.isSupervisor() && !seller.getSellerId().equals(id))
            return "redirect:/error403/user";

        AuthUser user = authUserService.findById(id);
        if (Objects.isNull(user))
            return "redirect:/error404/user";

        try {
           validateUsername(user, username);
           user.setUsername(username);
           validateEmail(user, email);
           user.setEmail(email);
           validateRole(user, role);
           user.setRole(role);
           user.setFullName(fullname);

            if (!password.isEmpty()) {
                validatePassword(user, password, password1);
                String passHash = passwordEncoder.encode(password);
                user.setPassword(passHash);
            }
            authUserService.saveUser(user);

            return "redirect:/user/list";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
        model.addAttribute("authUser", user);
        model.addAttribute("rolePermitted", seller.isSupervisor());
        model.addAttribute("roleTitle", getRoleLabel(user.getRole()));

        return "user";
    }

    @PostMapping("/user/{id}/delete")
    public String deleteAuthUser(@PathVariable(value = "id") Long id,
                                Model model) {
        Seller seller = getSeller();
        if (!seller.isSupervisor() || seller.getSellerId().equals(id)) {
            return "redirect:/error403/user";
        }

        if (id < 0) id = 0L;
        AuthUser user = authUserService.findById(id);
        if (Objects.isNull(user)) {
            return "redirect:/error404/user";
        }

        authUserService.removeUser(user);

        return "redirect:/user/list";
    }

    @ModelAttribute("username")
    public String getUsername() {
        return Utilities.getCurrentUser().getUsername();
    }

    private String getRoleLabel(String roleName) {
        if (roleMap.isEmpty()) {
            for (AuthRoles role: AuthRoles.values())
                roleMap.put(role.name(), role.getLabel());
        }

        return roleMap.containsKey(roleName) ? roleMap.get(roleName) : roleName;
    }

    private void validateUsername(AuthUser user, String username) throws IllegalArgumentException {
        if (username.isEmpty())
            throw new IllegalArgumentException("Username cannot be empty");
        if ((Objects.isNull(user.getUsername()) || !user.getUsername().equals(username))
                && Objects.nonNull(authUserService.findByLogin(username)))
            throw new IllegalArgumentException("Username already exists");
    }

    private void validateEmail(AuthUser user, String email) throws IllegalArgumentException, AddressException {
        if (email.isEmpty())
            throw new IllegalArgumentException("Email cannot be empty");
        if ((Objects.isNull(user.getEmail()) || !user.getEmail().equals(email))
                && Objects.nonNull(authUserService.findByEmail(email)))
            throw new IllegalArgumentException("Email already exists");
        new InternetAddress(email).validate();
    }

    private void validateRole(AuthUser user, String role) {
        if (!EnumUtils.isValidEnum(AuthRoles.class, role))
            throw new RuntimeException("Invalid role");
    }

    private void validatePassword(AuthUser user, String password, String password1) throws IllegalArgumentException {
        if (Objects.isNull(user.getPassword()) || !password.isEmpty()) {
            if (!password.equals(password1))
                throw new IllegalArgumentException("Passwords aren't equal");
        }
    }

    private void sendEmail(AuthUser authUser, Model model) {
        try {
            emailService.sendSimpleMessage(
                    authUser.getEmail(),
                    Constants.REGISTRATION_SUBJECT,
                    String.format(Constants.REGISTRATION_MESSAGE, authUser.getUsername(), authUser.getRole())
            );
            model.addAttribute("sendmailResult", "Sent successfully");
        } catch (Exception ex) {
            model.addAttribute("sendmailResult", "Error " + ex.getMessage());
        }

        model.addAttribute("email", authUser.getEmail());
    }

    private Seller getSeller() {
        User currentUser = (User)authenticationFacade.getAuthentication().getPrincipal();

        return new Seller(currentUser, authUserService.findByLogin(currentUser.getUsername()));
    }
}
