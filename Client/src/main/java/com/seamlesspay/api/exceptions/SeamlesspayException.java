package com.seamlesspay.api.exceptions;

import java.io.IOException;

/**
 * Parent class for exceptions encountered when using the SDK.
 */
public class SeamlesspayException extends IOException {

    public SeamlesspayException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeamlesspayException(String message) {
        super(message);
    }

    public SeamlesspayException() {
        super();
    }
}
