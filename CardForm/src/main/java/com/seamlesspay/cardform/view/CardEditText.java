/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.cardform.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import androidx.core.widget.TextViewCompat;
import com.seamlesspay.cardform.R;
import com.seamlesspay.cardform.utils.CardNumberTransformation;
import com.seamlesspay.cardform.utils.CardType;

/**
 * An {@link android.widget.EditText} that displays Card icons
 * based on the number entered.
 */
public class CardEditText extends ErrorEditText implements TextWatcher {

  public interface OnCardTypeChangedListener {
    void onCardTypeChanged(CardType cardType);
  }

  private boolean mMask = false;
  private CardType mCardType;
  private OnCardTypeChangedListener mOnCardTypeChangedListener;
  private TransformationMethod mSavedTranformationMethod;

  public CardEditText(Context context) {
    super(context);
    init();
  }

  public CardEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public CardEditText(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    setInputType(InputType.TYPE_CLASS_NUMBER);
    addTextChangedListener(this);
    updateCardType();
    mSavedTranformationMethod = getTransformationMethod();
  }

  /**
   * @return The {@link CardType} currently entered in
   * the {@link android.widget.EditText}
   */
  public CardType getCardType() {
    return mCardType;
  }

  /**
   * @param mask if {@code true}, all but the last four digits of the
   * card number will be masked when focus leaves the card field. Uses
   * {@link CardNumberTransformation}, transforming the number from
   * something like "4111111111111111" to "•••• 1111".
   */
  public void setMask(boolean mask) {
    mMask = mask;
  }

  @Override
  protected void onFocusChanged(
    boolean focused,
    int direction,
    Rect previouslyFocusedRect
  ) {
    super.onFocusChanged(focused, direction, previouslyFocusedRect);

    if (focused) {
      unmaskNumber();

      if (getText().toString().length() > 0) {
        setSelection(getText().toString().length());
      }
    } else if (mMask && isValid()) {
      maskNumber();
    }
  }

  /**
   * Receive a callback when the {@link CardType} changes
   * @param listener to be called when the {@link CardType} changes
   */
  public void setOnCardTypeChangedListener(OnCardTypeChangedListener listener) {
    mOnCardTypeChangedListener = listener;
  }

  @Override
  public void afterTextChanged(Editable editable) {
    Object[] paddingSpans = editable.getSpans(
      0,
      editable.length(),
      SpaceSpan.class
    );

    for (Object span : paddingSpans) {
      editable.removeSpan(span);
    }

    updateCardType();
    addSpans(editable, mCardType.getSpaceIndices());

    if (mCardType.getMaxCardLength() == getSelectionStart()) {
      validate();

      if (isValid()) {
        focusNextView();
      } else {
        unmaskNumber();
      }
    } else if (!hasFocus()) {
      if (mMask) {
        maskNumber();
      }
    }
  }

  @Override
  public boolean isValid() {
    return isOptional() || mCardType.validate(getText().toString());
  }

  @Override
  public String getErrorMessage() {
    if (TextUtils.isEmpty(getText())) {
      return "Card number is required";
    } else {
      return "Card number is invalid";
    }
  }

  private void maskNumber() {
    if (!(getTransformationMethod() instanceof CardNumberTransformation)) {
      mSavedTranformationMethod = getTransformationMethod();

      setTransformationMethod(new CardNumberTransformation());
    }
  }

  private void unmaskNumber() {
    if (getTransformationMethod() != mSavedTranformationMethod) {
      setTransformationMethod(mSavedTranformationMethod);
    }
  }

  private void updateCardType() {
    CardType type = CardType.forCardNumber(getText().toString());

    if (mCardType != type) {
      mCardType = type;

      InputFilter[] filters = {
        new LengthFilter(mCardType.getMaxCardLength())
      };

      setFilters(filters);
      invalidate();

      if (mOnCardTypeChangedListener != null) {
        mOnCardTypeChangedListener.onCardTypeChanged(mCardType);
      }
    }
  }

  private void addSpans(Editable editable, int[] spaceIndices) {
    final int length = editable.length();

    for (int index : spaceIndices) {
      if (index <= length) {
        editable.setSpan(
          new SpaceSpan(),
          index - 1,
          index,
          Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
      }
    }
  }

  @Override
  public void beforeTextChanged(
    CharSequence s,
    int start,
    int count,
    int after
  ) {}
}
