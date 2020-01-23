package com.seamlesspay.demo;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.seamlesspay.api.SeamlesspayFragment;
import com.seamlesspay.api.Charge;
import com.seamlesspay.api.exceptions.InvalidArgumentException;
import com.seamlesspay.api.models.BaseChargeNonce;
import com.seamlesspay.api.models.CardChargeBulder;
import com.seamlesspay.api.models.PaymentMethodNonce;
import com.seamlesspay.demo.R;

public class CreateTransactionActivity extends BaseActivity {

    public static final String EXTRA_PAYMENT_METHOD_NONCE = "nonce";
    private ProgressBar mLoadingSpinner;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void reset() {
    }

    @Override
    public void onBaseChargeNonceCreated(BaseChargeNonce chargeNonceNonce) {
        super.onBaseChargeNonceCreated(chargeNonceNonce);

        setStatus(R.string.transaction_complete);
        setMessage("Amount: " + chargeNonceNonce.getAmount() +
                "\nStatus: " +  chargeNonceNonce.getStatus() +
                "\nStatus message: " + chargeNonceNonce.getStatusDescription() +
                "\ntxnID #: " + chargeNonceNonce.getChargeId());
    }

    @Override
    protected void onAuthorizationFetched() {

        try {
            mSeamlesspayFragment = SeamlesspayFragment.newInstance(this, mAuthorization);
        } catch (InvalidArgumentException e) {
            onError(e);
        }

        crateCharge((PaymentMethodNonce) getIntent().getParcelableExtra(EXTRA_PAYMENT_METHOD_NONCE));
    }

    public void onError(Exception error) {
        super.onError(error);

        setStatus(R.string.transaction_failed);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_transaction_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mLoadingSpinner = findViewById(R.id.loading_spinner);
        setTitle(R.string.processing_transaction);
    }

    private void crateCharge(PaymentMethodNonce nonce) {

        CardChargeBulder chargeBulder = new CardChargeBulder()
                .setAmount("1")
                .setCurrency(CardChargeBulder.Keys.CURRENCY_USD)
                .setCapture(true)
                .setToken(nonce.getToken())
                .setDescription("Demo Android Client Charge")
                .setCvv(nonce.getInfo());

        Charge.create(mSeamlesspayFragment, chargeBulder);
    }

    private void setStatus(int message) {
        mLoadingSpinner.setVisibility(View.GONE);
        setTitle(message);
        TextView status = findViewById(R.id.transaction_status);
        status.setText(message);
        status.setVisibility(View.VISIBLE);
    }

    private void setMessage(String message) {
        mLoadingSpinner.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.transaction_message);
        textView.setText(message);
        textView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}
