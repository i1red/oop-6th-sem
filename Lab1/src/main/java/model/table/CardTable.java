package model.table;

import java.util.Arrays;
import java.util.List;

public class CardTable {
    public static final String NAME = "card";

    public static class Column {
        public static final String ID = "id";
        public static final String NUMBER = "number";
        public static final String BALANCE = "balance";
        public static final String ACCOUNT_ID = "account_id";
    }

    public static final List<String> COLUMNS = Arrays.asList(Column.ID, Column.NUMBER, Column.BALANCE, Column.ACCOUNT_ID);

}
