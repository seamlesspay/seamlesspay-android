package com.seamlesspay.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.seamlesspay.api.models.CardNonce;
import com.seamlesspay.api.models.PaymentMethodNonce;
import com.seamlesspay.demo.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends BaseActivity {

    static final String EXTRA_PAYMENT_RESULT = "payment_result";
    private static final int CARDS_REQUEST = 3;
    private static final String KEY_NONCE = "nonce";
    private PaymentMethodNonce mNonce;

    private ImageView mNonceIcon;
    private TextView mNonceString;
    private TextView mNonceDetails;
    private TextView mDeviceData;

    private Button mCardsButton;
    private Button mCreateTransactionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mNonceIcon = findViewById(R.id.nonce_icon);
        mNonceString = findViewById(R.id.nonce);
        mNonceDetails = findViewById(R.id.nonce_details);
        mDeviceData = findViewById(R.id.device_data);

        mCardsButton = findViewById(R.id.card);
        mCreateTransactionButton = findViewById(R.id.create_transaction);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_NONCE)) {
                mNonce = savedInstanceState.getParcelable(KEY_NONCE);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNonce != null) {
            outState.putParcelable(KEY_NONCE, mNonce);
        }
    }

    public void launchCards(View v) {
        Intent intent = new Intent(this, CardActivity.class);
        startActivityForResult(intent, CARDS_REQUEST);
    }

    public void createTransaction(View v) {
        Intent intent = new Intent(this, CreateTransactionActivity.class)
                .putExtra(CreateTransactionActivity.EXTRA_PAYMENT_METHOD_NONCE, mNonce);
        startActivity(intent);

        mCreateTransactionButton.setEnabled(false);
        clearNonce();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Parcelable returnedData = data.getParcelableExtra(EXTRA_PAYMENT_RESULT);
            if (returnedData instanceof PaymentMethodNonce) {
                displayNonce((PaymentMethodNonce) returnedData);
            }
            mCreateTransactionButton.setEnabled(true);
        }
    }

    @Override
    protected void reset() {
        enableButtons(true);
        mCreateTransactionButton.setEnabled(false);
        clearNonce();
    }

    @Override
    protected void onAuthorizationFetched() {
        enableButtons(true);
    }

    private void displayNonce(PaymentMethodNonce paymentMethodNonce) {

        mNonce = paymentMethodNonce;
        mNonceString.setText(getString(R.string.nonce_placeholder, mNonce.getTxnType()));
        mNonceString.setVisibility(VISIBLE);

        String details = CardActivity.getDisplayString((CardNonce) mNonce);
        mNonceDetails.setText(details);
        mNonceDetails.setVisibility(VISIBLE);

        mCreateTransactionButton.setEnabled(true);
    }

    private void clearNonce() {
        mNonceIcon.setVisibility(GONE);
        mNonceString.setVisibility(GONE);
        mNonceDetails.setVisibility(GONE);
        mDeviceData.setVisibility(GONE);
        mCreateTransactionButton.setEnabled(false);
    }

    private void enableButtons(boolean enable) {
        mCardsButton.setEnabled(enable);
    }
}
