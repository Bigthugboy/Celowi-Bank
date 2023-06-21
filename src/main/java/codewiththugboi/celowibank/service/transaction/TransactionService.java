package codewiththugboi.celowibank.service.transaction;


import codewiththugboi.celowibank.data.model.User;
import codewiththugboi.celowibank.dto.request.TransactionRequest;


import java.util.List;
import java.util.Optional;

public interface TransactionService {
    List<TransactionRequest> getAccountTransactions(Long userId);
    List<TransactionRequest> getAllTransactions();
    void createTransaction(Optional<User> user, TransactionRequest request);

}
