package model.dao;

import model.entity.BankAccount;
import model.mapper.BankAccountMapper;
import model.table.BankAccountTable;

public class BankAccountDAO extends DAO<BankAccount>{
    public BankAccountDAO() {
        super(new BankAccountMapper(), BankAccountTable.NAME, BankAccountTable.COLUMNS);
    }
}
