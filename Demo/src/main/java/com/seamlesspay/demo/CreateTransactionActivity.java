/*
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.demo;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import com.seamlesspay.api.client.ApiClient;
import com.seamlesspay.api.interfaces.BaseAdjustCallback;
import com.seamlesspay.api.interfaces.BaseChargeTokenCallback;
import com.seamlesspay.api.interfaces.BaseVoidCallback;
import com.seamlesspay.api.models.AdjustChargeBuilder;
import com.seamlesspay.api.models.BaseChargeToken;
import com.seamlesspay.api.models.CardChargeBulder;
import com.seamlesspay.api.models.PaymentMethodToken;

public class CreateTransactionActivity extends BaseActivity {
  public static final String EXTRA_PAYMENT_METHOD_TOKEN = "token";
  public static final String EXTRA_PAYMENT_METHOD = "method";
  private ProgressBar mLoadingSpinner;
  private Button mDeleteButton, mAdjustButton, mCaptureButton, mUncapture;
  private Long mStartTime, mEndTime;
  private String mTransactionId;

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void reset() {}

  @Override
  protected void onAuthorizationFetched() {
    mApiClient = ApiClient.Companion.newInstance(mAuthorization);
    PaymentMethodToken method = (PaymentMethodToken) getIntent().getParcelableExtra(EXTRA_PAYMENT_METHOD_TOKEN);
    crateCharge(method);

    mStartTime = System.currentTimeMillis();
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
    mDeleteButton = findViewById(R.id.btnDelete);
    mAdjustButton = findViewById(R.id.btnAdjust);
    mCaptureButton = findViewById(R.id.btnCapture);
    mUncapture = findViewById(R.id.btnUncapture);

    setTitle(R.string.processing_transaction);
  }

  private void crateCharge(PaymentMethodToken token) {
    CardChargeBulder chargeBulder = new CardChargeBulder()
      .setAmount("1")
      .setCurrency(CardChargeBulder.Keys.CURRENCY_USD)
      .setCapture(true)
      .setToken(token.getToken())
      .setDescription("Demo Android Client Charge");

    mApiClient.charge(chargeBulder, new BaseChargeTokenCallback() {
      @Override
      public void success(BaseChargeToken baseChargedToken) {
        mEndTime = System.currentTimeMillis();

        long timeElapsed = mEndTime - mStartTime;

        mTransactionId = baseChargedToken.getChargeId();

        setStatus(R.string.transaction_complete);
        setMessage(
            "Amount: " +
                baseChargedToken.getAmount() +
                "\nStatus: " +
                baseChargedToken.getStatus() +
                "\nStatus message: " +
                baseChargedToken.getStatusDescription() +
                "\ntxnID #: " +
                baseChargedToken.getChargeId() +
                "\nTransaction runtime : " +
                ((float) timeElapsed / 1000) +
                " s"
        );
        mDeleteButton.setVisibility(View.VISIBLE);
        mAdjustButton.setVisibility(View.VISIBLE);
        mCaptureButton.setVisibility(View.VISIBLE);
        mUncapture.setVisibility(View.VISIBLE);
      }

      @Override
      public void failure(Exception exception) {
        onError(exception);
      }
    });
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

  public void deleteRequest(View v) {
    mApiClient.voidCharge(mTransactionId, new BaseVoidCallback() {
      @Override
      public void success() {
        mDeleteButton.setVisibility(View.GONE);
      }

      @Override
      public void failure(Exception exception) {
        onError(exception);
      }
    });
  }

  public void adjustRequest(View v) {
    AdjustChargeBuilder builder = new AdjustChargeBuilder()
        .setAmount("100");
    adjust(builder);
  }

  public void captureRequest(View v) {
    AdjustChargeBuilder builder = new AdjustChargeBuilder()
        .setCapture(true);
    adjust(builder);
  }

  public void uncaptureRequest(View v) {
    AdjustChargeBuilder builder = new AdjustChargeBuilder()
        .setCapture(false);
    adjust(builder);
  }

  private void adjust(AdjustChargeBuilder builder) {
    mApiClient.adjustCharge(builder, mTransactionId, new BaseAdjustCallback() {
      @Override
      public void success() {
        //Nothing to do
      }

      @Override
      public void failure(Exception exception) {
        onError(exception);
      }
    });
  }
}
