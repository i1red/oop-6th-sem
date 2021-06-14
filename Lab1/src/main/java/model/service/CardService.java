package model.service;

import model.Table;
import model.database.dao.BankAccountDAO;
import model.database.dao.CardDAO;
import model.database.dao.DAO;
import model.database.dao.exception.IntegrityConstraintViolation;
import model.entity.BankAccount;
import model.entity.Card;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardService {
    private final DAO<Card> cardDAO = new CardDAO();
    private final DAO<BankAccount> bankAccountDAO = new BankAccountDAO();

    public Card createCard(Card card) throws IllegalArgumentException {
        try {
            return cardDAO.insert(card);
        } catch (IntegrityConstraintViolation e) {
            throw new IllegalArgumentException("Failed to create card for bank account", e);
        }
    }

    public List<Card> listUserCards(int userId) {
        List<Card> userCards = new ArrayList<>();

        List<BankAccount> bankAccounts = bankAccountDAO.filter(Table.BankAccount.Column.USER_ID, userId);
        for (BankAccount bankAccount: bankAccounts) {
            userCards.addAll(cardDAO.filter(Table.Card.Column.ACCOUNT_ID, bankAccount.getId()));
        }

        return userCards;
    }
}
