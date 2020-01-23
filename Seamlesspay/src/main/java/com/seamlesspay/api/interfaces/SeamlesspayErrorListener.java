package com.seamlesspay.api.interfaces;

/**
 * Interface that defines callbacks for errors that occur when processing Seamlesspay requests.
 */
public interface SeamlesspayErrorListener extends SeamlesspayListener {

    /**
     * {@link #onError(Exception)} will be called when there is an exception that cannot be handled,
     * such as a network or request error, or when there are data validation errors.
     *
     * @param error
     */
    void onError(Exception error);
}
