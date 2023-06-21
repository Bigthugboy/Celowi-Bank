package codewiththugboi.celowibank.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WithdrawalRequest {
    private BigDecimal amount;
    private String accountNumber;
    private String pin;
}
