package com.example.UserManagementService.exception;

public class MaximumActiveSessionsException extends RuntimeException{
    public MaximumActiveSessionsException() {
    }

    public MaximumActiveSessionsException(String message) {
        super(message);
    }

    public MaximumActiveSessionsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MaximumActiveSessionsException(Throwable cause) {
        super(cause);
    }

    public MaximumActiveSessionsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
