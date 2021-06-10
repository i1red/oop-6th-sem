package model.mapper;

import model.entity.BankAccount;
import model.table.BankAccountTable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BankAccountMapper implements Mapper<BankAccount> {

    @Override
    public BankAccount fromResultSet(ResultSet resultSet) throws SQLException {
        return new BankAccount()
                .setId(resultSet.getInt(BankAccountTable.Column.ID))
                .setNumber(resultSet.getString(BankAccountTable.Column.NUMBER))
                .setCustomerId(resultSet.getInt(BankAccountTable.Column.CUSTOMER_ID))
                .setBlocked(resultSet.getBoolean(BankAccountTable.Column.IS_BLOCKED));
    }

    @Override
    public void fillPreparedStatement(BankAccount bankAccount, PreparedStatement preparedStatement, List<String> columnNames) throws SQLException {
        for (int columnIndex = 0; columnIndex < columnNames.size(); columnIndex++) {
            switch (columnNames.get(columnIndex)) {
                case BankAccountTable.Column.ID -> preparedStatement.setInt(columnIndex + 1, bankAccount.getId());
                case BankAccountTable.Column.NUMBER -> preparedStatement.setString(columnIndex + 1, bankAccount.getNumber());
                case BankAccountTable.Column.CUSTOMER_ID -> preparedStatement.setInt(columnIndex + 1, bankAccount.getCustomerId());
                case BankAccountTable.Column.IS_BLOCKED -> preparedStatement.setBoolean(columnIndex + 1, bankAccount.isBlocked());
                default -> throw new IllegalArgumentException(String.format("Column %s does not exist", columnNames.get(columnIndex)));
            }
        }
    }
}
