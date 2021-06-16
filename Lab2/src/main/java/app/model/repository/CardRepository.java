package app.model.repository;

import app.model.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Integer> {
    List<Card> getAllByAccountId(int accountId);
}
