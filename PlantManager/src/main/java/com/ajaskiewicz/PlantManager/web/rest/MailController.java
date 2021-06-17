package com.ajaskiewicz.PlantManager.web.rest;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.service.MailService;
import com.ajaskiewicz.PlantManager.service.PlantService;
import com.ajaskiewicz.PlantManager.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.mail.MessagingException;
import java.util.Comparator;
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

        for (var i = 0; i < usersList.size(); i++) {

            var plantsToBeWateredSoon = plantService.findPlantsToBeWateredSoon(usersList.get(i).getId());
            plantsToBeWateredSoon.sort(Comparator.comparing(Plant::getWateringDifferenceInDays));

            if (plantsToBeWateredSoon.isEmpty()) {
                log.info("Nothing to be watered soon for user: " + usersList.get(i).getUsername() + " Reminder not sent.");
            }

            props.put("name", userService.findById(usersList.get(i).getId()).getUsername());
            props.put("plants", plantsToBeWateredSoon);

            mailService.sendMessageUsingThymeleafTemplate(userService.findById(usersList.get(i).getId()).getEmail(), "Plant Manager reminder", props);
            log.info("Email reminder sent to username: " + usersList.get(i).getUsername());
        }
    }
}
