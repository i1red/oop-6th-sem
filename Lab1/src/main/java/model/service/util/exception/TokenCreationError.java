package model.service.util.exception;

public class TokenCreationError extends Error {
    public TokenCreationError(String errorMessage) {
        super(errorMessage);
    }
    public TokenCreationError(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
