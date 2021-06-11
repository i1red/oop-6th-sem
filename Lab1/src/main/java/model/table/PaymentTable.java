package model.table;

import java.util.Arrays;
import java.util.List;

public class PaymentTable {
    public static final String NAME = "payment";

    public static class Column {
        public static final String ID = "id";
        public static final String FROM_CARD_ID = "from_card_id";
        public static final String TO_CARD_ID = "to_card_id";
        public static final String SUM = "sum";
    }

    public static final List<String> COLUMNS = Arrays.asList(Column.ID, Column.FROM_CARD_ID, Column.TO_CARD_ID, Column.SUM);

}
