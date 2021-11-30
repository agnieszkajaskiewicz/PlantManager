package com.ajaskiewicz.PlantManager.web.rest;

import com.ajaskiewicz.PlantManager.model.User;
import com.ajaskiewicz.PlantManager.service.SecurityService;
import com.ajaskiewicz.PlantManager.service.UserService;
import com.ajaskiewicz.PlantManager.web.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    private UserService userService;
    private SecurityService securityService;
    private UserValidator userValidator;

    @Autowired
    public UserController(UserService userService, SecurityService securityService, UserValidator userValidator) {
        this.userService = userService;
        this.securityService = securityService;
        this.userValidator = userValidator;
    }

    @GetMapping("/sign-up")
    public String registration(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/dashboard";
        }

        model.addAttribute("userForm", new User());

        return "signUpPage";
    }

    @PostMapping("/sign-up")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "signUpPage";
        }

        userService.save(userForm);
        securityService.autoLogin(userForm.getUsername(), userForm.getRepeatPassword());

        return "redirect:/dashboard";
    }

    @PostMapping("/sign-up/v2")
    public ResponseEntity<String> registration(@RequestBody User user) {
        userService.save(user);
        securityService.autoLogin(user.getUsername(), user.getRepeatPassword());

        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/sign-in") //ten endpoint jest wołany tylko na logout, prawdopodobnie go nie potrzebujemy
    public String login(Model model, String error, String logout) {
        if (securityService.isAuthenticated()) {
            return "redirect:/dashboard";
        }

        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid.");
        }

        if (logout != null) {
            model.addAttribute("error", "You have been logged out successfully.");
        }

        return "signInPage";
    }

    @GetMapping("/")
    public String homePage() {
        return "homePage";
    }

}
