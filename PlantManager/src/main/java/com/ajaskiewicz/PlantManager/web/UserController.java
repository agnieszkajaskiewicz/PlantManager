package com.ajaskiewicz.PlantManager.web;

import com.ajaskiewicz.PlantManager.model.User;
import com.ajaskiewicz.PlantManager.service.SecurityService;
import com.ajaskiewicz.PlantManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

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

        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/dashboard";
    }

    @GetMapping("/sign-in")
    public String login(Model model, String error, String logout) {
        if(securityService.isAuthenticated()) {
            return "redirect:/dashboard";
        }
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("error", "You have been logged out successfully.");
        return "signInPage";
    }

    @GetMapping("/")
    public String homePage() {
        return "homePage";
    }

}
