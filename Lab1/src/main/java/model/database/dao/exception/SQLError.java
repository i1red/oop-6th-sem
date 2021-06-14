package model.database.dao.exception;

public class SQLError extends Error {
    public SQLError(String message) {
        super(message);
    }
    public SQLError(String message, Throwable err) {
        super(message, err);
    }
}
