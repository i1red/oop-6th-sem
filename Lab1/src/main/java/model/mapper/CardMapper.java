package model.mapper;

import java.util.List;

import model.entity.Card;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Table;

public class CardMapper implements Mapper<Card>{

    @Override
    public Card fromResultSet(ResultSet resultSet) throws SQLException {
        return new Card()
                .setId(resultSet.getInt(Table.Card.Column.ID))
                .setNumber(resultSet.getString(Table.Card.Column.NUMBER))
                .setBalance(resultSet.getDouble(Table.Card.Column.BALANCE))
                .setAccountId(resultSet.getInt(Table.Card.Column.ACCOUNT_ID));
    }

    @Override
    public void fillPreparedStatement(Card card, PreparedStatement preparedStatement, List<String> columnNames) throws SQLException {
        for (int columnIndex = 0; columnIndex < columnNames.size(); columnIndex++) {
            switch (columnNames.get(columnIndex)) {
                case Table.Card.Column.ID -> preparedStatement.setInt(columnIndex + 1, card.getId());
                case Table.Card.Column.NUMBER -> preparedStatement.setString(columnIndex + 1, card.getNumber());
                case Table.Card.Column.BALANCE -> preparedStatement.setDouble(columnIndex + 1, card.getBalance());
                case Table.Card.Column.ACCOUNT_ID -> preparedStatement.setInt(columnIndex + 1, card.getAccountId());
                default -> throw new IllegalArgumentException(String.format("Column %s does not exist", columnNames.get(columnIndex)));
            }
        }
    }
}
