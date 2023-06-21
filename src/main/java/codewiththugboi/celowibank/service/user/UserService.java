package codewiththugboi.celowibank.service.user;

import codewiththugboi.celowibank.data.model.User;
import codewiththugboi.celowibank.dto.request.*;
import codewiththugboi.celowibank.dto.response.*;

import java.util.List;

public interface UserService {
    RegisterResponse register (RegisterRequest request);
    AuthenticationResponse login(AuthenticationRequest request);

    /* Transaction methods */
    DepositResponse deposit (TransactionRequest request);
    WithdrawnResponse withdrawn (TransactionRequest request);
    TransferResponse transfer(TransactionRequest request);

    /* User Account other methods */
    pinResponse pin(PinRequest request);
    BalanceResponse checkBalance(BalanceRequest request);
    DeleteResponse delete(DeleteRequest deleteRequest);
    User reset(ResetRequest request);

    /*Get User Method */
    User getUserById(Long userId);
    List<User> findAll();


}
