package codewiththugboi.celowibank.service.user;
import codewiththugboi.celowibank.data.model.ConfirmationToken;
import codewiththugboi.celowibank.data.model.RoleType;
import codewiththugboi.celowibank.data.model.TransactionDetails;
import codewiththugboi.celowibank.data.model.User;
import codewiththugboi.celowibank.data.repo.UserRepository;
import codewiththugboi.celowibank.dto.request.*;
import codewiththugboi.celowibank.dto.response.*;
import codewiththugboi.celowibank.exceptions.*;
import codewiththugboi.celowibank.security.config.JwtServices;
import codewiththugboi.celowibank.service.ConfirmationTokenImpl;
import codewiththugboi.celowibank.service.transaction.TransactionServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final JwtServices jwtService;
    private final AuthenticationManager authenticationManager;
    //    private final TwilioSmsService smsService;
    private final TransactionServiceImpl transaction;
private final ConfirmationTokenImpl confirmationTokenImpl;
    private final HttpServletRequest httpRequest;
    private final HttpServletResponse httpResponse;
    public final PasswordEncoder passwordEncoder;


    @Override
    public RegisterResponse register(RegisterRequest request) throws DuplicateEmailException {
        if (findByEmail(request.getEmail())) {
            throw new DuplicateEmailException("ACCOUNT ALREADY REQUEST");
        }

        User user = mapUserRequestToUser(request);
        String accountNumber = generateAccountNumber();
        user.setAccountNumber(accountNumber);
        user.setRoles(RoleType.ROLE_USER);
        saveUser(user);



//        smsService.sendSms(request.getPhoneNumber(), "Welcome to Celowi-Bank, we hope to provide you a good banking expirenece");

        return buildUserResponse(user);
    }

    private boolean findByEmail(String email) {
        Optional<User> user = repository.findByEmail(email);
        return user.isPresent();

    }


    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        String jwtToken = jwtService.generateToken(user);
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

        // Create session
        createSession(httpRequest, httpResponse, user.getEmail());


        return authenticationResponse;
    }

    public void createSession(HttpServletRequest request, HttpServletResponse response, String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String token = jwtService.generateToken(user);
        response.addHeader("Authorization", "Bearer " + token);
        request.getSession().setAttribute("user", user);
    }

    @Override
    @Transactional
    public DepositResponse deposit(TransactionRequest request) throws ErrorException {
        Optional<User> user = repository.findByAccountNumber(request.getAccountNumber());
        if (user.isPresent()) {
            user.get().setBalance(request.getAmount().add(user.get().getBalance()));
            request.setDetails(TransactionDetails.DEPOSIT);
            User savedDeposit = repository.save(user.get());
            transaction.createTransaction(user, request);
            return createDepositResponse(savedDeposit);
        }

        throw new ErrorException("Unable to deposit (\"USER DOES NOT EXIST\");");
    }

    @Override
    @Transactional
    public WithdrawnResponse withdrawn(TransactionRequest request) {
        Optional<User> user = (repository.findByAccountNumber(request.getAccountNumber()));
        if (user.isPresent()) {
            if (passwordEncoder.matches(request.getPin(), user.get().getPin())) {
                if (request.getAmount().compareTo(user.get().getBalance()) < 1) {
                    if (request.getAmount().compareTo(BigDecimal.ONE) > 0) {
                        user.get().setBalance(user.get().getBalance().subtract(request.getAmount()));
                        request.setDetails(TransactionDetails.WITHDRAWN);
                        User savedWithdrawal = repository.save(user.get());
                        transaction.createTransaction(user, request);
                        return CreateWithdrawalResponse(savedWithdrawal);
                    }
                    throw new ErrorException("Cant withdrawn a negative amount");
                }
                throw new InsufficientFundExceptions("Insufficient balance");
            }
            throw new InvalidPinException("INCORRECT PIN");
        }
        throw new ErrorException("USER DOES NOT EXIST");
    }


    @Override
    public pinResponse pin(PinRequest request) {
        Optional<User> user = repository.findByAccountNumber(request.getAccountNumber());
        if (user.isPresent()) {
            String encodePin = passwordEncoder.encode(request.getPin());
            user.get().setPin(encodePin);
            repository.save(user.get());
            pinResponse response = new pinResponse();
            response.setMessage("YOUR NEW PIN IS " + request.getPin());
            return response;
        }
        throw new ErrorException("USER DOES NOT EXIST");
    }

    @Override
    @Transactional
    public TransferResponse transfer(TransactionRequest request) {
        Optional<User> sourceUser = repository.findByAccountNumber(request.getFromAccount());
        Optional<User> destinationUser = repository.findByAccountNumber(request.getToAccount());

        if (sourceUser.isPresent() && destinationUser.isPresent()) {
            User source = sourceUser.get();
            User destination = destinationUser.get();

            if (passwordEncoder.matches(request.getPin(), source.getPin())) {
                BigDecimal transferAmount = request.getAmount();

                if (transferAmount.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal sourceBalance = source.getBalance();
                    if (sourceBalance.compareTo(transferAmount) >= 0) {
                        source.setBalance(sourceBalance.subtract(transferAmount));
                        destination.setBalance(destination.getBalance().add(transferAmount));
                        request.setDetails(TransactionDetails.TRANSFER);

                        User savedSource = repository.save(source);
                        User savedDestination = repository.save(destination);

                        transaction.createTransaction(Optional.of(source), request);
                        transaction.createTransaction(Optional.of(destination), request);

                        return createTransferResponse(savedSource, savedDestination, request);
                    }
                    throw new InsufficientFundExceptions("Insufficient balance for transfer");
                }
                throw new InsufficientFundExceptions("Invalid transfer amount");
            }
            throw new InvalidPinException("Incorrect PIN for source account");
        }
        throw new ErrorException("One or both accounts do not exist");
    }


    @Override
    public BalanceResponse checkBalance(BalanceRequest request) {
        Optional<User> user = repository.findByAccountNumber(request.getAccountNumber());
        if (user.isPresent()) {
            if (passwordEncoder.matches(request.getPin(), user.get().getPin())) {
                BalanceResponse response = new BalanceResponse();
                response.setBalance(user.get().getBalance());
                return response;
            }
            throw new InvalidPinException("INCORRECT PIN");
        }
        throw new InvalidPinException("USER DOES NOT EXIST");
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public DeleteResponse delete(DeleteRequest deleteRequest) {
        Optional<User> user = repository.findByAccountNumber(deleteRequest.getAccountNumber());
        if (passwordEncoder.matches(deleteRequest.getPin(), user.get().getPin())) {
            repository.delete(user.get());
            DeleteResponse response = new DeleteResponse();
            response.setAccountNumber(deleteRequest.getAccountNumber());
            response.setMessage("ACCOUNT SUCCESSFULLY DELETED  " + deleteRequest.getAccountNumber());
            return response;
        }
        throw new InvalidPinException("INCORRECT PIN");
    }

    @Override
    public User reset(ResetRequest request) {
        Optional<User> user = repository.findByEmail(request.getEmail()); {
            if (user.isPresent()) {
                if (Objects.equals(request.getPassword(), request.getCheckPassword())) {
                    String encodedPassword1 = passwordEncoder.encode(request.getPassword());
                    user.get().setPassword(encodedPassword1);

                    String encodedCheckPassword1 = passwordEncoder.encode(request.getCheckPassword());
                    user.get().setCheckPassword(encodedCheckPassword1);


                    return user.get();
                }
                throw new ErrorException("Password does not match");
            }
           throw new UsernameNotFoundException("User not found" + request.getEmail());
        }
    }


        @Override
        public User getUserById (Long userId){
            return repository.findById(userId)
                    .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + userId));
        }

    public boolean enableAppUser(String token) {
        return confirmationTokenImpl.setConfirmedAt(token);
    }


        private User mapUserRequestToUser (RegisterRequest request){
            ModelMapper modelMapper = new ModelMapper();
            User user = modelMapper.map(request, User.class);
            if (Objects.equals(request.getPassword(),request.getCheckPassword())){
                String encodedPassword = passwordEncoder.encode(request.getPassword());
                user.setPassword(encodedPassword);
                String encodedCheckPassword1 = passwordEncoder.encode(request.getCheckPassword());
                user.setCheckPassword(encodedCheckPassword1);
                return user;
            }

           throw new ErrorException("incorrect password");
        }

        private void saveUser (User user){
            repository.save(user);
        }

        private String generateAccountNumber () {
            return String.valueOf(UUID.randomUUID().getLeastSignificantBits()).substring(1, 11);
        }

        private RegisterResponse buildUserResponse (User user){
            String token = String.valueOf(UUID.randomUUID().getLeastSignificantBits()).substring(1,5);
            ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
            confirmationTokenImpl.saveToken(confirmationToken);
            return RegisterResponse.builder()
                    .token(confirmationToken)
                    .accountNumber(user.getAccountNumber())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .pin("0000")
                    .dateCreated(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy, hh:mm a").format(user.getDate()))
                    .message("Account successfully created")

                    .build();
        }


        private DepositResponse createDepositResponse (User user){
            DepositResponse response = new DepositResponse();
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setMessage("SUCCESSFULLY DEPOSITED");
            response.setBalance(String.valueOf(user.getBalance()));
//            response.setDateTime(LocalDateTime.parse(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy, hh:mm a").format(response.getDateTime())));
            return response;
        }

        private WithdrawnResponse CreateWithdrawalResponse (User user){
            WithdrawnResponse withdrawnResponse = new WithdrawnResponse();
            withdrawnResponse.setMessage("SUCCESSFULLY WITHDRAWN");
            withdrawnResponse.setBalance(String.valueOf(user.getBalance()));
//            withdrawnResponse.setDateWithdrawal(LocalDateTime.parse(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyy, hh:mm, a").format(withdrawnResponse.getDateWithdrawal())));
            return withdrawnResponse;
        }
        private TransferResponse createTransferResponse (User savedSource, User savedDestination, TransactionRequest
        request){
            TransferResponse response = new TransferResponse();
            response.setAmountTransfer(request.getAmount());
            response.setBalance(savedSource.getBalance());
            response.setAccountNumber(savedSource.getAccountNumber());
            response.setDestinationAccount(savedDestination.getAccountNumber());
//            response.setDateTransfer(LocalDateTime.parse(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyy, hh:mm, a").format(savedSource.getDate())));

            response.setMessage("YOU HAVE SUCCESSFULLY TRANSFER " + request.getAmount() + " TO " + savedDestination.getAccountNumber());

            return response;
        }


    }




