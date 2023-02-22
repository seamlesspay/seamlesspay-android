/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.demo;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.seamlesspay.api.models.CardToken;
import com.seamlesspay.api.models.PaymentMethodToken;
import com.seamlesspay.demo.R;

public class MainActivity extends BaseActivity {
  static final String EXTRA_PAYMENT_RESULT = "payment_result";
  static final String EXTRA_TIMER_RESULT = "timer_result";

  private static final int CARDS_REQUEST = 3;
  private static final String KEY_TOKEN = "token";

  private Button mCardsButton;
  private Button mCreateTransactionButton;
  private Button mCreateRefundButton;
  private ImageView mTokenIcon;
  private Long mTimeElapsed;
  private PaymentMethodToken mToken;
  private TextView mDeviceData;
  private TextView mTokenDetails;
  private TextView mTokenString;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.main_activity);

    mTokenIcon = findViewById(R.id.token_icon);
    mTokenString = findViewById(R.id.token);
    mTokenDetails = findViewById(R.id.token_details);
    mDeviceData = findViewById(R.id.device_data);
    mCardsButton = findViewById(R.id.card);
    mCreateTransactionButton = findViewById(R.id.create_transaction);
    mCreateRefundButton = findViewById(R.id.create_refund);

    if (savedInstanceState != null) {
      if (savedInstanceState.containsKey(KEY_TOKEN)) {
        mToken = savedInstanceState.getParcelable(KEY_TOKEN);
      }
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    if (mToken != null) {
      outState.putParcelable(KEY_TOKEN, mToken);
    }
  }

  public void launchCards(View v) {
    Intent intent = new Intent(this, CardActivity.class);

    startActivityForResult(intent, CARDS_REQUEST);
  }

  public void createRefund(View v) {
    Intent intent = new Intent(this, RefundActivity.class)
        .putExtra(CreateTransactionActivity.EXTRA_PAYMENT_METHOD_TOKEN, mToken);

    startActivity(intent);

    mCreateTransactionButton.setEnabled(false);

    clearToken();
  }

  public void createTransaction(View v) {
    Intent intent = new Intent(this, CreateTransactionActivity.class)
    .putExtra(CreateTransactionActivity.EXTRA_PAYMENT_METHOD_TOKEN, mToken);

    startActivity(intent);

    mCreateTransactionButton.setEnabled(false);

    clearToken();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == RESULT_OK) {
      Parcelable returnedData = data.getParcelableExtra(EXTRA_PAYMENT_RESULT);

      mTimeElapsed = data.getLongExtra(EXTRA_TIMER_RESULT, 0);

      if (returnedData instanceof PaymentMethodToken) {
        displayToken((PaymentMethodToken) returnedData);
      }

      mCreateTransactionButton.setEnabled(true);
    }
  }

  @Override
  protected void reset() {
    enableButtons(true);

    mCreateTransactionButton.setEnabled(false);

    clearToken();
  }

  @Override
  protected void onAuthorizationFetched() {
    enableButtons(true);
  }

  private void displayToken(PaymentMethodToken paymentMethodToken) {
    mToken = paymentMethodToken;

    mTokenString.setText(
      getString(R.string.token_placeholder, mToken.getTxnType())
    );

    mTokenString.setVisibility(VISIBLE);

    String details = CardActivity.getDisplayString(
      (CardToken) mToken,
      mTimeElapsed
    );

    mTokenDetails.setText(details);
    mTokenDetails.setVisibility(VISIBLE);

    mCreateTransactionButton.setEnabled(true);
    mCreateRefundButton.setEnabled(true);
  }

  private void clearToken() {
    mTokenIcon.setVisibility(GONE);
    mTokenString.setVisibility(GONE);
    mTokenDetails.setVisibility(GONE);
    mDeviceData.setVisibility(GONE);
    mCreateTransactionButton.setEnabled(false);
    mCreateRefundButton.setEnabled(false);
  }

  private void enableButtons(boolean enable) {
    mCardsButton.setEnabled(enable);
  }
}
