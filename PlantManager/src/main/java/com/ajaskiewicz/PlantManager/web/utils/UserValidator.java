package com.ajaskiewicz.PlantManager.web.utils;

import com.ajaskiewicz.PlantManager.model.User;
import com.ajaskiewicz.PlantManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        var user = (User) o;

        if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate", "Duplicate.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "repeatPassword", "NotEmpty");
        if (!user.getRepeatPassword().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Not matched", "Diff.userForm.passwordConfirm");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        if (userService.findByEmail(user.getEmail()) != null) {
            errors.rejectValue("email", "Duplicate", "Duplicate.userForm.email");
        }
    }
}
