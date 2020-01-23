package com.seamlesspay.demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.seamlesspay.api.SeamlesspayFragment;
import com.seamlesspay.api.PanVault;
import com.seamlesspay.api.exceptions.InvalidArgumentException;
import com.seamlesspay.api.interfaces.SeamlesspayErrorListener;
import com.seamlesspay.api.interfaces.PaymentMethodNonceCreatedListener;
import com.seamlesspay.api.models.CardBuilder;
import com.seamlesspay.api.models.CardNonce;
import com.seamlesspay.api.models.Configuration;
import com.seamlesspay.api.models.PaymentMethodNonce;
import com.seamlesspay.cardform.OnCardFormFieldFocusedListener;
import com.seamlesspay.cardform.OnCardFormSubmitListener;
import com.seamlesspay.cardform.utils.CardType;
import com.seamlesspay.cardform.view.CardEditText;
import com.seamlesspay.cardform.view.CardForm;
import com.seamlesspay.demo.R;


public class CardActivity extends BaseActivity implements
        PaymentMethodNonceCreatedListener, SeamlesspayErrorListener, OnCardFormSubmitListener,
        OnCardFormFieldFocusedListener {

    private Configuration mConfiguration;
    private ProgressDialog mLoading;
    private CardForm mCardForm;
    private Button mPurchaseButton;

    private CardType mCardType;

    @Override
    protected void onCreate(Bundle onSaveInstanceState) {
        super.onCreate(onSaveInstanceState);

        setContentView(R.layout.custom_activity);
        setUpAsBack();

        mCardForm = findViewById(R.id.card_form);
        mCardForm.setOnFormFieldFocusedListener(this);
        mCardForm.setOnCardFormSubmitListener(this);

        mPurchaseButton = findViewById(R.id.purchase_button);

        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(false)
                .actionLabel(getString(R.string.purchase))
                .setup(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        safelyCloseLoadingView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void reset() {
        mPurchaseButton.setEnabled(false);
    }

    @Override
    protected void onAuthorizationFetched() {
        try {
            mSeamlesspayFragment = SeamlesspayFragment.newInstance(this, mAuthorization);
        } catch (InvalidArgumentException e) {
            onError(e);
        }

        mPurchaseButton.setEnabled(true);
    }

    @Override
    public void onError(Exception error) {
        super.onError(error);
    }

    @Override
    public void onCancel(int requestCode) {
        super.onCancel(requestCode);

        Toast.makeText(this, "Canceled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCardFormFieldFocused(View field) {
        if (mSeamlesspayFragment == null) {
            return;
        }

        if (!(field instanceof CardEditText) && !TextUtils.isEmpty(mCardForm.getCardNumber())) {
            CardType cardType = CardType.forCardNumber(mCardForm.getCardNumber());
            if (mCardType != cardType) {
                mCardType  = cardType;
            }
        }
    }

    @Override
    public void onCardFormSubmit() {
        onPurchase(null);
    }

    public void onPurchase(View v) {
        setProgressBarIndeterminateVisibility(true);

            CardBuilder cardBuilder = new CardBuilder()
                    .accountNumber(mCardForm.getCardNumber())
                    .expirationMonth(mCardForm.getExpirationMonth())
                    .expirationYear(mCardForm.getExpirationYear())
         //           .cvv(mCardForm.getCvv())
                    .setTxnType(CardBuilder.Keys.CREDIT_CARD_TYPE)
                    .billingZip(mCardForm.getPostalCode());

            PanVault.tokenize(mSeamlesspayFragment, cardBuilder);
    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        super.onPaymentMethodNonceCreated(paymentMethodNonce);

            paymentMethodNonce.setInfo(mCardForm.getCvv());

            Intent intent = new Intent()
                    .putExtra(MainActivity.EXTRA_PAYMENT_RESULT, paymentMethodNonce);
            setResult(RESULT_OK, intent);
            finish();
    }

    private void safelyCloseLoadingView() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
        }
    }

    public static String getDisplayString(CardNonce nonce) {
        return "PanVault Last Four: " + nonce.getLastFour() +
                "\nToken: " +  nonce.getToken() +
                "\nExpDate: " +  nonce.getExpirationDate();
    }
}
