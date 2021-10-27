package com.ajaskiewicz.PlantManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {

    @Value("classpath:/static/logoPlantManager.png")
    private Resource resourceFile;

    private SpringTemplateEngine thymeleafTemplateEngine;
    private JavaMailSender mailSender;

    @Autowired
    public MailServiceImpl(SpringTemplateEngine thymeleafTemplateEngine, JavaMailSender mailSender) {
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
        this.mailSender = mailSender;
    }

    @Override
    public void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel) throws MessagingException {
        var thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        var htmlBody = thymeleafTemplateEngine.process("reminderTemplate.html", thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.addInline("attachment.png", resourceFile);

        mailSender.send(message);
    }
}
