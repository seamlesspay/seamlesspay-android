/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api;

import com.seamlesspay.api.interfaces.BaseAdjustCallback;
import com.seamlesspay.api.interfaces.BaseChargeTokenCallback;
import com.seamlesspay.api.interfaces.BaseVoidCallback;
import com.seamlesspay.api.interfaces.HttpResponseCallback;
import com.seamlesspay.api.interfaces.VerifyTokenCallback;
import com.seamlesspay.api.internal.AppHelper;
import com.seamlesspay.api.models.BaseChargeToken;
import com.seamlesspay.api.models.CardChargeBulder;
import com.seamlesspay.api.models.CardVerifyBuilder;
import com.seamlesspay.api.models.AdjustChargeBuilder;
import org.json.JSONException;
import org.json.JSONObject;

class ChargeClient {
  static final String CHARGE_ENDPOINT = "charges";

  static void create(
    final SeamlesspayFragment fragment,
    final CardChargeBulder cardChargeBulder,
    final BaseChargeTokenCallback callback
  ) {
    createRest(fragment, cardChargeBulder, callback);
  }

  static void delete(
      final SeamlesspayFragment fragment,
      final String transactionId,
      final BaseVoidCallback callback
  ) {
    createRest(fragment, transactionId, callback);
  }

  static void adjust(
      final SeamlesspayFragment fragment,
      final String transactionId,
      final AdjustChargeBuilder adjustChargeBuilder,
      final BaseAdjustCallback callback
  ) {
    createRest(fragment, adjustChargeBuilder, transactionId, callback);
  }

  static void verify(
      final SeamlesspayFragment fragment,
      final CardVerifyBuilder cardVerifyBuilder,
      final VerifyTokenCallback callback
  ) {
    createRest(fragment, cardVerifyBuilder, callback);
  }

  private static void createRest(
      final SeamlesspayFragment fragment,
      final CardVerifyBuilder cardChargeBulder,
      final VerifyTokenCallback callback
  ) {

    String data = cardChargeBulder.build();

    try {
      JSONObject dataJson = new JSONObject(data);

      dataJson.put(
          "deviceFingerprint",
          AppHelper.getDeviceFingerprint(fragment.getContext())
      );

      data = dataJson.toString();
    } catch (JSONException ignored) {}

    fragment
        .getApiHttpClient()
        .post(
            ChargeClient.CHARGE_ENDPOINT,
            data,
            new HttpResponseCallback() {

              @Override
              public void success(String responseBody) {
                try {
                  callback.success(BaseChargeToken.parseChargeToken(responseBody));
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

  private static void createRest(
    final SeamlesspayFragment fragment,
    final CardChargeBulder cardChargeBulder,
    final BaseChargeTokenCallback callback
  ) {

    String data = cardChargeBulder.build();

    try {
      JSONObject dataJson = new JSONObject(data);

      dataJson.put(
        "deviceFingerprint",
        AppHelper.getDeviceFingerprint(fragment.getContext())
      );

      data = dataJson.toString();
    } catch (JSONException ignored) {}

    fragment
      .getApiHttpClient()
      .post(
        ChargeClient.CHARGE_ENDPOINT,
        data,
        new HttpResponseCallback() {

          @Override
          public void success(String responseBody) {
            try {
              callback.success(BaseChargeToken.parseChargeToken(responseBody));
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

  private static void createRest(
      final SeamlesspayFragment fragment,
      final AdjustChargeBuilder adjustBuilder,
      final String transactionId,
      final BaseAdjustCallback callback
  ) {

    String data = adjustBuilder.build();

    try {
      JSONObject dataJson = new JSONObject(data);

      dataJson.put(
          "deviceFingerprint",
          AppHelper.getDeviceFingerprint(fragment.getContext())
      );

      data = dataJson.toString();
    } catch (JSONException ignored) {}

    String path = ChargeClient.CHARGE_ENDPOINT + "/"+ transactionId;

    fragment
        .getApiHttpClient()
        .update(
            path,
            data,
            new HttpResponseCallback() {

              @Override
              public void success(String responseBody) {
                callback.success();
              }

              @Override
              public void failure(Exception exception) {
                callback.failure(exception);
              }
            }
        );
  }


  private static void createRest(
      final SeamlesspayFragment fragment,
      final String transactionId,
      final BaseVoidCallback callback
  ) {

    String path = ChargeClient.CHARGE_ENDPOINT + "/"+ transactionId;
    fragment
        .getApiHttpClient()
        .delete(
            path,
            new HttpResponseCallback() {

              @Override
              public void success(String responseBody) {
                callback.success();
              }

              @Override
              public void failure(Exception exception) {
                callback.failure(exception);
              }
            }
        );
  }
}
