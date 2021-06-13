package model.service;

import model.Table;
import model.database.dao.BankAccountDAO;
import model.database.dao.DAO;
import model.entity.BankAccount;

import java.sql.SQLException;
import java.util.List;

public class BankAccountService {
    DAO<BankAccount> bankAccountDAO = new BankAccountDAO();

    public BankAccount create(BankAccount bankAccount)  {
        try {
            return bankAccountDAO.insert(bankAccount);
        } catch (SQLException e) {
            return null;
        }
    }

    public BankAccount bankAccountSetBlocked(int id, boolean blocked) {
        try {
            return bankAccountDAO.update(Table.BankAccount.Column.IS_BLOCKED, new BankAccount().setId(id).setBlocked(blocked));
        } catch (SQLException e) {
            return null;
        }
    }

    public List<BankAccount> listBankAccounts() {
        try {
            return bankAccountDAO.list();
        } catch (SQLException e) {
            return null;
        }
    }

    public List<BankAccount> listUserBankAccounts(int userId) {
        try {
            return bankAccountDAO.filter(Table.BankAccount.Column.USER_ID, userId);
        } catch (SQLException e) {
            return null;
        }
    }
}
