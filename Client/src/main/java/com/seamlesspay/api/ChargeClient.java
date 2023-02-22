/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api;

import com.seamlesspay.api.interfaces.BaseChargeTokenCallback;
import com.seamlesspay.api.interfaces.HttpResponseCallback;
import com.seamlesspay.api.internal.AppHelper;
import com.seamlesspay.api.models.BaseChargeToken;
import com.seamlesspay.api.models.CardChargeBulder;
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
}
