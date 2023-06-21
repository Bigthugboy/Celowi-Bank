package codewiththugboi.celowibank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepositResponse {
    private String message;
    private LocalDateTime dateTime;
    private String firstName;
    private String lastName;
    private String balance;


}
