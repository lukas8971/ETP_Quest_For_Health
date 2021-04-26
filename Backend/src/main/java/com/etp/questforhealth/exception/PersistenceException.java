package com.etp.questforhealth.exception;

public class PersistenceException extends RuntimeException {

    private static final long serialVersionUID = -654684025423238439L;

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}