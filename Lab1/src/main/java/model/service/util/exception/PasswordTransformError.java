package model.service.util.exception;

public class PasswordTransformError extends Error {
    public PasswordTransformError(String errorMessage) {
        super(errorMessage);
    }
    public PasswordTransformError(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
