package codewiththugboi.celowibank.dto.request;

import codewiththugboi.celowibank.data.model.AccountType;
import codewiththugboi.celowibank.data.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String checkPassword;
    private String phoneNumber;
    private String username;
    private Gender gender;
    private String nextOfKin;
    private String address;
    private AccountType accountType;
    private int age;
}
