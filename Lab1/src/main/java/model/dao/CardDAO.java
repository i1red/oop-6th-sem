package model.dao;

import model.entity.Card;
import model.mapper.CardMapper;
import model.table.CardTable;

public class CardDAO extends DAO<Card> {
    public CardDAO() {
        super(new CardMapper(), CardTable.NAME, CardTable.COLUMNS);
    }
}
