package codewiththugboi.celowibank.event;

import codewiththugboi.celowibank.dto.request.VerificationMessageRequest;
import codewiththugboi.celowibank.dto.response.MailResponse;
import codewiththugboi.celowibank.mail.EmailService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import org.thymeleaf.context.Context;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class SendMessageEventListener {
    @Qualifier("mailgun_sender")
    private final EmailService emailService;
    private final Environment env;


    public SendMessageEventListener(EmailService emailService, Environment env) {
        this.emailService = emailService;
        this.env = env;

    }

    @EventListener
    public void handleSendMessageEvent(SendMessageEvent event) throws UnirestException, ExecutionException, InterruptedException {
        VerificationMessageRequest messageRequest = (VerificationMessageRequest) event.getSource();

        String verificationLink = messageRequest.getDomainUrl() + "api/v1/register/" + messageRequest.getVerificationToken();

        log.info("Message request --> {}", messageRequest);
        Context context = new Context();
        context.setVariable("user_name", messageRequest.getUsersFullName().toUpperCase());
        context.setVariable("verification_token", verificationLink);
        if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
            log.info("Message Event -> {}", event.getSource());

            MailResponse mailResponse = emailService.sendHtmlMail(messageRequest).get();
            log.info("Mail Response --> {}", mailResponse);
        } else {
            messageRequest.setBody(verificationLink);
            MailResponse mailResponse = emailService.sendSimpleMail(messageRequest).get();
            log.info("Mail Response --> {}", mailResponse);
        }
    }
}
