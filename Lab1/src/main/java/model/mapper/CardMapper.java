package model.mapper;

import java.util.List;

import model.entity.Card;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.table.CardTable;

public class CardMapper implements Mapper<Card>{

    @Override
    public Card fromResultSet(ResultSet resultSet) throws SQLException {
        return new Card()
                .setId(resultSet.getInt(CardTable.Column.ID))
                .setNumber(resultSet.getString(CardTable.Column.NUMBER))
                .setBalance(resultSet.getDouble(CardTable.Column.BALANCE))
                .setAccountId(resultSet.getInt(CardTable.Column.ACCOUNT_ID));
    }

    @Override
    public void fillPreparedStatement(Card card, PreparedStatement preparedStatement, List<String> columnNames) throws SQLException {
        for (int columnIndex = 0; columnIndex < columnNames.size(); columnIndex++) {
            switch (columnNames.get(columnIndex)) {
                case CardTable.Column.ID -> preparedStatement.setInt(columnIndex + 1, card.getId());
                case CardTable.Column.NUMBER -> preparedStatement.setString(columnIndex + 1, card.getNumber());
                case CardTable.Column.BALANCE -> preparedStatement.setDouble(columnIndex + 1, card.getBalance());
                case CardTable.Column.ACCOUNT_ID -> preparedStatement.setInt(columnIndex + 1, card.getAccountId());
                default -> throw new IllegalArgumentException(String.format("Column %s does not exist", columnNames.get(columnIndex)));
            }
        }
    }
}
