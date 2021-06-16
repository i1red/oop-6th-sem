package model.service;

import model.database.JdbcConnectionPool;
import model.database.Table;
import model.database.dao.BankAccountDAO;
import model.database.dao.CardDAO;
import model.database.exception.IntegrityConstraintViolation;
import model.database.exception.SQLError;
import model.entity.BankAccount;
import model.entity.Card;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardService {
    public Card createCard(Card card, Integer cardUserId) throws IllegalArgumentException {
        try (Connection connection = JdbcConnectionPool.getInstance().getConnection()){

            if (cardUserId != null) {
                Optional<BankAccount> bankAccount = new BankAccountDAO(connection).get(card.getAccountId());
                if (bankAccount.isPresent() && bankAccount.get().getCustomerId() != cardUserId) {
                    throw new IllegalArgumentException("User does not hold this account");
                }
            }

            return new CardDAO(connection).insert(card);
        } catch (IntegrityConstraintViolation e) {
            throw new IllegalArgumentException("Failed to create card for bank account", e);
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public List<Card> listUserCards(int userId) {
        List<Card> userCards = new ArrayList<>();

        try (Connection connection = JdbcConnectionPool.getInstance().getConnection()){
            List<BankAccount> bankAccounts = new BankAccountDAO(connection).filter(Table.BankAccount.Column.USER_ID, userId);

            var cardDAO = new CardDAO(connection);
            for (BankAccount bankAccount: bankAccounts) {
                userCards.addAll(cardDAO.filter(Table.Card.Column.ACCOUNT_ID, bankAccount.getId()));
            }
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }

        return userCards;
    }
}
