package com.ajaskiewicz.PlantManager.web.rest;

import com.ajaskiewicz.PlantManager.model.User;
import com.ajaskiewicz.PlantManager.service.SecurityService;
import com.ajaskiewicz.PlantManager.service.UserService;
import com.ajaskiewicz.PlantManager.web.utils.UserValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    private static final String USERNAME = "Grazyna";
    private static final String EMAIL = "grazyna@grazka.pl";
    private static final String PASSWORD = "janusz";
    private static final String REPEAT_PASSWORD = "janusz";
    private static final String TOKEN = "1234";

    private MockMvc mockMvc;
    private UserValidator userValidator;
    private UserService userService;
    private SecurityService securityService;
    private UserController userController;
    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        userValidator = mock(UserValidator.class);
        userService = mock(UserService.class);
        securityService = mock(SecurityService.class);
        userController = new UserController(userService, securityService, userValidator);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void shouldRegisterValidUser() throws Exception {
        //given
        var userEntity = new User();
        userEntity.setUsername(USERNAME);
        userEntity.setEmail(EMAIL);
        userEntity.setPassword(PASSWORD);
        userEntity.setRepeatPassword(REPEAT_PASSWORD);

        var userValueCapture = ArgumentCaptor.forClass(User.class);
        var errorsValueCapture = ArgumentCaptor.forClass(Errors.class);
        doNothing().when(userValidator).validate(userValueCapture.capture(), errorsValueCapture.capture());
        doNothing().when(userService).save(userValueCapture.capture());
        when(securityService.login(USERNAME, PASSWORD)).thenReturn(TOKEN);

        //when && then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/sign-up/v2")
                        .content(mapper.writeValueAsString(userEntity))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", "Bearer " + TOKEN))
                .andExpect(header().string("username", USERNAME));

        verify(userValidator).validate(userValueCapture.getValue(), errorsValueCapture.getValue());
        verify(userService).save(userValueCapture.getValue());
        verify(securityService).login(userEntity.getUsername(), userEntity.getRepeatPassword());
        verifyNoMoreInteractions(userValidator, userService, securityService);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Jola", "WspolneKontoGrazynyJoliJanuszaIKota"})
    void shouldNotRegisterUserWithInvalidUsername(String invalidUserName) throws Exception {
        //given
        var userEntity = new User();
        userEntity.setUsername(invalidUserName);
        userEntity.setEmail(EMAIL);
        userEntity.setPassword(PASSWORD);
        userEntity.setRepeatPassword(REPEAT_PASSWORD);

        var userValueCapture = ArgumentCaptor.forClass(User.class);
        var errorsValueCapture = ArgumentCaptor.forClass(Errors.class);
        doNothing().when(userValidator).validate(userValueCapture.capture(), errorsValueCapture.capture());
        doNothing().when(userService).save(userValueCapture.capture());

        var expectedErrorMessage = List.of("Please use between 6 and 32 characters.", "username");

        //when && then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/sign-up/v2")
                        .content(mapper.writeValueAsString(userEntity))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$..defaultMessage", is(expectedErrorMessage)));

        verify(userValidator).validate(userValueCapture.getValue(), errorsValueCapture.getValue());
        assertThat(errorsValueCapture.getAllValues().getFirst().getErrorCount()).isEqualTo(1);
        verifyNoMoreInteractions(userValidator, userService, securityService);
    }

    @ParameterizedTest
    @ValueSource(strings = {"username", "email", "password"})
    void shouldNotRegisterUserWithEmptyRequiredValue(String field) throws Exception {
        //given
        var userEntity = new User();
        switch(field) {
            case "username":
                userEntity.setUsername("");
                userEntity.setEmail(EMAIL);
                userEntity.setPassword(PASSWORD);
                break;
            case "email":
                userEntity.setUsername(USERNAME);
                userEntity.setEmail("");
                userEntity.setPassword(PASSWORD);
                break;
            case "password":
                userEntity.setUsername(USERNAME);
                userEntity.setEmail(EMAIL);
                userEntity.setPassword("");
                break;
        }
        userEntity.setRepeatPassword(REPEAT_PASSWORD);

        var userValueCapture = ArgumentCaptor.forClass(User.class);
        var errorsValueCapture = ArgumentCaptor.forClass(Errors.class);
        doNothing().when(userValidator).validate(userValueCapture.capture(), errorsValueCapture.capture());
        doNothing().when(userService).save(userValueCapture.capture());

        var expectedErrorMessage = "This field is required.";

        //when && then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/sign-up/v2")
                        .content(mapper.writeValueAsString(userEntity))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userValidator).validate(userValueCapture.getValue(), errorsValueCapture.getValue());

        var errorDefaultMessagesList = errorsValueCapture
                .getAllValues()
                .getFirst()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        assertThat(errorDefaultMessagesList).contains(expectedErrorMessage);

        verifyNoMoreInteractions(userValidator, userService, securityService);
    }

    @ParameterizedTest
    @ValueSource(strings = {"jola@", "jolaatjola.pl", "@jola.pl"})
    void shouldNotRegisterUserWithInvalidEmail(String invalidEmail) throws Exception {
        //given
        var userEntity = new User();
        userEntity.setUsername(USERNAME);
        userEntity.setEmail(invalidEmail);
        userEntity.setPassword(PASSWORD);
        userEntity.setRepeatPassword(REPEAT_PASSWORD);

        var userValueCapture = ArgumentCaptor.forClass(User.class);
        var errorsValueCapture = ArgumentCaptor.forClass(Errors.class);
        doNothing().when(userValidator).validate(userValueCapture.capture(), errorsValueCapture.capture());
        doNothing().when(userService).save(userValueCapture.capture());

        var expectedErrorMessage = "Use proper email format.";

        //when && then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/sign-up/v2")
                        .content(mapper.writeValueAsString(userEntity))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userValidator).validate(userValueCapture.getValue(), errorsValueCapture.getValue());

        var errorDefaultMessagesList = errorsValueCapture
                .getAllValues()
                .getFirst()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        assertThat(errorDefaultMessagesList).contains(expectedErrorMessage);

        verifyNoMoreInteractions(userValidator, userService, securityService);
    }

    @Test
    void shouldLoginUser() throws Exception {
        when(securityService.login(USERNAME, PASSWORD)).thenReturn(TOKEN);

        //when && then
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/sign-in/v2")
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", "Bearer " + TOKEN))
                .andExpect(header().string("username", USERNAME));

        verify(securityService).login(USERNAME, PASSWORD);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void shouldReturn401ForInvalidUserCredentials() throws Exception {
        var errorMessage = "Invalid username or password";
        when(securityService.login(USERNAME, PASSWORD)).thenThrow(new BadCredentialsException(errorMessage));

        //when && then
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/sign-in/v2")
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(errorMessage))
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(header().doesNotExist("username"));

        verify(securityService).login(USERNAME, PASSWORD);
        verifyNoMoreInteractions(securityService);
    }
}