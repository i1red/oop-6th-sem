package model.service;

import model.Table;
import model.database.dao.BankAccountDAO;
import model.database.dao.CardDAO;
import model.database.dao.DAO;
import model.entity.BankAccount;
import model.entity.Card;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardService {
    private final DAO<Card> cardDAO = new CardDAO();
    private final DAO<BankAccount> bankAccountDAO = new BankAccountDAO();

    public Card createCard(Card card) {
        try {
            return cardDAO.insert(card);
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Card> listUserCards(int userId) {
        try {
            List<Card> userCards = new ArrayList<>();

            List<BankAccount> bankAccounts = bankAccountDAO.filter(Table.BankAccount.Column.USER_ID, userId);
            for (BankAccount bankAccount: bankAccounts) {
                userCards.addAll(cardDAO.filter(Table.Card.Column.ACCOUNT_ID, bankAccount.getId()));
            }

            return userCards;
        } catch (SQLException e) {
            return null;
        }
    }
}
