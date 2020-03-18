/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.interfaces;

import android.content.Intent;
import com.seamlesspay.api.SeamlesspayFragment;

/**
 * Interface that defines a callback for {@link android.app.Fragment#onActivityResult(int, int, Intent)}
 * cancel events.
 */
public interface SeamlesspayCancelListener extends SeamlesspayListener {
  /**
   * {@link #onCancel(int)} ()} will be called when {@link SeamlesspayFragment}
   * receives an {@link android.app.Fragment#onActivityResult(int, int, Intent)} with a resultCode
   * of {@link android.app.Activity#RESULT_CANCELED}.
   *
   * @param requestCode The request code used to start the {@link android.app.Activity} that was
   *        canceled.
   */
  void onCancel(int requestCode);
}
