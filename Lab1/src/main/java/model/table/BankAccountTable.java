package model.table;

import java.util.Arrays;
import java.util.List;

public class BankAccountTable {
    public static final String NAME = "bank_account";

    public static class Column {
        public static final String ID = "id";
        public static final String NUMBER = "number";
        public static final String CUSTOMER_ID = "customer_id";
        public static final String IS_BLOCKED = "is_blocked";
    }

    public static final List<String> COLUMNS = Arrays.asList(Column.ID, Column.NUMBER, Column.CUSTOMER_ID, Column.IS_BLOCKED);

}
