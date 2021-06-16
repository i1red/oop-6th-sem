package app.model.repository;

import app.model.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    List<BankAccount> getAllByUserId(int userId);
}
