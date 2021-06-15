package model.database.dao.mapper;

import model.entity.BankAccount;
import model.database.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BankAccountMapper implements Mapper<BankAccount> {

    @Override
    public BankAccount fromResultSet(ResultSet resultSet) throws SQLException {
        return new BankAccount()
                .setId(resultSet.getInt(Table.BankAccount.Column.ID))
                .setNumber(resultSet.getString(Table.BankAccount.Column.NUMBER))
                .setCustomerId(resultSet.getInt(Table.BankAccount.Column.USER_ID))
                .setBlocked(resultSet.getBoolean(Table.BankAccount.Column.IS_BLOCKED));
    }

    @Override
    public void fillPreparedStatement(BankAccount bankAccount, PreparedStatement preparedStatement, List<String> columnNames) throws SQLException {
        for (int columnIndex = 0; columnIndex < columnNames.size(); columnIndex++) {
            switch (columnNames.get(columnIndex)) {
                case Table.BankAccount.Column.ID -> preparedStatement.setInt(columnIndex + 1, bankAccount.getId());
                case Table.BankAccount.Column.NUMBER -> preparedStatement.setString(columnIndex + 1, bankAccount.getNumber());
                case Table.BankAccount.Column.USER_ID -> preparedStatement.setInt(columnIndex + 1, bankAccount.getCustomerId());
                case Table.BankAccount.Column.IS_BLOCKED -> preparedStatement.setBoolean(columnIndex + 1, bankAccount.isBlocked());
                default -> throw new IllegalArgumentException(String.format("Column %s does not exist", columnNames.get(columnIndex)));
            }
        }
    }
}
