/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.cardform;

/**
 * Listener to receive a callback when the card form becomes valid or invalid
 */
public interface OnCardFormValidListener {
  /**
   * Called when the card form becomes valid or invalid
   * @param valid indicates wither the card form is currently valid or invalid
   */
  void onCardFormValid(boolean valid);
}
