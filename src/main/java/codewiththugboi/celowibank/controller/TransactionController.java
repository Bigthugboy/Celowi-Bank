package codewiththugboi.celowibank.controller;

import codewiththugboi.celowibank.dto.request.TransactionRequest;
import codewiththugboi.celowibank.service.transaction.TransactionServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/bank")
@AllArgsConstructor
public class TransactionController {
    private final TransactionServiceImpl transactionService;
    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<TransactionRequest>> getAccountTransactions(@PathVariable Long accountId) {
        List<TransactionRequest> transactions = transactionService.getAccountTransactions(accountId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionRequest>> getAllTransactions() {
        List<TransactionRequest> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
}
