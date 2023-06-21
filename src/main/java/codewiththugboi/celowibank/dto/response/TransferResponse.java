package codewiththugboi.celowibank.dto.response;


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
public class TransferResponse {
    private BigDecimal amountTransfer;
    private BigDecimal balance;
    private String accountNumber;
    private String message;
    private Long transactionId;
    private LocalDateTime dateTransfer;
    private String destinationAccount;
}
