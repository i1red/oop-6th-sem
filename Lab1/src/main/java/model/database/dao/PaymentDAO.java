package model.database.dao;

import model.entity.Payment;
import model.database.dao.mapper.PaymentMapper;
import model.database.Table;

import java.sql.Connection;

public class PaymentDAO extends DAO<Payment> {
    public PaymentDAO(Connection connection) {
        super(new PaymentMapper(), Table.Payment.NAME, Table.Payment.COLUMNS, connection);
    }
}
