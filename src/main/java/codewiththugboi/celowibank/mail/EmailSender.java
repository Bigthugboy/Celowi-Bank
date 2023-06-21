package codewiththugboi.celowibank.mail;

import codewiththugboi.celowibank.dto.request.VerificationMessageRequest;
import codewiththugboi.celowibank.dto.response.MailResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.concurrent.CompletableFuture;

public interface EmailSender {
    CompletableFuture<MailResponse> sendHtmlMail(VerificationMessageRequest messageRequest)throws UnirestException; ;
    CompletableFuture<MailResponse> sendSimpleMail(VerificationMessageRequest messageRequest)throws UnirestException;

}
