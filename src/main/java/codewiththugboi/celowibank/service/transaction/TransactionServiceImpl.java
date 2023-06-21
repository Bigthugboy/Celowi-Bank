package codewiththugboi.celowibank.service.transaction;

import codewiththugboi.celowibank.data.model.Transaction;

import codewiththugboi.celowibank.data.model.User;
import codewiththugboi.celowibank.data.repo.TransactionRepository;
import codewiththugboi.celowibank.dto.request.TransactionRequest;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService{
    private final TransactionRepository transactionRepository;


    @Override
    public List<TransactionRequest> getAccountTransactions(Long userId) {
        User user = transactionRepository.getUserById(userId);
        List<Transaction> transactions = user.getTransactions();

        return transactions.stream()
                .map(this::mapToTransactionRequest)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionRequest> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();

        return transactions.stream()
                .map(this::mapToTransactionRequest)
                .collect(Collectors.toList());
    }

    @Override
    public void createTransaction(Optional<User> user, TransactionRequest request) {

                Transaction transaction = new Transaction();
                transaction.setUser(user.get());
                transaction.setAccountNumber(request.getAccountNumber());
                transaction.setAmount(request.getAmount());
                transaction.setEmail(user.get().getEmail());
                transaction.setBalance(user.get().getBalance());
//                transaction.setTimestamp(LocalDateTime.parse(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy, hh:mm a").format(request.getDateTime())));
                transaction.setTransactionDetails(request.getDetails());
                transactionRepository.save(transaction);
            }






    private TransactionRequest mapToTransactionRequest(Transaction transaction) {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setId(transaction.getId());
        transactionRequest.setAmount(transaction.getAmount());
        transactionRequest.setDateTime(transaction.getTimestamp());

        User user = new User();
        user.setId(transaction.getUser().getId());
        user.setAccountNumber(transaction.getUser().getAccountNumber());

        transactionRequest.setId(user.getId());
        transactionRequest.setAccountNumber(user.getAccountNumber());

        return transactionRequest;
    }
}
