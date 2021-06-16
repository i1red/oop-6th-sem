package model.service;

import model.database.JdbcConnectionPool;
import model.database.Table;
import model.database.dao.BankAccountDAO;
import model.database.dao.CardDAO;
import model.database.dao.DAO;
import model.database.dao.PaymentDAO;
import model.database.exception.IntegrityConstraintViolation;
import model.database.exception.SQLError;
import model.entity.BankAccount;
import model.entity.Card;
import model.entity.Payment;

import java.sql.Connection;
import java.sql.SQLException;


public class PaymentService {

    public Payment createPayment(Payment payment, int payerId) throws IllegalArgumentException {
        try (Connection connection = JdbcConnectionPool.getInstance().getConnection()){
            var cardDAO = new CardDAO(connection);
            var bankAccountDAO = new BankAccountDAO(connection);
            var paymentDAO = new PaymentDAO(connection);

            Card fromCard = cardDAO.get(payment.getFromCardId())
                    .orElseThrow(() -> new IllegalArgumentException("Source card does not exist"));
            Card toCard = cardDAO.get(payment.getToCardId())
                    .orElseThrow(() -> new IllegalArgumentException("Destination card does not exist"));

            if (fromCard.getBalance() < payment.getSum()) {
                throw new IllegalArgumentException("Not enough money");
            }

            BankAccount bankAccount = bankAccountDAO.get(fromCard.getAccountId()).get();

            if (bankAccount.getCustomerId() == payerId) {
                throw new IllegalArgumentException("User does not hold this card");
            }

            if (bankAccount.isBlocked()) {
                throw new IllegalArgumentException("Card cannot be used because bank account is blocked");
            }

            connection.setAutoCommit(false);

            try {
                cardDAO.update(Table.Card.Column.BALANCE, fromCard.setBalance(fromCard.getBalance() - payment.getSum()));
                cardDAO.update(Table.Card.Column.BALANCE, toCard.setBalance(toCard.getBalance() + payment.getSum()));
                Payment insertedPayment = paymentDAO.insert(payment);
                connection.commit();
                return insertedPayment;
            } catch (Throwable e) {
                connection.rollback();
                throw new Error(e.getMessage());
            }
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }
}
