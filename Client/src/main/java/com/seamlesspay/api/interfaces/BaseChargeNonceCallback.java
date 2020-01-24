package com.seamlesspay.api.interfaces;

import com.seamlesspay.api.models.BaseChargeNonce;

/**
 * Communicates {@link BaseChargeNonce} from a HTTP request on the main thread.
 * One and only one method will be invoked in response to a request.
 */
public interface BaseChargeNonceCallback {

    /**
     * @param baseChargedNonce parsed {@link BaseChargeNonce} from the HTTP request.
     */
    void success(BaseChargeNonce baseChargedNonce);

    /**
     * @param exception error that caused the request to fail.
     */
    void failure(Exception exception);

}
