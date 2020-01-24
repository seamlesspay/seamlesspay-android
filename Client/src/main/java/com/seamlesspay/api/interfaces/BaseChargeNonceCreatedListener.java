package com.seamlesspay.api.interfaces;

import com.seamlesspay.api.models.BaseChargeNonce;

/**
 * Interface that defines callbacks to be called when {@link BaseChargeNonce}s are created.
 */

public interface BaseChargeNonceCreatedListener extends SeamlesspayListener {

    /**
     * {@link #onBaseChargeNonceCreated} will be called when a new {@link BaseChargeNonce} has been
     * created.
     *
     * @param baseChargeNonceNonce the {@link BaseChargeNonce}.
     */
    void onBaseChargeNonceCreated(BaseChargeNonce baseChargeNonceNonce);
}
