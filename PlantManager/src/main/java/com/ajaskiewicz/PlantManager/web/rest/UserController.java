package com.ajaskiewicz.PlantManager.web.rest;

import com.ajaskiewicz.PlantManager.model.User;
import com.ajaskiewicz.PlantManager.service.SecurityService;
import com.ajaskiewicz.PlantManager.service.UserService;
import com.ajaskiewicz.PlantManager.web.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

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

    @PostMapping("/sign-up/v2")
    public ResponseEntity registration(@Valid @RequestBody User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getFieldErrors(), HttpStatus.BAD_REQUEST);
        }

        userService.save(user);
        securityService.autoLogin(user.getUsername(), user.getRepeatPassword());

        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/")
    public String homePage() {
        return "homePage";
    }

}
