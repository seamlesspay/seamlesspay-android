package com.seamlesspay.api.interfaces;

import com.seamlesspay.api.models.BaseChargeToken;

/**
 * Interface that defines callbacks to be called when {@link BaseChargeToken}s are created.
 */

public interface BaseChargeTokenCreatedListener extends SeamlesspayListener {

    /**
     * {@link #onBaseChargeTokenCreated} will be called when a new {@link BaseChargeToken} has been
     * created.
     *
     * @param baseChargeToken the {@link BaseChargeToken}.
     */
    void onBaseChargeTokenCreated(BaseChargeToken baseChargeToken);
}
