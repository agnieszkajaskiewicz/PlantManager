package com.ajaskiewicz.PlantManager.web.rest;

import com.ajaskiewicz.PlantManager.service.MailService;
import com.ajaskiewicz.PlantManager.service.PlantService;
import com.ajaskiewicz.PlantManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MailController {

    MailService mailService;
    UserService userService;
    PlantService plantService;

    @Autowired
    public MailController(MailService mailService, UserService userService, PlantService plantService) {
        this.mailService = mailService;
        this.userService = userService;
        this.plantService = plantService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void sendReminder() throws MessagingException {
        Map<String, Map<String, Object>> labels = new HashMap<>();
        Map<String, Object> props = new HashMap<>();
        props.put("name", userService.findById(1).getUsername());
        props.put("plants", plantService.findPlantsToBeWateredSoon(1));

        labels.put("sendTemplate", props);
        mailService.sendMessageUsingThymeleafTemplate(userService.findById(1).getEmail(), "Plant Manager reminder", props);

    }
}
