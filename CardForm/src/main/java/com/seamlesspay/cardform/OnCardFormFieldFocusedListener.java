/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.cardform;

import android.view.View;

/**
 * Listener to receive a callback when a field is focused in the card form
 */
public interface OnCardFormFieldFocusedListener {
  /**
   * @param field that was focused
   */
  void onCardFormFieldFocused(View field);
}
