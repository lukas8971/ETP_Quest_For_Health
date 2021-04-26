package com.etp.questforhealth.exception;

public class ValidationException extends RuntimeException{
    private static final long serialVersionUID = -5418597125204154L;

    public ValidationException(String message) {
        super(message);
    }
}
