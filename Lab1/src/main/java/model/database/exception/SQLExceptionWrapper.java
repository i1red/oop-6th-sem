package model.database.exception;

import java.sql.SQLException;

public class SQLExceptionWrapper {
    private static final String INTEGRITY_CONSTRAINT_VIOLATION = "23";

    public static void wrapExceptionOnUpdate(SQLException e) throws IntegrityConstraintViolation {
        if (getErrorState(e).equals(INTEGRITY_CONSTRAINT_VIOLATION)) {
            throw new IntegrityConstraintViolation(e.getMessage());
        } else {
            throw new SQLError(e.getMessage());
        }
    }

    public static void wrapExceptionOnQuery(SQLException e) {
        throw new SQLError(e.getMessage());
    }

    private static String getErrorState(SQLException e) {
        //first two chars of SQL State string are common for different DBMS and contain info about error type
        //https://stackoverflow.com/questions/1988570/how-to-catch-a-specific-exception-in-jdbc
        return e.getSQLState().substring(0, 2);
    }
}
