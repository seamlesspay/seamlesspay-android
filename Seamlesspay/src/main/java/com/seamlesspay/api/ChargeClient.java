package com.seamlesspay.api;

import com.seamlesspay.api.interfaces.BaseChargeNonceCallback;
import com.seamlesspay.api.interfaces.HttpResponseCallback;
import com.seamlesspay.api.models.BaseChargeNonce;
import com.seamlesspay.api.models.CardChargeBulder;

import org.json.JSONException;

import static com.seamlesspay.api.models.BaseChargeNonce.parseChargeNonces;

class ChargeClient {

    static final String CHARGE_ENDPOINT = "charge";

    static void create(final SeamlesspayFragment fragment, final CardChargeBulder cardChargeBulder,
                         final BaseChargeNonceCallback callback) {

        createRest(fragment, cardChargeBulder, callback);
    }

    private static void createRest(final SeamlesspayFragment fragment, final CardChargeBulder cardChargeBulder,
                                     final BaseChargeNonceCallback callback) {

        fragment.getApiHttpClient().post(ChargeClient.CHARGE_ENDPOINT,
                cardChargeBulder.build(), new HttpResponseCallback() {
                    @Override
                    public void success(String responseBody) {
                        try {
                            callback.success(BaseChargeNonce.parseChargeNonces(responseBody));
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
