//package codewiththugboi.celowibank.sms;
//
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//@AllArgsConstructor
//@NoArgsConstructor
//public class TwilioSmsService {
//    @Value("${twilio.account.sid}")
//    private String accountSid = System.getenv("TWILIO_ACCOUNT_SID");
//
//    @Value("${twilio.auth.token}")
//    private String authToken = System.getenv("TWILIO_AUTH_TOKEN");
//
//    @Value("${twilio.phone.number}")
//    private String phoneNumber= System.getenv("TWILIO_NUMBER");
//
//    public void sendSms(String recipient, String message) {
//        Twilio.init(accountSid, authToken);
//
//        try {
//            Message twilioMessage = Message.creator(
//                            new PhoneNumber(recipient),
//                            new PhoneNumber(phoneNumber),
//                            message)
//                    .create();
//
//            System.out.println("SMS sent successfully! SID: " + twilioMessage.getSid());
//        } catch (Exception e) {
//            System.out.println("Failed to send SMS. Error: " + e.getMessage());
//        }
//    }
//}
