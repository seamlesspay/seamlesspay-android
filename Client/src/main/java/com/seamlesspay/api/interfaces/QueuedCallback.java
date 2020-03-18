/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.interfaces;

import com.seamlesspay.api.SeamlesspayFragment;

/**
 * Callback used by {@link SeamlesspayFragment} to queue responses to various
 * async operations.
 */
public interface QueuedCallback {
  /**
   * @return {@code true} if the run method should be called, {@code false} otherwise.
   */
  boolean shouldRun();

  /**
   * Method to execute arbitrary code on main thread.
   */

  void run();
}
