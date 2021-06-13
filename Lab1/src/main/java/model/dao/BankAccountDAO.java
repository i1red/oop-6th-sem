package model.dao;

import model.entity.BankAccount;
import model.mapper.BankAccountMapper;
import model.Table;

public class BankAccountDAO extends DAO<BankAccount>{
    public BankAccountDAO() {
        super(new BankAccountMapper(), Table.BankAccount.NAME, Table.BankAccount.COLUMNS);
    }
}
