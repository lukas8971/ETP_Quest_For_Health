package com.etp.questforhealth.exception;

public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 3015776249239096551L;

    public NotFoundException(String message) {
        super(message);
    }
}
