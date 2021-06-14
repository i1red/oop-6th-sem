package model.service;

import model.Table;
import model.database.dao.BankAccountDAO;
import model.database.dao.DAO;
import model.database.dao.exception.IntegrityConstraintViolation;
import model.entity.BankAccount;

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

    public BankAccount bankAccountSetBlocked(int id, boolean blocked) {
        try {
            return bankAccountDAO.update(Table.BankAccount.Column.IS_BLOCKED, new BankAccount().setId(id).setBlocked(blocked));
        } catch (IntegrityConstraintViolation e) {
            throw new IllegalArgumentException("Failed to update bank account", e);
        }
    }

    public List<BankAccount> listBankAccounts() {
        return bankAccountDAO.list();
    }

    public List<BankAccount> listUserBankAccounts(int userId) {
        return bankAccountDAO.filter(Table.BankAccount.Column.USER_ID, userId);
    }
}