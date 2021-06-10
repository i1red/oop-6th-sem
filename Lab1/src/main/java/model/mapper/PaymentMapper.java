package model.mapper;

import java.util.List;

import model.entity.Payment;
import model.table.PaymentTable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentMapper implements Mapper<Payment> {

    @Override
    public Payment fromResultSet(ResultSet resultSet) throws SQLException {
        return new Payment()
                .setId(resultSet.getInt(PaymentTable.Column.ID))
                .setFromCardId(resultSet.getInt(PaymentTable.Column.FROM_CARD_ID))
                .setToCardId(resultSet.getInt(PaymentTable.Column.TO_CARD_ID))
                .setSum(resultSet.getDouble(PaymentTable.Column.SUM));
    }

    @Override
    public void fillPreparedStatement(Payment payment, PreparedStatement preparedStatement, List<String> columnNames) throws SQLException {
        for (int columnIndex = 0; columnIndex < columnNames.size(); columnIndex++) {
            switch (columnNames.get(columnIndex)) {
                case PaymentTable.Column.ID -> preparedStatement.setInt(columnIndex + 1, payment.getId());
                case PaymentTable.Column.FROM_CARD_ID -> preparedStatement.setInt(columnIndex + 1, payment.getFromCardId());
                case PaymentTable.Column.TO_CARD_ID -> preparedStatement.setInt(columnIndex + 1, payment.getToCardId());
                case PaymentTable.Column.SUM -> preparedStatement.setDouble(columnIndex + 1, payment.getSum());
                default -> throw new IllegalArgumentException(String.format("Column %s does not exist", columnNames.get(columnIndex)));
            }
        }
    }
}
