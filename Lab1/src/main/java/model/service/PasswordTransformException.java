package model.service;

public class PasswordTransformException extends Exception {
    public PasswordTransformException(String errorMessage) {
        super(errorMessage);
    }
    public PasswordTransformException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
