package codewiththugboi.celowibank.service;


import codewiththugboi.celowibank.data.model.ConfirmationToken;
import codewiththugboi.celowibank.data.model.User;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenImpl implements ConfirmationTokenInterface{
    private final Map<String, ConfirmationToken> tokenMap;


    public boolean setConfirmedAt(String token) {
        ConfirmationToken confirmationToken = tokenMap.get(token);
        if (confirmationToken != null) {
            confirmationToken.setConfirmedAt(LocalDateTime.now());
            return true;
        }
        return false;
    }

    public void saveToken(ConfirmationToken token) {
        tokenMap.put(token.getToken(), token);
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteToken(String token) {
        tokenMap.remove(token);
    }

//    public String getToken(String token) {
//        return token;
//    }
//
//    public boolean setConfirmedAt(String token) {
//        String confirmationToken = getToken(token);
//        if (confirmationToken != null) {
//            User user = new User();
//            user.setConfirmAt(LocalDateTime.now());
//            return true;
//        }
//        return false;
//    }
}
