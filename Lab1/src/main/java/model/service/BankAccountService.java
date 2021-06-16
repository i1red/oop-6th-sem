package model.service;

import model.database.JdbcConnectionPool;
import model.database.Table;
import model.database.dao.BankAccountDAO;
import model.database.exception.IntegrityConstraintViolation;
import model.database.exception.SQLError;
import model.entity.BankAccount;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class BankAccountService {
    public BankAccount create(BankAccount bankAccount) throws IllegalArgumentException {
        try (Connection connection = JdbcConnectionPool.getInstance().getConnection()){
            return new BankAccountDAO(connection).insert(bankAccount);
        } catch (IntegrityConstraintViolation e) {
            throw new IllegalArgumentException("Failed to create bank account for user", e);
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public BankAccount bankAccountSetBlocked(int accountId, boolean blocked) {
        return bankAccountSetBlocked(accountId, blocked, null);
    }

    public BankAccount bankAccountSetBlocked(int accountId, boolean blocked, Integer accountUserId) throws IllegalArgumentException{
        BankAccount bankAccount = new BankAccount().setId(accountId).setBlocked(blocked);

        try (Connection connection = JdbcConnectionPool.getInstance().getConnection()){
            BankAccountDAO bankAccountDAO = new BankAccountDAO(connection);
            if (accountUserId != null) {
                return bankAccountDAO.update(
                        Table.BankAccount.Column.IS_BLOCKED,
                        bankAccount.setCustomerId(accountUserId),
                        Arrays.asList(Table.BankAccount.Column.ID, Table.BankAccount.Column.USER_ID)
                );
            }
            return bankAccountDAO.update(Table.BankAccount.Column.IS_BLOCKED, bankAccount);
        } catch (IntegrityConstraintViolation e) {
            throw new ServerError("Failed to update bank account");
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public List<BankAccount> listBankAccounts() {
        try (Connection connection = JdbcConnectionPool.getInstance().getConnection()){
            return new BankAccountDAO(connection).list();
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public List<BankAccount> listUserBankAccounts(int userId) {
        try (Connection connection = JdbcConnectionPool.getInstance().getConnection()){
            return new BankAccountDAO(connection).filter(Table.BankAccount.Column.USER_ID, userId);
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }
}
