package model.service;

import model.database.Table;
import model.database.dao.BankAccountDAO;
import model.database.dao.DAO;
import model.database.dao.exception.IntegrityConstraintViolation;
import model.entity.BankAccount;

import java.util.Arrays;
import java.util.List;

public class BankAccountService {
    DAO<BankAccount> bankAccountDAO = new BankAccountDAO();

    public BankAccount create(BankAccount bankAccount) throws IllegalArgumentException {
        try {
            return bankAccountDAO.insert(bankAccount);
        } catch (IntegrityConstraintViolation e) {
            throw new IllegalArgumentException("Failed to create bank account for user", e);
        }
    }

    public BankAccount bankAccountSetBlocked(int accountId, boolean blocked) {
        return bankAccountSetBlocked(accountId, blocked, null);
    }

    public BankAccount bankAccountSetBlocked(int accountId, boolean blocked, Integer accountUserId) throws IllegalArgumentException{
        BankAccount bankAccount = new BankAccount().setId(accountId).setBlocked(blocked);

        try {
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
        }
    }

    public List<BankAccount> listBankAccounts() {
        return bankAccountDAO.list();
    }

    public List<BankAccount> listUserBankAccounts(int userId) {
        return bankAccountDAO.filter(Table.BankAccount.Column.USER_ID, userId);
    }
}
