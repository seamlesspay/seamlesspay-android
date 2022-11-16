/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api;

import static com.seamlesspay.api.models.PaymentMethodToken.parsePaymentMethodToken;

import com.seamlesspay.api.exceptions.ErrorWithResponse;
import com.seamlesspay.api.interfaces.HttpResponseCallback;
import com.seamlesspay.api.interfaces.PaymentMethodTokenCallback;
import com.seamlesspay.api.interfaces.SeamlesspayErrorListener;
import com.seamlesspay.api.internal.AppHelper;
import com.seamlesspay.api.models.PaymentMethodBuilder;
import com.seamlesspay.api.models.PaymentMethodToken;
import org.json.JSONException;
import org.json.JSONObject;

import io.sentry.Sentry;

class TokenizationClient {
  static final String PAYMENT_METHOD_ENDPOINT = "tokens";

  /**
   * Create a {@link PaymentMethodToken} in the Seamlesspay Gateway.
   *
   * On completion, returns the {@link PaymentMethodToken} to {@link PaymentMethodTokenCallback}.
   *
   * If creation fails validation, {@link SeamlesspayErrorListener#onError(Exception)}
   * will be called with the resulting {@link ErrorWithResponse}.
   *
   * If an error not due to validation (server error, network issue, etc.) occurs, {@link
   * SeamlesspayErrorListener#onError(Exception)} (Throwable)}
   * will be called with the {@link Exception} that occurred.
   *
   * @param paymentMethodBuilder {@link PaymentMethodBuilder} for the {@link PaymentMethodToken}
   * to be created.
   */
  static void tokenize(
    final SeamlesspayFragment fragment,
    final PaymentMethodBuilder paymentMethodBuilder,
    final PaymentMethodTokenCallback callback
  ) {
    tokenizeRest(fragment, paymentMethodBuilder, callback);
  }

  private static void tokenizeRest(
    final SeamlesspayFragment fragment,
    final PaymentMethodBuilder paymentMethodBuilder,
    final PaymentMethodTokenCallback callback
  ) {
    String data = paymentMethodBuilder.build();

    try {
      JSONObject dataJson = new JSONObject(data);

      dataJson.put(
        "deviceFingerprint",
        AppHelper.getDeviceFingerprint(fragment.getContext())
      );

      data = dataJson.toString();
    } catch (JSONException ignored) {}
      try {
          throw new Exception("Test it is");
      } catch (Exception e) {
          Sentry.captureException(e);
      }
      fragment
      .getPanVaulHttpClient()
      .post(
        TokenizationClient.PAYMENT_METHOD_ENDPOINT,
        data,
        new HttpResponseCallback() {

          @Override
          public void success(String responseBody) {
            try {
              callback.success(
                parsePaymentMethodToken(
                  responseBody,
                  paymentMethodBuilder.getResponsePaymentMethodType()
                )
              );
            } catch (JSONException e) {
              callback.failure(e);
            }
          }

          @Override
          public void failure(Exception exception) {
            callback.failure(exception);
          }
        }
      );
  }
}
