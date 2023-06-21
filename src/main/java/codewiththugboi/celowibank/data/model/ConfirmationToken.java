package codewiththugboi.celowibank.data.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor


@AllArgsConstructor
public class ConfirmationToken {

    private Long id;
    private String token;

    private LocalDateTime createdAt;

    private LocalDateTime expirdAt;
    //    @Column(nullable = false)
    private LocalDateTime confirmedAt;

    private User user;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expirdAt,   User user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expirdAt = expirdAt;

        this.user = user;
    }
}

