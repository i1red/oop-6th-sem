package model.dao;

import model.entity.Payment;
import model.mapper.PaymentMapper;
import model.Table;

public class PaymentDAO extends DAO<Payment> {
    public PaymentDAO() {
        super(new PaymentMapper(), Table.Payment.NAME, Table.Payment.COLUMNS);
    }
}
