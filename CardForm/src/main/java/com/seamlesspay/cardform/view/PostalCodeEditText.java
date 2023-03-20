/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.cardform.view;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.util.AttributeSet;

/**
 * Input for postal codes. Validated for presence only due to the wide
 * variation of postal code formats worldwide.
 */
public class PostalCodeEditText extends ErrorEditText {
  private static final int DEFAULT_MIN_LENGTH = 5;
  private static final int DEFAULT_MAX_LENGTH = 5;

  public PostalCodeEditText(Context context) {
    super(context);
    init();
  }

  public PostalCodeEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public PostalCodeEditText(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);

    InputFilter[] filters = { new LengthFilter(DEFAULT_MAX_LENGTH) };

    setFilters(filters);
  }

  @Override
  public boolean isValid() {
    return isOptional() || getText().toString().length() >= DEFAULT_MIN_LENGTH;
  }

  @Override
  public String getErrorMessage() {
    return "Postal code is required";
  }
}
