/*
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import com.seamlesspay.api.Transaction;
import com.seamlesspay.api.SeamlesspayFragment;
import com.seamlesspay.api.exceptions.InvalidArgumentException;
import com.seamlesspay.api.models.AdjustChargeBuilder;
import com.seamlesspay.api.models.BaseChargeToken;
import com.seamlesspay.api.models.CardChargeBulder;
import com.seamlesspay.api.models.CardVerifyBuilder;
import com.seamlesspay.api.models.PaymentMethodToken;

public class CreateTransactionActivity extends BaseActivity {
  public static final String EXTRA_PAYMENT_METHOD_TOKEN = "token";
  public static final String EXTRA_PAYMENT_METHOD = "method";
  private ProgressBar mLoadingSpinner;
  private Button mDeleteButton, mAdjustButton;
  private Long mStartTime, mEndTime;
  private String mTransactionId;

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void reset() {}

  @Override
  public void onBaseChargeTokenCreated(BaseChargeToken chargeToken) {
    super.onBaseChargeTokenCreated(chargeToken);

    mEndTime = System.currentTimeMillis();

    long timeElapsed = mEndTime - mStartTime;

    mTransactionId = chargeToken.getChargeId();

    setStatus(R.string.transaction_complete);
    setMessage(
      "Amount: " +
      chargeToken.getAmount() +
      "\nStatus: " +
      chargeToken.getStatus() +
      "\nStatus message: " +
      chargeToken.getStatusDescription() +
      "\ntxnID #: " +
      chargeToken.getChargeId() +
      "\nTransaction runtime : " +
      ((float) timeElapsed / 1000) +
      " s"
    );
    mDeleteButton.setVisibility(View.VISIBLE);
    mAdjustButton.setVisibility(View.VISIBLE);
  }

  @Override
  protected void onAuthorizationFetched() {
    try {
      mSeamlesspayFragment =
        SeamlesspayFragment.newInstance(this, mAuthorization);
    } catch (InvalidArgumentException e) {
      onError(e);
    }

    boolean isCharge = getIntent().getBooleanExtra(EXTRA_PAYMENT_METHOD, true);
    PaymentMethodToken method = (PaymentMethodToken) getIntent().getParcelableExtra(EXTRA_PAYMENT_METHOD_TOKEN);
    if (isCharge) {
      crateCharge(method);
    } else  {
      crateVerify(method);
    }


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

    setTitle(R.string.processing_transaction);
  }

  private void crateCharge(PaymentMethodToken token) {
    CardChargeBulder chargeBulder = new CardChargeBulder()
      .setAmount("1")
      .setCurrency(CardChargeBulder.Keys.CURRENCY_USD)
      .setCapture(true)
      .setToken(token.getToken())
      .setDescription("Demo Android Client Charge")
      .setCvv(token.getInfo());

    Transaction.create(mSeamlesspayFragment, chargeBulder);
  }

  private void crateVerify(PaymentMethodToken token) {
    CardVerifyBuilder verifyBuilder = new CardVerifyBuilder()
        .setToken(token.getToken())
        .setDescription("Demo Android Client Verify")
        .setCvv(token.getInfo());

    Transaction.verify(mSeamlesspayFragment, verifyBuilder);
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
    Transaction.delete(mSeamlesspayFragment, mTransactionId);
  }

  public void adjustRequest(View v) {
    AdjustChargeBuilder builder = new AdjustChargeBuilder()
        .setAmount("100");

    Transaction.adjust(mSeamlesspayFragment, mTransactionId, builder);
  }

  @Override
  public void onChargeVoided() {
    super.onChargeVoided();
    mDeleteButton.setVisibility(View.GONE);
  }

  @Override
  public void onChargeUpdated() {
    super.onChargeUpdated();
    mAdjustButton.setVisibility(View.GONE);
  }
}
