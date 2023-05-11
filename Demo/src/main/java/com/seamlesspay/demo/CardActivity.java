/*
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.seamlesspay.api.Token;
import com.seamlesspay.api.SeamlesspayFragment;
import com.seamlesspay.api.exceptions.InvalidArgumentException;
import com.seamlesspay.api.interfaces.PaymentMethodTokenCreatedListener;
import com.seamlesspay.api.interfaces.SeamlesspayErrorListener;
import com.seamlesspay.api.models.CardBuilder;
import com.seamlesspay.api.models.CardToken;
import com.seamlesspay.api.models.Configuration;
import com.seamlesspay.api.models.PaymentMethodToken;
import com.seamlesspay.cardform.OnCardFormFieldFocusedListener;
import com.seamlesspay.cardform.OnCardFormSubmitListener;
import com.seamlesspay.cardform.utils.CardType;
import com.seamlesspay.cardform.view.CardEditText;
import com.seamlesspay.cardform.view.CardForm;
import com.seamlesspay.cardform.view.ExpirationDateEditText;

public class CardActivity
  extends BaseActivity
  implements
    PaymentMethodTokenCreatedListener,
    SeamlesspayErrorListener,
    OnCardFormSubmitListener,
    OnCardFormFieldFocusedListener {
  private Button mPurchaseButton;
  private CardForm mCardForm;
  private CardType mCardType;
  private ExpirationDateEditText mExpDate;
  private Configuration mConfiguration;
  private Long mStartTime, mEndTime;
  private ProgressDialog mLoading;

  @Override
  protected void onCreate(Bundle onSaveInstanceState) {
    super.onCreate(onSaveInstanceState);

    setContentView(R.layout.custom_activity);
    setUpAsBack();

    mCardForm = findViewById(R.id.card_form);

    mCardForm.setOnFormFieldFocusedListener(this);
    mCardForm.setOnCardFormSubmitListener(this);

    mPurchaseButton = findViewById(R.id.purchase_button);
    mExpDate = findViewById(R.id.bt_card_form_expiration);
    mCardForm
      .cardRequired(true)
      .expirationRequired(true)
      .cvvRequired(true)
      .postalCodeRequired(true)
      .mobileNumberRequired(false)
      .actionLabel(getString(R.string.purchase))
      .setup(this);
    mExpDate.useDialogForExpirationDateEntry(this, false);
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
      mSeamlesspayFragment =
        SeamlesspayFragment.newInstance(this, mAuthorization);
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

    if (
      !(field instanceof CardEditText) &&
      !TextUtils.isEmpty(mCardForm.getCardNumber())
    ) {
      CardType cardType = CardType.forCardNumber(mCardForm.getCardNumber());

      if (mCardType != cardType) {
        mCardType = cardType;
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
      .setTxnType(CardBuilder.Keys.CREDIT_CARD_TYPE)
      .billingZip(mCardForm.getPostalCode())
      .cvv(mCardForm.getCvv());

    Token.tokenize(mSeamlesspayFragment, cardBuilder);

    mStartTime = System.currentTimeMillis();
  }

  @Override
  public void onPaymentMethodTokenCreated(
    PaymentMethodToken paymentMethodToken
  ) {
    super.onPaymentMethodTokenCreated(paymentMethodToken);

    mEndTime = System.currentTimeMillis();

    long timeElapsed = mEndTime - mStartTime;

    paymentMethodToken.setInfo(mCardForm.getCvv());

    Intent intent = new Intent()
      .putExtra(MainActivity.EXTRA_PAYMENT_RESULT, paymentMethodToken)
      .putExtra(MainActivity.EXTRA_TIMER_RESULT, timeElapsed);

    setResult(RESULT_OK, intent);
    finish();
  }

  private void safelyCloseLoadingView() {
    if (mLoading != null && mLoading.isShowing()) {
      mLoading.dismiss();
    }
  }

  public static String getDisplayString(CardToken token, long timeElapsed) {
    return (
      "Card Last Four: " +
      token.getLastFour() +
      "\nToken: " +
      token.getToken() +
      "\nExpDate: " +
      token.getExpirationDate() +
      "\nTokenization runtime : " +
      ((float) timeElapsed / 1000) +
      " s"
    );
  }
}
