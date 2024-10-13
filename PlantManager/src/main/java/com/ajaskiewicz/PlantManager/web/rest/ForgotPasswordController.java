package com.ajaskiewicz.PlantManager.web.rest;

import com.ajaskiewicz.PlantManager.service.UserService;
import com.ajaskiewicz.PlantManager.web.utils.WebUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {

    private JavaMailSender mailSender;
    private UserService userService;

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
        var email = request.getParameter("email");
        var token = RandomStringUtils.randomAlphabetic(30);

        try {
            userService.updateResetPasswordToken(token, email);
            var resetPasswordLink = WebUtil.getSiteURL(request) + "/resetPassword?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please, check your emailbox.");
        } catch (UsernameNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException exc) {
            model.addAttribute("error", "Error while sending email");
        }

        return "forgotPassPage";
    }

    public void sendEmail(String recipientEmail, String link) throws UnsupportedEncodingException, MessagingException {
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message);

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
        var user = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (user == null) {
            model.addAttribute("message", "Invalid token");
        }

        return "resetPassPage";
    }

    @PostMapping("/resetPassword")
    public String processResetPassword(HttpServletRequest request, Model model) {
        var token = request.getParameter("token");
        var password = request.getParameter("password");

        var user = userService.getByResetPasswordToken(token);

        if (user == null) {
            model.addAttribute("message", "Invalid token");
        } else {
            userService.updatePassword(user, password);
            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "resetPassPage";
    }

}
