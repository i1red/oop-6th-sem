package model.table;

public class PaymentTable {
    public static final String NAME = "payment";

    public static class Column {
        public static final String ID = "id";
        public static final String FROM_CARD_ID = "from_card_id";
        public static final String TO_CARD_ID = "to_card_id";
        public static final String SUM = "sum";
    }
}
