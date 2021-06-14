package model.service;

import model.database.dao.BankAccountDAO;
import model.database.dao.CardDAO;
import model.database.dao.DAO;
import model.database.dao.PaymentDAO;
import model.database.dao.exception.IntegrityConstraintViolation;
import model.entity.BankAccount;
import model.entity.Card;
import model.entity.Payment;

import java.util.Optional;


public class PaymentService {
    private final DAO<Payment> paymentDAO = new PaymentDAO();
    private final DAO<Card> cardDAO = new CardDAO();
    private final DAO<BankAccount> bankAccountDAO = new BankAccountDAO();

    public Payment createPayment(Payment payment) throws IllegalArgumentException {
        Card fromCard = cardDAO.get(payment.getFromCardId())
                .orElseThrow(() -> new IllegalArgumentException("No such card"));

        if (fromCard.getBalance() < payment.getSum()) {
            throw new IllegalArgumentException("Not enough money");
        }

        BankAccount bankAccount = bankAccountDAO.get(fromCard.getAccountId()).get();

        if (bankAccount.isBlocked()) {
            throw new IllegalArgumentException("Card cannot be used because bank account is blocked");
        }

        try {
            return paymentDAO.insert(payment);
        } catch (IntegrityConstraintViolation e) {
            throw new IllegalArgumentException("Failed to create payment between cards", e);
        }
    }
}
