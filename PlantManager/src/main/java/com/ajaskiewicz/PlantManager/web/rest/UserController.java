package com.ajaskiewicz.PlantManager.web.rest;

import com.ajaskiewicz.PlantManager.model.User;
import com.ajaskiewicz.PlantManager.service.SecurityService;
import com.ajaskiewicz.PlantManager.service.UserService;
import com.ajaskiewicz.PlantManager.web.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/sign-up") // old endpoint, to be removed
    public String registration(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/dashboard";
        }

        model.addAttribute("userForm", new User());

        return "signUpPage";
    }

    @PostMapping("/sign-up/v2")
    public ResponseEntity<?> registration(@Valid @RequestBody User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getFieldErrors(), HttpStatus.BAD_REQUEST);
        }

        userService.save(user);
        String jwtToken = securityService.login(user.getUsername(), user.getRepeatPassword());

        return ResponseEntity.ok().header("Authorization", "Bearer " + jwtToken).header("username", user.getUsername()).build();
    }

    @PostMapping(path = "/sign-in/v2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> login(@Valid @RequestParam("username") String username, @Valid @RequestParam("password") String password) {
        try {
            String jwtToken = securityService.login(username, password);
            return ResponseEntity.ok().header("Authorization", "Bearer " + jwtToken).header("username", username).build();
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/sign-in") // old endpoint, to be removed
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

    @GetMapping("/") // old endpoint, to be removed
    public String homePage() {
        return "homePage";
    }
}
