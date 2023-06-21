package codewiththugboi.celowibank.data.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Getter
@Setter
public class Transaction {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private TransactionDetails transactionDetails;
    private String accountNumber;
}