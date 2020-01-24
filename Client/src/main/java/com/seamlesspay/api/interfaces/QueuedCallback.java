package com.seamlesspay.api.interfaces;

import com.seamlesspay.api.SeamlesspayFragment;



/**
 * Callback used by {@link SeamlesspayFragment} to queue responses to various
 * async operations.
 */
public interface QueuedCallback {

    /**
     * @return {@code true} if the run method should be called, {@code false} otherwise.
     */
    boolean shouldRun();

    /**
     * Method to execute arbitrary code on main thread.
     */

    void run();
}
