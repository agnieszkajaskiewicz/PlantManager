package com.ajaskiewicz.PlantManager.web.rest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ajaskiewicz.PlantManager.exceptions.ControllerErrorHandler;
import com.ajaskiewicz.PlantManager.model.User;
import com.ajaskiewicz.PlantManager.service.UserService;

import jakarta.mail.internet.MimeMessage;

class ForgotPasswordControllerTest {

    private MockMvc mockMvc;

    private JavaMailSender mailSender;
    private UserService userService;

    private ForgotPasswordController forgotPasswordController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        mailSender = mock(JavaMailSender.class);
        openMocks(this);
        forgotPasswordController = new ForgotPasswordController(mailSender, userService);

        ReflectionTestUtils.setField(forgotPasswordController, "frontendUrl", "http://localhost:3000");
        
        mockMvc = MockMvcBuilders.standaloneSetup(forgotPasswordController)
                .setControllerAdvice(new ControllerErrorHandler())
                .build();
    }

    @Test
    void shouldProcessForgotPasswordV2() throws Exception {
        //given
        var email = "sample@email.com";
        when(mailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));

        //when && then
        mockMvc.perform(post("/forgotPassword/v2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + email + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("If that email exists in our system, we have sent a reset password link."));

        verify(userService).updateResetPasswordToken(anyString(), eq(email));
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(any(MimeMessage.class));

        verifyNoMoreInteractions(userService, mailSender);
    }

    @Test
    void shouldProcessResetPasswordV2() throws Exception {
        //given
        var token = "validToken123";
        var newPassword = "newSecurePassword";
        var email = "user@example.com";
        var user = new User();
        user.setId(1L);
        user.setEmail(email);
        
        when(userService.findByResetPasswordToken(token)).thenReturn(user);

        //when && then
        mockMvc.perform(post("/resetPassword/v2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\":\"" + token + "\",\"newPassword\":\"" + newPassword + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("You have successfully changed your password."));

        verify(userService).findByResetPasswordToken(token);
        verify(userService).updatePassword(user, newPassword);
        verify(userService).updateResetPasswordToken(null, email);
        
        verifyNoMoreInteractions(userService);
    }

    @Test
    void shouldReturnBadRequestWhenResetPasswordWithInvalidToken() throws Exception {
        //given
        var token = "invalidToken";
        var newPassword = "newSecurePassword";
        
        when(userService.findByResetPasswordToken(token)).thenReturn(null);

        //when && then
        mockMvc.perform(post("/resetPassword/v2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\":\"" + token + "\",\"newPassword\":\"" + newPassword + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid or expired token"));

        verify(userService).findByResetPasswordToken(token);
        
        verifyNoMoreInteractions(userService);
    }

    @Test
    void shouldValidateToken() throws Exception {
        //given
        var token = "validToken123";
        var user = new User();
        user.setId(1L);
        
        when(userService.findByResetPasswordToken(token)).thenReturn(user);

        //when && then
        mockMvc.perform(get("/resetPassword/v2/validate")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.message").value("Token is valid"));

        verify(userService).findByResetPasswordToken(token);
        
        verifyNoMoreInteractions(userService);
    }

    @Test
    void shouldReturnInvalidWhenValidatingInvalidToken() throws Exception {
        //given
        var token = "invalidToken";
        
        when(userService.findByResetPasswordToken(token)).thenReturn(null);

        //when && then
        mockMvc.perform(get("/resetPassword/v2/validate")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.message").value("Invalid or expired token"));

        verify(userService).findByResetPasswordToken(token);
        
        verifyNoMoreInteractions(userService);
    }
}