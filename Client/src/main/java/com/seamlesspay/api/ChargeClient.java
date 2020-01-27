package com.seamlesspay.api;

import com.seamlesspay.api.interfaces.BaseChargeTokenCallback;
import com.seamlesspay.api.interfaces.HttpResponseCallback;
import com.seamlesspay.api.models.BaseChargeToken;
import com.seamlesspay.api.models.CardChargeBulder;

import org.json.JSONException;

class ChargeClient {

    static final String CHARGE_ENDPOINT = "charge";

    static void create(final SeamlesspayFragment fragment, final CardChargeBulder cardChargeBulder,
                         final BaseChargeTokenCallback callback) {

        createRest(fragment, cardChargeBulder, callback);
    }

    private static void createRest(final SeamlesspayFragment fragment, final CardChargeBulder cardChargeBulder,
                                     final BaseChargeTokenCallback callback) {

        fragment.getApiHttpClient().post(ChargeClient.CHARGE_ENDPOINT,
                cardChargeBulder.build(), new HttpResponseCallback() {
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
                });
    }

}
