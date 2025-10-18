package com.ajaskiewicz.PlantManager.web.rest;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ajaskiewicz.PlantManager.model.User;
import com.ajaskiewicz.PlantManager.service.UserService;
import com.ajaskiewicz.PlantManager.web.utils.WebUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ForgotPasswordController {

    private final JavaMailSender mailSender;
    private final UserService userService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Autowired
    public ForgotPasswordController(JavaMailSender mailSender, UserService userService) {
        this.mailSender = mailSender;
        this.userService = userService;
    }

    @GetMapping("/forgotPassword")
    public String forgotPassPage() {
        return "forgotPassPage";
    }

    @PostMapping("/forgotPassword")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomStringUtils.randomAlphabetic(30);

        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = WebUtil.getSiteURL(request) + "/resetPassword?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please, check your emailbox.");
        } catch (UsernameNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException exc) {
            model.addAttribute("error", "Error while sending email");
        }

        return "forgotPassPage";
    }

    @PostMapping(value = "/forgotPassword/v2", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> processForgotPasswordV2(@RequestBody @Valid ForgotPasswordRequest request) throws NoSuchAlgorithmException {
        String email = request.getEmail();
        String token = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(SecureRandom.getInstanceStrong().generateSeed(48));

        try {
            log.info("Password reset requested for email: {}", email);
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = frontendUrl + "/resetPassword?token=" + token;
            sendEmail(email, resetPasswordLink);
            return ResponseEntity.ok(new MessageResponse("If that email exists in our system, we have sent a reset password link."));
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.ok(new MessageResponse("If that email exists in our system, we have sent a reset password link."));
        } catch (UnsupportedEncodingException | MessagingException exc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error while sending email. Please try again later."));
        }

    }

    private void sendEmail(String recipientEmail, String link) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@plantmanager.com", "Plant Manager Support");
        helper.setTo(recipientEmail);

        var subject = "Here's the link to reset your password";

        var content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping("/resetPassword")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        User user = userService.findByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (user == null) {
            model.addAttribute("message", "Invalid token");
        }

        return "resetPassPage";
    }

    @PostMapping("/resetPassword")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        User user = userService.findByResetPasswordToken(token);

        if (user == null) {
            model.addAttribute("message", "Invalid token");
        } else {
            userService.updatePassword(user, password);
            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "resetPassPage";
    }

    @PostMapping(value = "/resetPassword/v2", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> processResetPasswordV2(@RequestBody @Valid ResetPasswordRequest request) {
        User user = userService.findByResetPasswordToken(request.getToken());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Invalid or expired token"));
        }

        userService.updatePassword(user, request.getNewPassword());
        userService.updateResetPasswordToken(null, user.getEmail());
        log.info("Password successfully reset for user: {}", user.getEmail());

        return ResponseEntity.ok(new MessageResponse("You have successfully changed your password."));
    }

    @GetMapping(value = "/resetPassword/v2/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenValidationResponse> validateTokenV2(@RequestParam("token") String token) {
        User user = userService.findByResetPasswordToken(token);

        if (user == null) {
            return ResponseEntity.ok(new TokenValidationResponse(false, "Invalid or expired token"));
        }

        return ResponseEntity.ok(new TokenValidationResponse(true, "Token is valid"));
    }

    @Setter
    @Getter
    public static class ForgotPasswordRequest {
        @NotEmpty(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;
    }

    @Setter
    @Getter
    public static class ResetPasswordRequest {
        @NotEmpty(message = "Token is required")
        private String token;

        @NotEmpty(message = "New password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String newPassword;
    }


    @Setter
    @Getter
    public static class MessageResponse {
        private String message;

        public MessageResponse(String message) {
            this.message = message;
        }
    }

    public static class TokenValidationResponse {
        private boolean valid;

        @Setter
        @Getter
        private String message;

        public TokenValidationResponse(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
    }
}
