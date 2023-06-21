package codewiththugboi.celowibank.dto.request;

import codewiththugboi.celowibank.data.model.TransactionDetails;
import codewiththugboi.celowibank.data.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionRequest {

    private Long id;
    private BigDecimal amount;
    private LocalDateTime dateTime;
    private String accountNumber;
    private User user;
    private String email;
    private TransactionDetails details;
    private BigDecimal balance;
    private String pin;
    private String fromAccount;
    private String toAccount;

}
