package codewiththugboi.celowibank.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SmsRequest {
    private final  String phoneNumber;

    private final String message;

    public SmsRequest(@JsonProperty("phoneNumber")String phoneNumber, @JsonProperty("message") String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }
    @Override
    public String toString(){
        return "smsRequest{" + "phoneNumber='" + phoneNumber + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
