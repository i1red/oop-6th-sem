package model.table;

import java.util.Arrays;
import java.util.List;

public class UserTable {
    public static final String NAME = "\"user\"";

    public static class Column {
        public static final String ID = "id";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String IS_ADMIN = "is_admin";
    }

    public static final List<String> COLUMNS = Arrays.asList(Column.ID, Column.USERNAME, Column.PASSWORD, Column.IS_ADMIN);
}
