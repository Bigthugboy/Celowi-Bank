package codewiththugboi.celowibank.dto.response;

import codewiththugboi.celowibank.data.model.ConfirmationToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    private String message;
    private String username;
    private String email;
    private String dateCreated;
    private String accountNumber;
    private String pin;
    private ConfirmationToken token;
}
