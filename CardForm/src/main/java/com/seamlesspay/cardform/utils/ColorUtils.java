/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.cardform.utils;

import android.app.Activity;
import android.util.TypedValue;

/**
 * Helpers for dealing with UI Colors
 */
public class ColorUtils {

  /**
   * Returns the color for given activity attribute, or a fallback value
   *
   * @param activity Activity for which color is defined
   * @param attr Attribute ID of the activity
   * @param fallbackColor The color to fallback to if not defined
   * @return Color or fallback color data
   */
  public static int getColor(
    Activity activity,
    String attr,
    int fallbackColor
  ) {
    TypedValue color = new TypedValue();

    try {
      int colorId = activity
        .getResources()
        .getIdentifier(attr, "attr", activity.getPackageName());

      if (activity.getTheme().resolveAttribute(colorId, color, true)) {
        return color.data;
      }
    } catch (Exception ignored) {}

    return activity.getResources().getColor(fallbackColor);
  }
}
