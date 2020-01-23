package com.seamlesspay.api;


import com.seamlesspay.api.interfaces.BaseChargeNonceCallback;
import com.seamlesspay.api.models.BaseChargeNonce;
import com.seamlesspay.api.models.CardChargeBulder;

public class Charge {

    public static void create(final SeamlesspayFragment fragment, final CardChargeBulder chargeBuilder) {

        ChargeClient.create(fragment, chargeBuilder, new BaseChargeNonceCallback() {
            @Override
            public void success(BaseChargeNonce baseChargeNonce) {
                fragment.postCallback(baseChargeNonce);
            }

            @Override
            public void failure(Exception exception) {
                fragment.postCallback(exception);
            }
        });
    }
}
