package com.seamlesspay.api;


import com.seamlesspay.api.interfaces.BaseChargeTokenCallback;
import com.seamlesspay.api.models.BaseChargeToken;
import com.seamlesspay.api.models.CardChargeBulder;

public class Charge {

    public static void create(final SeamlesspayFragment fragment, final CardChargeBulder chargeBuilder) {

        ChargeClient.create(fragment, chargeBuilder, new BaseChargeTokenCallback() {
            @Override
            public void success(BaseChargeToken baseChargeToken) {
                fragment.postCallback(baseChargeToken);
            }

            @Override
            public void failure(Exception exception) {
                fragment.postCallback(exception);
            }
        });
    }
}
