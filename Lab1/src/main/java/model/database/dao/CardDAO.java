package model.database.dao;

import model.entity.Card;
import model.mapper.CardMapper;
import model.Table;

public class CardDAO extends DAO<Card> {
    public CardDAO() {
        super(new CardMapper(), Table.Card.NAME, Table.Card.COLUMNS);
    }
}
