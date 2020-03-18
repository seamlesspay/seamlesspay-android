/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api;

import com.seamlesspay.api.exceptions.ErrorWithResponse;
import com.seamlesspay.api.interfaces.PaymentMethodTokenCallback;
import com.seamlesspay.api.interfaces.PaymentMethodTokenCreatedListener;
import com.seamlesspay.api.interfaces.SeamlesspayErrorListener;
import com.seamlesspay.api.models.CardBuilder;
import com.seamlesspay.api.models.PaymentMethodToken;

/**
 * Used to tokenize credit or debit cards using a {@link CardBuilder}.
 * For more information see the
 * <a href="https://docs.seamlesspay.com/#tag/PanVault">documentation</a>
 */
public class PanVault {

  /**
   * Create a {@link PaymentMethodToken}.
   *
   * On completion, returns the {@link PaymentMethodToken} to
   * {@link PaymentMethodTokenCreatedListener}.
   *
   * If creation fails validation,
   * {@link SeamlesspayErrorListener#onError(Exception)}
   * will be called with the resulting {@link ErrorWithResponse}.
   *
   * If an error not due to validation (server error, network issue, etc.)
   * occurs, {@link SeamlesspayErrorListener#onError(Exception)}
   * will be called with the {@link Exception} that occurred.
   *
   * @param fragment {@link SeamlesspayFragment}
   * @param cardBuilder {@link CardBuilder}
   */
  public static void tokenize(
    final SeamlesspayFragment fragment,
    final CardBuilder cardBuilder
  ) {
    TokenizationClient.tokenize(
      fragment,
      cardBuilder,
      new PaymentMethodTokenCallback() {

        @Override
        public void success(PaymentMethodToken paymentMethodToken) {
          fragment.postCallback(paymentMethodToken);
        }

        @Override
        public void failure(Exception exception) {
          fragment.postCallback(exception);
        }
      }
    );
  }
}
