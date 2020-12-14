package com.restraunt.shapshuk.validation;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1981063800538652933L;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }
}
