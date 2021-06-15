package model.database.dao;

import model.entity.BankAccount;
import model.database.dao.mapper.BankAccountMapper;
import model.database.Table;

import java.sql.Connection;

public class BankAccountDAO extends DAO<BankAccount>{
    public BankAccountDAO(Connection connection) {
        super(new BankAccountMapper(), Table.BankAccount.NAME, Table.BankAccount.COLUMNS, connection);
    }
}
