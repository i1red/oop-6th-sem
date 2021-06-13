package model.service.sign;

public class TokenCreationException extends Exception {
    public TokenCreationException(String errorMessage) {
        super(errorMessage);
    }
    public TokenCreationException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
