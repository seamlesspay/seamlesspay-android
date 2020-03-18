/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.demosinglefield;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.seamlesspay.api.Authorization;
import com.seamlesspay.api.Charge;
import com.seamlesspay.api.PanVault;
import com.seamlesspay.api.SeamlesspayFragment;
import com.seamlesspay.api.exceptions.InvalidArgumentException;
import com.seamlesspay.api.interfaces.BaseChargeTokenCreatedListener;
import com.seamlesspay.api.interfaces.PaymentMethodTokenCreatedListener;
import com.seamlesspay.api.interfaces.SeamlesspayErrorListener;
import com.seamlesspay.api.models.BaseChargeToken;
import com.seamlesspay.api.models.CardBuilder;
import com.seamlesspay.api.models.CardChargeBulder;
import com.seamlesspay.api.models.CardToken;
import com.seamlesspay.api.models.Configuration;
import com.seamlesspay.api.models.PaymentMethodToken;
import com.seamlesspay.ui.view.CardInputWidget;

public class MainActivity extends AppCompatActivity implements PaymentMethodTokenCreatedListener,
        BaseChargeTokenCreatedListener, SeamlesspayErrorListener {

    CardInputWidget mCardInputWidget;
    SeamlesspayFragment mSeamlesspayFragment;
    TextView mInfoView, mChargeView;
    private Long mStartTime, mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button payButton = findViewById(R.id.payButton);

        mInfoView = findViewById(R.id.infoView);
        mChargeView = findViewById(R.id.chargeView);
        mCardInputWidget = (CardInputWidget) findViewById(R.id.cardInputWidget);
        mCardInputWidget.configureForUs();

        try {
            Authorization authorization = Authorization.fromKeys(
                    "sandbox",
                    "pk_XXXXXXXXXXXXXXXXXXXXXXXXXX"
            );

            mSeamlesspayFragment =
                    SeamlesspayFragment.newInstance(this, authorization);
        } catch (InvalidArgumentException e) {
            mInfoView.setText(e.getMessage());
        }

        payButton.setOnClickListener(
                new View.OnClickListener() {

                    public void onClick(View v) {
                        // Code here executes on main thread after user presses button
                        mCardInputWidget.clearFocus();
                        mInfoView.setText("");
                        mChargeView.setText("");

                        CardBuilder cardBuilder = new CardBuilder()
                                .accountNumber(mCardInputWidget.getCardNumber())
                                .expirationMonth(mCardInputWidget.getExpirationMonth())
                                .expirationYear(mCardInputWidget.getExpirationYear())
                                .setTxnType(CardBuilder.Keys.CREDIT_CARD_TYPE)
                                .billingZip(mCardInputWidget.getPostalCode())
                                .cvv(mCardInputWidget.getCvv())
                                .verification(true);

                        PanVault.tokenize(mSeamlesspayFragment, cardBuilder);

                        mStartTime = System.currentTimeMillis();
                    }
                }
        );
    }

    public void onPaymentMethodTokenCreated(
            PaymentMethodToken paymentMethodToken
    ) {
        mEndTime = System.currentTimeMillis();

        long timeElapsed = mEndTime - mStartTime;

        CardChargeBulder chargeBulder = new CardChargeBulder()
                .setAmount("1")
                .setCurrency(CardChargeBulder.Keys.CURRENCY_USD)
                .setCapture(true)
                .setToken(paymentMethodToken.getToken())
                .setDescription("Demo Android Client Charge")
                .setCvv(mCardInputWidget.getCvv());

        CardToken token = (CardToken) paymentMethodToken;

        mInfoView.setText(
                "Card Last Four: " +
                        token.getLastFour() +
                        "\nToken: " +
                        token.getToken() +
                        "\nExpDate: " +
                        token.getExpirationDate() +
                        (token.getVerificationResult() != null ? "\nVerificationResult: " : "") +
                        (
                                token.getVerificationResult() != null
                                        ? token.getVerificationResult()
                                        : ""
                        ) +
                        "\nTokenization runtime : " +
                        ((float) timeElapsed / 1000) +
                        " s"
        );

        Charge.create(mSeamlesspayFragment, chargeBulder);

        mStartTime = System.currentTimeMillis();
    }

    public void onBaseChargeTokenCreated(BaseChargeToken chargeToken) {
        mEndTime = System.currentTimeMillis();

        long timeElapsed = mEndTime - mStartTime;

        mChargeView.setText(
                "Amount: " +
                        chargeToken.getAmount() +
                        "\nStatus: " +
                        chargeToken.getStatus() +
                        "\nStatus message: " +
                        chargeToken.getStatusDescription() +
                        "\ntxnID #: " +
                        chargeToken.getChargeId() +
                        "\nCharge runtime : " +
                        ((float) timeElapsed / 1000) +
                        " s"
        );
    }

    public void onError(Exception error) {
        mInfoView.setText("Error\n" + error.getMessage());
    }
}
