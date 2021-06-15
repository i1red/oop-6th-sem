package model.service;

import model.database.Table;
import model.database.dao.BankAccountDAO;
import model.database.dao.CardDAO;
import model.database.dao.DAO;
import model.database.dao.exception.IntegrityConstraintViolation;
import model.entity.BankAccount;
import model.entity.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardService {
    private final DAO<Card> cardDAO = new CardDAO();
    private final DAO<BankAccount> bankAccountDAO = new BankAccountDAO();

    public Card createCard(Card card, Integer cardUserId) throws IllegalArgumentException {
        if (cardUserId != null) {
            Optional<BankAccount> bankAccount = bankAccountDAO.get(card.getAccountId());
            if (bankAccount.isPresent() && bankAccount.get().getCustomerId() != cardUserId) {
                throw new IllegalArgumentException("User does not hold this account");
            }
        }

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
