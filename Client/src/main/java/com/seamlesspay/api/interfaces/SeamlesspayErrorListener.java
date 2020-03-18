/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.interfaces;

/**
 * Interface that defines callbacks for errors that occur when processing Seamlesspay requests.
 */
public interface SeamlesspayErrorListener extends SeamlesspayListener {
  /**
   * {@link #onError(Exception)} will be called when there is an exception that cannot be handled,
   * such as a network or request error, or when there are data validation errors.
   *
   * @param error
   */
  void onError(Exception error);
}
