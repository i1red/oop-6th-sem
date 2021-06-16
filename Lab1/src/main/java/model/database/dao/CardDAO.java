package model.database.dao;

import model.entity.Card;
import model.database.dao.mapper.CardMapper;
import model.database.Table;

import java.sql.Connection;

public class CardDAO extends DAO<Card> {
    public CardDAO(Connection connection) {
        super(new CardMapper(), Table.Card.NAME, Table.Card.COLUMNS, connection);
    }
}
