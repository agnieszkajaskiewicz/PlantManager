package com.ajaskiewicz.PlantManager.web.rest;

import com.ajaskiewicz.PlantManager.model.User;
import com.ajaskiewicz.PlantManager.service.MailService;
import com.ajaskiewicz.PlantManager.service.PlantService;
import com.ajaskiewicz.PlantManager.service.UserService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Slf4j
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

    @Scheduled(cron = "* 0 7 */3 * *") //Email reminder is sent every 3 days at 7 am.
    public void sendReminder() throws MessagingException {
        Map<String, Object> props = new HashMap<>();

        var usersList = userService.findAll();

        for (User value : usersList) {
            User user = userService.findById(value.getId());

            Long userId = user.getId();
            String username = user.getUsername();
            String email = user.getEmail();

            var plantsToBeWateredSoon = plantService.findPlantsToBeWateredSoon(userId);

            if (plantsToBeWateredSoon.isEmpty()) {
                log.info("Nothing to be watered soon for user " + username + ". Reminder not sent.");
            }

            props.put("name", username);
            props.put("plants", plantsToBeWateredSoon);

            mailService.sendMessageUsingThymeleafTemplate(email, "Plant Manager reminder", props);
            log.info("Email reminder sent to user " + username);
        }
    }
}
