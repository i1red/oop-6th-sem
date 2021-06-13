package model.service;

import model.database.dao.BankAccountDAO;
import model.database.dao.CardDAO;
import model.database.dao.DAO;
import model.database.dao.PaymentDAO;
import model.entity.BankAccount;
import model.entity.Card;
import model.entity.Payment;

import java.sql.SQLException;

public class PaymentService {
    private final DAO<Payment> paymentDAO = new PaymentDAO();
    private final DAO<Card> cardDAO = new CardDAO();
    private final DAO<BankAccount> bankAccountDAO = new BankAccountDAO();

    public Payment createPayment(Payment payment) throws ServerError, IllegalArgumentException {
        Card fromCard;
        try {
            fromCard = cardDAO.get(payment.getFromCardId());
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed database query", e);
        }

        if (fromCard.getBalance() < payment.getSum()) {
            throw new IllegalArgumentException("Not enough money");
        }

        BankAccount bankAccount;
        try {
            bankAccount = bankAccountDAO.get(fromCard.getAccountId());
        } catch (SQLException e) {
            throw new ServerError("Failed database query", e);
        }

        if (bankAccount.isBlocked()) {
            throw new IllegalArgumentException("Card cannot be used because bank account is blocked");
        }

        try {
            return paymentDAO.insert(payment);
        } catch (SQLException e) {
            throw new ServerError("Failed database query", e);
        }
    }
}
