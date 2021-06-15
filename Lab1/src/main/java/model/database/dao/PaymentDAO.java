package model.database.dao;

import model.entity.Payment;
import model.database.dao.mapper.PaymentMapper;
import model.database.Table;

public class PaymentDAO extends DAO<Payment> {
    public PaymentDAO() {
        super(new PaymentMapper(), Table.Payment.NAME, Table.Payment.COLUMNS);
    }
}
