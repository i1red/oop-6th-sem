package model.database.dao.exception;

public class IntegrityConstraintViolation extends Exception{
    public IntegrityConstraintViolation(String message) {
        super(message);
    }

    public IntegrityConstraintViolation(String message, Throwable err) {
        super(message, err);
    }
}
