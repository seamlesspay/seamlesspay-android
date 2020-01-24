package com.seamlesspay.api;

import com.seamlesspay.api.interfaces.PaymentMethodNonceCallback;
import com.seamlesspay.api.models.CardBuilder;
import com.seamlesspay.api.models.CardNonce;
import com.seamlesspay.api.models.PaymentMethodNonce;
import com.seamlesspay.api.exceptions.ErrorWithResponse;
import com.seamlesspay.api.interfaces.SeamlesspayErrorListener;
import com.seamlesspay.api.interfaces.PaymentMethodNonceCreatedListener;

/**
 * Used to tokenize credit or debit cards using a {@link CardBuilder}. For more information see the
 * <a href="https://docs.seamlesspay.com/#tag/PanVault">documentation</a>
 */
public class PanVault {

    /**
     * Create a {@link PaymentMethodNonce}.
     * <p>
     * On completion, returns the {@link PaymentMethodNonce} to
     * {@link PaymentMethodNonceCreatedListener}.
     * <p>
     * If creation fails validation, {@link SeamlesspayErrorListener#onError(Exception)}
     * will be called with the resulting {@link ErrorWithResponse}.
     * <p>
     * If an error not due to validation (server error, network issue, etc.) occurs, {@link
     * SeamlesspayErrorListener#onError(Exception)}
     * will be called with the {@link Exception} that occurred.
     *
     * @param fragment {@link SeamlesspayFragment}
     * @param cardBuilder {@link CardBuilder}
     */
    public static void tokenize(final SeamlesspayFragment fragment, final CardBuilder cardBuilder) {
        TokenizationClient.tokenize(fragment, cardBuilder, new PaymentMethodNonceCallback() {
            @Override
            public void success(PaymentMethodNonce paymentMethodNonce) {
                fragment.postCallback(paymentMethodNonce);
            }

            @Override
            public void failure(Exception exception) {
                fragment.postCallback(exception);
            }
        });
    }
}
