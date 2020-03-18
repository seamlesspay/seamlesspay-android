/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.cardform.view;

import android.content.Context;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.util.AttributeSet;

/**
 * Input for mobile number. Validated for presence only due to the wide
 * variation of mobile number formats worldwide.
 */
public class MobileNumberEditText extends ErrorEditText {

  public MobileNumberEditText(Context context) {
    super(context);
    init();
  }

  public MobileNumberEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public MobileNumberEditText(
    Context context,
    AttributeSet attrs,
    int defStyle
  ) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    if (isInEditMode()) {
      return;
    }

    setInputType(InputType.TYPE_CLASS_PHONE);

    InputFilter[] filters = { new LengthFilter(14) };

    setFilters(filters);
    addTextChangedListener(new PhoneNumberFormattingTextWatcher());
  }

  /**
   * @return the unformatted mobile number entered by the user
   */
  public String getMobileNumber() {
    return PhoneNumberUtils.stripSeparators(getText().toString());
  }

  @Override
  public boolean isValid() {
    return isOptional() || getText().toString().length() >= 8;
  }

  @Override
  public String getErrorMessage() {
    return "Mobile number is required";
  }
}
