/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.cardform.utils;

import android.app.Activity;
import com.seamlesspay.cardform.R;

public enum ExpirationDateDialogTheme {
  LIGHT(R.color.bt_black_87, R.color.bt_white_87, R.color.bt_black_38),
  DARK(R.color.bt_white_87, R.color.bt_black_87, R.color.bt_white_38);

  private final int mItemDisabledTextColor;
  private final int mItemInverseTextColor;
  private final int mItemTextColor;

  private int mResolvedItemDisabledTextColor;
  private int mResolvedItemInverseTextColor;
  private int mResolvedItemTextColor;
  private int mResolvedSelectedItemBackground;

  ExpirationDateDialogTheme(
    int itemTextColor,
    int itemInverseTextColor,
    int itemDisabledTextColor
  ) {
    mItemTextColor = itemTextColor;
    mItemInverseTextColor = itemInverseTextColor;
    mItemDisabledTextColor = itemDisabledTextColor;
  }

  public static ExpirationDateDialogTheme detectTheme(Activity activity) {
    ExpirationDateDialogTheme theme;

    if (ViewUtils.isDarkBackground(activity)) {
      theme = ExpirationDateDialogTheme.LIGHT;
    } else {
      theme = ExpirationDateDialogTheme.DARK;
    }

    theme.mResolvedItemTextColor =
      activity.getResources().getColor(theme.mItemTextColor);

    theme.mResolvedItemInverseTextColor =
      ColorUtils.getColor(
        activity,
        "textColorPrimaryInverse",
        theme.mItemInverseTextColor
      );

    theme.mResolvedItemDisabledTextColor =
      activity.getResources().getColor(theme.mItemDisabledTextColor);

    theme.mResolvedSelectedItemBackground =
      ColorUtils.getColor(activity, "colorAccent", R.color.bt_blue);

    return theme;
  }

  public int getItemTextColor() {
    return mResolvedItemTextColor;
  }

  public int getItemInvertedTextColor() {
    return mResolvedItemInverseTextColor;
  }

  public int getItemDisabledTextColor() {
    return mResolvedItemDisabledTextColor;
  }

  public int getSelectedItemBackground() {
    return mResolvedSelectedItemBackground;
  }
}
