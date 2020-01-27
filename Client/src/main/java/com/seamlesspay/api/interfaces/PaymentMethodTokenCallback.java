package com.seamlesspay.api.interfaces;

import com.seamlesspay.api.models.PaymentMethodToken;

/**
 * Communicates {@link PaymentMethodToken} from a HTTP request on the main thread.
 * One and only one method will be invoked in response to a request.
 */
public interface PaymentMethodTokenCallback {

    /**
     * @param paymentMethodToken parsed {@link PaymentMethodToken} from the HTTP request.
     */
    void success(PaymentMethodToken paymentMethodToken);

    /**
     * @param exception error that caused the request to fail.
     */
    void failure(Exception exception);
}
