package codewiththugboi.celowibank.data.repo;

import codewiththugboi.celowibank.data.model.Transaction;
import codewiththugboi.celowibank.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {


    User getUserById(Long userId);
}
