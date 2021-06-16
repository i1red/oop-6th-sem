package model.database;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Table {
    public static class User {
        public static final String NAME = "\"user\"";

        public static class Column {
            public static final String ID = "id";
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";
            public static final String IS_ADMIN = "is_admin";
        }

        public static final List<String> COLUMNS = Arrays.asList(Column.ID, Column.USERNAME, Column.PASSWORD, Column.IS_ADMIN);
    }

    public static class Payment {
        public static final String NAME = "payment";

        public static class Column {
            public static final String ID = "id";
            public static final String FROM_CARD_ID = "from_card_id";
            public static final String TO_CARD_ID = "to_card_id";
            public static final String SUM = "sum";
        }

        public static final List<String> COLUMNS = Arrays.asList(Column.ID, Column.FROM_CARD_ID, Column.TO_CARD_ID, Column.SUM);
    }

    public static class Card {
        public static final String NAME = "card";

        public static class Column {
            public static final String ID = "id";
            public static final String NUMBER = "number";
            public static final String BALANCE = "balance";
            public static final String ACCOUNT_ID = "account_id";
        }

        public static final List<String> COLUMNS = Arrays.asList(Column.ID, Column.NUMBER, Column.BALANCE, Column.ACCOUNT_ID);
    }

    public static class BankAccount {
        public static final String NAME = "bank_account";

        public static class Column {
            public static final String ID = "id";
            public static final String NUMBER = "number";
            public static final String USER_ID = "user_id";
            public static final String IS_BLOCKED = "is_blocked";
        }

        public static final List<String> COLUMNS = Arrays.asList(Column.ID, Column.NUMBER, Column.USER_ID, Column.IS_BLOCKED);
    }

    public static class RefreshToken {
        public static final String NAME = "refresh_token";

        public static class Column {
            public static final String VALUE = "value";
        }

        public static final List<String> COLUMNS = Collections.singletonList(Column.VALUE);
    }
}
