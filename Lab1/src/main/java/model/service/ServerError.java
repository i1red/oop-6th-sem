package model.service;

public class ServerError extends Error {
    ServerError(String errorMessage) {
        super(errorMessage);
    }
    ServerError(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
