package model.database.dao.mapper;

import java.util.List;

import model.entity.Payment;
import model.database.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentMapper implements Mapper<Payment> {

    @Override
    public Payment fromResultSet(ResultSet resultSet) throws SQLException {
        return new Payment()
                .setId(resultSet.getInt(Table.Payment.Column.ID))
                .setFromCardId(resultSet.getInt(Table.Payment.Column.FROM_CARD_ID))
                .setToCardId(resultSet.getInt(Table.Payment.Column.TO_CARD_ID))
                .setSum(resultSet.getDouble(Table.Payment.Column.SUM));
    }

    @Override
    public void fillPreparedStatement(Payment payment, PreparedStatement preparedStatement, List<String> columnNames) throws SQLException {
        for (int columnIndex = 0; columnIndex < columnNames.size(); columnIndex++) {
            switch (columnNames.get(columnIndex)) {
                case Table.Payment.Column.ID -> preparedStatement.setInt(columnIndex + 1, payment.getId());
                case Table.Payment.Column.FROM_CARD_ID -> preparedStatement.setInt(columnIndex + 1, payment.getFromCardId());
                case Table.Payment.Column.TO_CARD_ID -> preparedStatement.setInt(columnIndex + 1, payment.getToCardId());
                case Table.Payment.Column.SUM -> preparedStatement.setDouble(columnIndex + 1, payment.getSum());
                default -> throw new IllegalArgumentException(String.format("Column %s does not exist", columnNames.get(columnIndex)));
            }
        }
    }
}
