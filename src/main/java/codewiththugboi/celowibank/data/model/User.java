package codewiththugboi.celowibank.data.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class User implements UserDetails {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;


    private String address;
    //    this is to set the mail to contain email properties
    @Email
    @NotEmpty
    @Column(unique = true)
    @Pattern(regexp = "([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})")
    private String email;

    private String phoneNumber;
    private BigDecimal balance = BigDecimal.valueOf(0);

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private int age;

    private String nextOfKin;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;



    private Boolean locked = false;

    private Boolean enabled = true;

    private Boolean isSuspended = false;

    private LocalDateTime date = LocalDateTime.now();


    @Size(min = 8)
    private String password;


    private String username;
    private String accountNumber;

    private LocalDateTime confirmAt;

    private String pin;


    @Size(min = 8)

    private String checkPassword;
    private RoleType roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new java.util.ArrayList<>();

    public User(String generateTokin, LocalDateTime now, LocalDateTime plusMinutes) {

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(roles.toString()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }




}

