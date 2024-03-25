/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.interfaces;

import com.seamlesspay.api.models.BaseChargeToken;

/**
 * Communicates {@link BaseChargeToken} from a HTTP request on the main thread.
 * One and only one method will be invoked in response to a request.
 */
public interface BaseChargeTokenCallback {
  /**
   * @param baseChargedToken parsed {@link BaseChargeToken} from the HTTP request.
   */
  void success(BaseChargeToken baseChargedToken);

  /**
   * @param exception error that caused the request to fail.
   */
  void failure(Exception exception);
}
