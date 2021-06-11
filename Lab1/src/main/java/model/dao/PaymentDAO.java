package model.dao;

import model.entity.Payment;
import model.mapper.PaymentMapper;
import model.table.PaymentTable;

public class PaymentDAO extends DAO<Payment> {
    public PaymentDAO() {
        super(new PaymentMapper(), PaymentTable.NAME, PaymentTable.COLUMNS);
    }
}
