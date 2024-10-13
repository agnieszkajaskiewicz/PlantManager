package com.ajaskiewicz.PlantManager.service;

import jakarta.mail.MessagingException;
import java.util.Map;

public interface MailService {

    void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel) throws MessagingException;
}
