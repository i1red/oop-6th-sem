package model.service.util.exception;

public class TokenValidationException extends Exception{
    public TokenValidationException(String errorMessage) {
        super(errorMessage);
    }
    public TokenValidationException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
