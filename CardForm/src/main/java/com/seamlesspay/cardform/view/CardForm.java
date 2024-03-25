/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.cardform.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.IntDef;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.seamlesspay.cardform.CardScanningFragment;
import com.seamlesspay.cardform.OnCardFormFieldFocusedListener;
import com.seamlesspay.cardform.OnCardFormSubmitListener;
import com.seamlesspay.cardform.OnCardFormValidListener;
import com.seamlesspay.cardform.R;
import com.seamlesspay.cardform.utils.CardType;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import io.sentry.Sentry;
import io.sentry.SentryOptions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class CardForm
  extends LinearLayout
  implements
    CardEditText.OnCardTypeChangedListener,
    OnFocusChangeListener,
    OnClickListener,
    OnEditorActionListener,
    TextWatcher {
  /**
   * Hides the field.
   */
  public static final int FIELD_DISABLED = 0;

  /**
   * Shows the field, and makes the field optional.
   */
  public static final int FIELD_OPTIONAL = 1;

  /**
   * Shows the field, and require the field value to be non
   * empty when validating the card form.
   */
  public static final int FIELD_REQUIRED = 2;

  private CardScanningFragment mCardScanningFragment;

  /**
   * The statuses a field can be.
   */
  @Retention(RetentionPolicy.SOURCE)
  @IntDef({ FIELD_DISABLED, FIELD_OPTIONAL, FIELD_REQUIRED })
  @interface FieldStatus {
  }

  private CardEditText mCardNumber;
  private CardholderNameEditText mCardholderName;
  private CountryCodeEditText mCountryCode;
  private CvvEditText mCvv;
  private ExpirationDateEditText mExpiration;
  private InitialValueCheckBox mSaveCardCheckBox;
  private List<ErrorEditText> mVisibleEditTexts;
  private MobileNumberEditText mMobileNumber;
  private PostalCodeEditText mPostalCode;
  private TextView mMobileNumberExplanation;
  private ImageView mCardIcon;
  private TextView mDateSeparator;

  private boolean mCardNumberRequired;
  private boolean mCvvRequired;
  private boolean mExpirationRequired;
  private boolean mMobileNumberRequired;
  private boolean mPostalCodeRequired;
  private boolean mSaveCardCheckBoxChecked;
  private boolean mSaveCardCheckBoxVisible;
  private boolean mValid = false;
  private int mCardholderNameStatus = FIELD_DISABLED;
  private String mActionLabel;

  private CardEditText.OnCardTypeChangedListener mOnCardTypeChangedListener;
  private OnCardFormFieldFocusedListener mOnCardFormFieldFocusedListener;
  private OnCardFormSubmitListener mOnCardFormSubmitListener;
  private OnCardFormValidListener mOnCardFormValidListener;

  public CardForm(Context context) {
    super(context);
    init();
  }

  public CardForm(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public CardForm(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @TargetApi(VERSION_CODES.LOLLIPOP)
  public CardForm(
    Context context,
    AttributeSet attrs,
    int defStyleAttr,
    int defStyleRes
  ) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  private void init() {
    setVisibility(GONE);
    setOrientation(VERTICAL);

    inflate(getContext(), R.layout.bt_card_form_fields, this);

    mCardholderName = findViewById(R.id.bt_card_form_cardholder_name);
    mCardNumber = findViewById(R.id.bt_card_form_card_number);
    mCountryCode = findViewById(R.id.bt_card_form_country_code);
    mCvv = findViewById(R.id.bt_card_form_cvv);
    mExpiration = findViewById(R.id.bt_card_form_expiration);
    mMobileNumber = findViewById(R.id.bt_card_form_mobile_number);
    mMobileNumberExplanation =
      findViewById(R.id.bt_card_form_mobile_number_explanation);
    mPostalCode = findViewById(R.id.bt_card_form_postal_code);
    mSaveCardCheckBox = findViewById(R.id.bt_card_form_save_card_checkbox);
    mCardIcon = findViewById(R.id.iv_card_icon);
    mDateSeparator = findViewById(R.id.bt_card_form_date_divider);

    mVisibleEditTexts = new ArrayList<>();

    setListeners(mCardholderName);
    setListeners(mCardNumber);
    setListeners(mCvv);
    setListeners(mExpiration);
    setListeners(mMobileNumber);
    setListeners(mPostalCode);

    mCardNumber.setOnCardTypeChangedListener(this);
  }

  private void setUpSentry() {
    SentryOptions options = new SentryOptions();
    options.setDsn("https://f3ca34981162465cabb2783483179ae9@o4504125304209408.ingest.sentry.io/4504140012584960");
    Sentry.init(options);
    Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
      @Override
      public void uncaughtException(Thread thread, Throwable throwable) {
        Sentry.captureException(throwable);
      }
    });
  }

  /**
   * @param required {@code true} to show and require a credit card number,
   * {@code false} otherwise. Defaults to {@code false}.
   * @return {@link CardForm} for method chaining
   */
  public CardForm cardRequired(boolean required) {
    mCardNumberRequired = required;
    return this;
  }

  /**
   * @param required {@code true} to show and require an expiration date,
   * {@code false} otherwise. Defaults to {@code false}.
   * @return {@link CardForm} for method chaining
   */
  public CardForm expirationRequired(boolean required) {
    mExpirationRequired = required;
    return this;
  }

  /**
   * @param required {@code true} to show and require a cvv, {@code false}
   * otherwise. Defaults to {@code false}.
   * @return {@link CardForm} for method chaining
   */
  public CardForm cvvRequired(boolean required) {
    mCvvRequired = required;
    return this;
  }

  /**
   * @param cardHolderNameStatus can be one of the {@link FieldStatus} options.
   * - {@link CardForm#FIELD_DISABLED} to hide this field (default)
   * - {@link CardForm#FIELD_OPTIONAL} to show this field but make it optional
   * - {@link CardForm#FIELD_REQUIRED} to show this field and make it required
   *
   * @return {@link CardForm} for method chaining
   */
  public CardForm cardholderName(@FieldStatus int cardHolderNameStatus) {
    mCardholderNameStatus = cardHolderNameStatus;
    return this;
  }

  /**
   * @param required {@code true} to show and require a postal code,
   * {@code false} otherwise. Defaults to {@code false}.
   * @return {@link CardForm} for method chaining
   */
  public CardForm postalCodeRequired(boolean required) {
    mPostalCodeRequired = required;
    return this;
  }

  /**
   * @param required {@code true} to show and require a mobile number,
   * {@code false} otherwise. Defaults to {@code false}.
   * @return {@link CardForm} for method chaining
   */
  public CardForm mobileNumberRequired(boolean required) {
    mMobileNumberRequired = required;
    return this;
  }

  /**
   * @param actionLabel the {@link java.lang.String} to display to
   * the user to submit the form from the keyboard
   * @return {@link CardForm} for method chaining
   */
  public CardForm actionLabel(String actionLabel) {
    mActionLabel = actionLabel;
    return this;
  }

  /**
   * @param mobileNumberExplanation the {@link java.lang.String}
   * to display below the mobile number input
   * @return {@link CardForm} for method chaining
   */
  public CardForm mobileNumberExplanation(String mobileNumberExplanation) {
    mMobileNumberExplanation.setText(mobileNumberExplanation);
    return this;
  }

  /**
   * @param mask if {@code true}, card number input will be masked.
   */
  public CardForm maskCardNumber(boolean mask) {
    mCardNumber.setMask(mask);
    return this;
  }

  /**
   * @param mask if {@code true}, CVV input will be masked.
   */
  public CardForm maskCvv(boolean mask) {
    mCvv.setMask(mask);
    return this;
  }

  /**
   * @param visible Determines if the save card CheckBox should be shown.
   * Defaults to hidden / {@code false}
   * @return {@link CardForm} for method chaining
   */
  public CardForm saveCardCheckBoxVisible(boolean visible) {
    mSaveCardCheckBoxVisible = visible;
    return this;
  }

  /**
   * @param checked The default value for the Save Card CheckBox.
   * @return {@link CardForm} for method chaining
   */
  public CardForm saveCardCheckBoxChecked(boolean checked) {
    mSaveCardCheckBoxChecked = checked;
    return this;
  }

  /**
   * Sets up the card form for display to the user using the values provided in
   * CardForm#cardRequired(boolean), CardForm#expirationRequired(boolean), etc.
   * If {CardForm#setup(AppCompatActivity) is not called, the form will
   * not be visible.
   *
   * @param activity Used to set {@link android.view.WindowManager.LayoutParams#FLAG_SECURE}
   * to prevent screenshots
   */
  public void setup(AppCompatActivity activity) {
    mCardScanningFragment =
      (CardScanningFragment) activity
        .getSupportFragmentManager()
        .findFragmentByTag(CardScanningFragment.TAG);

    if (mCardScanningFragment != null) {
      mCardScanningFragment.setCardForm(this);
    }

    activity
      .getWindow()
      .setFlags(
        WindowManager.LayoutParams.FLAG_SECURE,
        WindowManager.LayoutParams.FLAG_SECURE
      );

    boolean cardHolderNameVisible = mCardholderNameStatus != FIELD_DISABLED;

    mExpiration.useDialogForExpirationDateEntry(activity, true);

    setFieldVisibility(mCardholderName, cardHolderNameVisible);

    setFieldVisibility(mCardNumber, mCardNumberRequired);
    setFieldVisibility(mExpiration, mExpirationRequired);
    setFieldVisibility(mCvv, mCvvRequired);

    setFieldVisibility(mPostalCode, mPostalCodeRequired);

    setFieldVisibility(mCountryCode, mMobileNumberRequired);
    setFieldVisibility(mMobileNumber, mMobileNumberRequired);

    setViewVisibility(mMobileNumberExplanation, mMobileNumberRequired);
    setViewVisibility(mSaveCardCheckBox, mSaveCardCheckBoxVisible);

    TextInputEditText editText;

    for (int i = 0; i < mVisibleEditTexts.size(); i++) {
      editText = mVisibleEditTexts.get(i);

      if (i == mVisibleEditTexts.size() - 1) {
        editText.setImeOptions(EditorInfo.IME_ACTION_GO);
        editText.setImeActionLabel(mActionLabel, EditorInfo.IME_ACTION_GO);
        editText.setOnEditorActionListener(this);
      } else {
        editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editText.setImeActionLabel(null, EditorInfo.IME_ACTION_NONE);
        editText.setOnEditorActionListener(null);
      }
    }

    mSaveCardCheckBox.setInitiallyChecked(mSaveCardCheckBoxChecked);

    setVisibility(VISIBLE);
  }

  /**
   * Check if card scanning is available.
   *
   * Card scanning requires the card.io dependency and camera support.
   *
   * @return {@code true} if available, {@code false} otherwise.
   */
  public boolean isCardScanningAvailable() {
    try {
      return CardIOActivity.canReadCardWithCamera();
    } catch (NoClassDefFoundError e) {
      return false;
    }
  }

  /**
   * Launches card.io card scanning is
   * {@link #isCardScanningAvailable()} is {@code true}.
   *
   * @param activity
   */
  public void scanCard(AppCompatActivity activity) {
    if (isCardScanningAvailable() && mCardScanningFragment == null) {
      mCardScanningFragment = CardScanningFragment.requestScan(activity, this);
    }
  }

  /**
   * Use {@link #handleCardIOResponse(int, Intent)} instead.
   */
  @SuppressLint("DefaultLocale")
  @Deprecated
  public void handleCardIOResponse(Intent data) {
    handleCardIOResponse(Integer.MIN_VALUE, data);
  }

  @SuppressLint("DefaultLocale")
  public void handleCardIOResponse(int resultCode, Intent data) {
    if (
      resultCode == Activity.RESULT_CANCELED || resultCode == Activity.RESULT_OK
    ) {
      mCardScanningFragment = null;
    }

    if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
      CreditCard scanResult = data.getParcelableExtra(
        CardIOActivity.EXTRA_SCAN_RESULT
      );

      if (mCardNumberRequired) {
        mCardNumber.setText(scanResult.cardNumber);
        mCardNumber.focusNextView();
      }

      if (scanResult.isExpiryValid() && mExpirationRequired) {
        mExpiration.setText(
          String.format("%02d%d", scanResult.expiryMonth, scanResult.expiryYear)
        );
        mExpiration.focusNextView();
      }
    }
  }

  private void setListeners(EditText editText) {
    editText.addTextChangedListener(this);
    editText.setOnClickListener(this);
    editText.setOnFocusChangeListener(this);
  }

  private void setViewVisibility(View view, boolean visible) {
    view.setVisibility(visible ? VISIBLE : GONE);
  }

  private void setFieldVisibility(ErrorEditText editText, boolean visible) {
    setViewVisibility(editText, visible);
    if (editText.getTextInputLayoutParent() != null) {
      setViewVisibility(editText.getTextInputLayoutParent(), visible);
    }

    if (visible) {
      mVisibleEditTexts.add(editText);
    } else {
      mVisibleEditTexts.remove(editText);
    }
  }

  /**
   * Set the listener to receive a callback when the card form
   * becomes valid or invalid
   * @param listener to receive the callback
   */
  public void setOnCardFormValidListener(OnCardFormValidListener listener) {
    mOnCardFormValidListener = listener;
  }

  /**
   * Set the listener to receive a callback when the card form should be submitted.
   * Triggered from a keyboard by a {@link android.view.inputmethod.EditorInfo#IME_ACTION_GO} event
   *
   * @param listener to receive the callback
   */
  public void setOnCardFormSubmitListener(OnCardFormSubmitListener listener) {
    mOnCardFormSubmitListener = listener;
  }

  /**
   * Set the listener to receive a callback when a field is focused
   *
   * @param listener to receive the callback
   */
  public void setOnFormFieldFocusedListener(
    OnCardFormFieldFocusedListener listener
  ) {
    mOnCardFormFieldFocusedListener = listener;
  }

  /**
   * Set the listener to receive a callback when the {@link CardType} changes.
   *
   * @param listener to receive the callback
   */
  public void setOnCardTypeChangedListener(
    CardEditText.OnCardTypeChangedListener listener
  ) {
    mOnCardTypeChangedListener = listener;
  }

  /**
   * Set {@link android.widget.EditText} fields as enabled or disabled
   *
   * @param enabled {@code true} to enable all required fields, {@code false} to disable all required fields
   */
  public void setEnabled(boolean enabled) {
    mCardholderName.setEnabled(enabled);
    mCardNumber.setEnabled(enabled);
    mCvv.setEnabled(enabled);
    mExpiration.setEnabled(enabled);
    mMobileNumber.setEnabled(enabled);
    mPostalCode.setEnabled(enabled);
  }

  /**
   * @return {@code true} if all require fields are valid, otherwise {@code false}
   */
  public boolean isValid() {
    boolean valid = true;
    if (mCardholderNameStatus == FIELD_REQUIRED) {
      valid = valid && mCardholderName.isValid();
    }
    if (mCardNumberRequired) {
      valid = valid && mCardNumber.isValid();
    }
    if (mExpirationRequired) {
      valid = valid && mExpiration.isValid();
    }
    if (mCvvRequired) {
      valid = valid && mCvv.isValid();
    }
    if (mPostalCodeRequired) {
      valid = valid && mPostalCode.isValid();
    }
    if (mMobileNumberRequired) {
      valid = valid && mCountryCode.isValid() && mMobileNumber.isValid();
    }
    return valid;
  }

  /**
   * Validate all required fields and mark invalid fields with an error indicator
   */
  public void validate() {
    if (mCardholderNameStatus == FIELD_REQUIRED) {
      mCardholderName.validate();
    }
    if (mCardNumberRequired) {
      mCardNumber.validate();
    }
    if (mExpirationRequired) {
      mExpiration.validate();
    }
    if (mCvvRequired) {
      mCvv.validate();
    }
    if (mPostalCodeRequired) {
      mPostalCode.validate();
    }
    if (mMobileNumberRequired) {
      mCountryCode.validate();
      mMobileNumber.validate();
    }
  }

  /**
   * @return {@link CardholderNameEditText} view in the card form
   */
  public CardholderNameEditText getCardholderNameEditText() {
    return mCardholderName;
  }

  /**
   * @return {@link CardEditText} view in the card form
   */
  public CardEditText getCardEditText() {
    return mCardNumber;
  }

  /**
   * @return {@link ExpirationDateEditText} view in the card form
   */
  public ExpirationDateEditText getExpirationDateEditText() {
    return mExpiration;
  }

  /**
   * @return {@link CvvEditText} view in the card form
   */
  public CvvEditText getCvvEditText() {
    return mCvv;
  }

  /**
   * @return {@link PostalCodeEditText} view in the card form
   */
  public PostalCodeEditText getPostalCodeEditText() {
    return mPostalCode;
  }

  /**
   * @return {@link CountryCodeEditText} view in the card form
   */
  public CountryCodeEditText getCountryCodeEditText() {
    return mCountryCode;
  }

  /**
   * @return {@link MobileNumberEditText} view in the card form
   */
  public MobileNumberEditText getMobileNumberEditText() {
    return mMobileNumber;
  }

  /**
   * Set visual indicator on name to indicate error
   *
   * @param errorMessage the error message to display
   */
  public void setCardholderNameError(String errorMessage) {
    if (mCardholderNameStatus == FIELD_REQUIRED) {
      mCardholderName.setError(errorMessage);

      if (
        !mCardNumber.isFocused() &&
        !mExpiration.isFocused() &&
        !mCvv.isFocused()
      ) {
        requestEditTextFocus(mCardholderName);
      }
    }
  }

  /**
   * Set visual indicator on card number to indicate error
   *
   * @param errorMessage the error message to display
   */
  public void setCardNumberError(String errorMessage) {
    if (mCardNumberRequired) {
      mCardNumber.setError(errorMessage);

      requestEditTextFocus(mCardNumber);
    }
  }

  /**
   * Set visual indicator on expiration to indicate error
   *
   * @param errorMessage the error message to display
   */
  public void setExpirationError(String errorMessage) {
    if (mExpirationRequired) {
      mExpiration.setError(errorMessage);

      if (!mCardNumber.isFocused()) {
        requestEditTextFocus(mExpiration);
      }
    }
  }

  /**
   * Set visual indicator on cvv to indicate error
   *
   * @param errorMessage the error message to display
   */
  public void setCvvError(String errorMessage) {
    if (mCvvRequired) {
      mCvv.setError(errorMessage);

      if (!mCardNumber.isFocused() && !mExpiration.isFocused()) {
        requestEditTextFocus(mCvv);
      }
    }
  }

  /**
   * Set visual indicator on postal code to indicate error
   *
   * @param errorMessage the error message to display
   */
  public void setPostalCodeError(String errorMessage) {
    if (mPostalCodeRequired) {
      mPostalCode.setError(errorMessage);

      if (
        !mCardNumber.isFocused() &&
        !mExpiration.isFocused() &&
        !mCvv.isFocused() &&
        !mCardholderName.isFocused()
      ) {
        requestEditTextFocus(mPostalCode);
      }
    }
  }

  /**
   * Set visual indicator on country code to indicate error
   *
   * @param errorMessage the error message to display
   */
  public void setCountryCodeError(String errorMessage) {
    if (mMobileNumberRequired) {
      mCountryCode.setError(errorMessage);

      if (
        !mCardNumber.isFocused() &&
        !mExpiration.isFocused() &&
        !mCvv.isFocused() &&
        !mCardholderName.isFocused() &&
        !mPostalCode.isFocused()
      ) {
        requestEditTextFocus(mCountryCode);
      }
    }
  }

  /**
   * Set visual indicator on mobile number field to indicate error
   *
   * @param errorMessage the error message to display
   */
  public void setMobileNumberError(String errorMessage) {
    if (mMobileNumberRequired) {
      mMobileNumber.setError(errorMessage);

      if (
        !mCardNumber.isFocused() &&
        !mExpiration.isFocused() &&
        !mCvv.isFocused() &&
        !mCardholderName.isFocused() &&
        !mPostalCode.isFocused() &&
        !mCountryCode.isFocused()
      ) {
        requestEditTextFocus(mMobileNumber);
      }
    }
  }

  private void requestEditTextFocus(EditText editText) {
    editText.requestFocus();
    (
      (InputMethodManager) getContext()
        .getSystemService(Context.INPUT_METHOD_SERVICE)
    ).showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
  }

  /**
   * Attempt to close the soft keyboard. Will have no effect if the keyboard is not open.
   */
  public void closeSoftKeyboard() {
    mCardNumber.closeSoftKeyboard();
  }

  /**
   * @return the text in the cardholder name field
   */
  public String getCardholderName() {
    return mCardholderName.getText().toString();
  }

  /**
   * @return the text in the card number field
   */
  public String getCardNumber() {
    return mCardNumber.getText().toString();
  }

  /**
   * @return the 2-digit month, formatted with a leading zero
   * if necessary from the expiration field. If no month has
   * been specified, an empty string is returned.
   */
  public String getExpirationMonth() {
    return mExpiration.getMonth();
  }

  /**
   * @return the 2- or 4-digit year depending on user input
   * from the expiration field. If no year has been specified,
   * an empty string is returned.
   */
  public String getExpirationYear() {
    return mExpiration.getYear();
  }

  /**
   * @return the text in the cvv field
   */
  public String getCvv() {
    return mCvv.getText().toString();
  }

  /**
   * @return the text in the postal code field
   */
  public String getPostalCode() {
    return mPostalCode.getText().toString();
  }

  /**
   * @return the text in the country code field
   */
  public String getCountryCode() {
    return mCountryCode.getCountryCode();
  }

  /**
   * @return the unformatted text in the mobile number field
   */
  public String getMobileNumber() {
    return mMobileNumber.getMobileNumber();
  }

  /**
   * @return whether or not the save card CheckBox is checked
   */
  public boolean isSaveCardCheckBoxChecked() {
    return mSaveCardCheckBox.isChecked();
  }

  @Override
  public void onCardTypeChanged(CardType cardType) {
    mCvv.setCardType(cardType);
    mCardIcon.setImageResource(cardType.getFrontResource());
    if (mOnCardTypeChangedListener != null) {
      mOnCardTypeChangedListener.onCardTypeChanged(cardType);
    }
  }

  @Override
  public void onFocusChange(View v, boolean hasFocus) {
    if (hasFocus && mOnCardFormFieldFocusedListener != null) {
      mOnCardFormFieldFocusedListener.onCardFormFieldFocused(v);
    }
    if (v instanceof ExpirationDateEditText) {
      int text = getExpirationDateEditText().getText().length();
      if (hasFocus) {
        mDateSeparator.setVisibility(VISIBLE);
      } else if (text == 0) {
        mDateSeparator.setVisibility(GONE);
      }
    }
  }

  @Override
  public void onClick(View v) {
    if (mOnCardFormFieldFocusedListener != null) {
      mOnCardFormFieldFocusedListener.onCardFormFieldFocused(v);
    }
  }

  @Override
  public void afterTextChanged(Editable s) {
    boolean valid = isValid();

    if (mValid != valid) {
      mValid = valid;
      if (mOnCardFormValidListener != null) {
        mOnCardFormValidListener.onCardFormValid(valid);
      }
    }
    if (getExpirationDateEditText().isFocused()) {
      int text = getExpirationDateEditText().getText().length();
      if (text == 0) {
        mDateSeparator.setText(R.string.bt_form_hint_pattern_expiration);
      } else {
        mDateSeparator.setText(R.string.bt_form_hint_pattern_expiration_empty);
      }
    }
  }

  @Override
  public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    if (
      actionId == EditorInfo.IME_ACTION_GO && mOnCardFormSubmitListener != null
    ) {
      mOnCardFormSubmitListener.onCardFormSubmit();

      return true;
    }
    return false;
  }

  @Override
  public void beforeTextChanged(
    CharSequence s,
    int start,
    int count,
    int after
  ) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}
}
