package com.ajaskiewicz.PlantManager.web.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ajaskiewicz.PlantManager.model.User;
import com.ajaskiewicz.PlantManager.service.UserService;

@Component
public class UserValidator implements Validator {

    private final UserService userService;
    private final MessageSource messageSource;

    @Autowired
    public UserValidator(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        if (userService.existsByUsername(user.getUsername())) {
            errors.rejectValue("username", "Duplicate.userForm.username", getMessage("Duplicate.userForm.username"));
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "repeatPassword", "NotEmpty");
        if (!user.getRepeatPassword().equals(user.getPassword())) {
            errors.rejectValue("repeatPassword", "Diff.userForm.repeatPassword", getMessage("Diff.userForm.repeatPassword"));
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        if (userService.existsByEmail(user.getEmail())) {
            errors.rejectValue("email", "Duplicate.userForm.email", getMessage("Duplicate.userForm.email"));
        }
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, code, LocaleContextHolder.getLocale());
    }
}
