package com.seamlesspay.api.exceptions;

/**
 * Error thrown when arguments provided to a method are invalid.
 */
public class InvalidArgumentException extends Exception {
    public InvalidArgumentException(String message) {
        super(message);
    }
}
