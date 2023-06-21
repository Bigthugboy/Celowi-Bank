package codewiththugboi.celowibank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WithdrawnResponse {
    private String message;
    private LocalDateTime dateWithdrawal;
    private String balance;
}
