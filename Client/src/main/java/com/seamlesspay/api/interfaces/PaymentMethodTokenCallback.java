/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.interfaces;

import com.seamlesspay.api.models.PaymentMethodToken;

/**
 * Communicates {@link PaymentMethodToken} from a HTTP request on the main thread.
 * One and only one method will be invoked in response to a request.
 */
public interface PaymentMethodTokenCallback {
  /**
   * @param paymentMethodToken parsed {@link PaymentMethodToken} from the HTTP request.
   */
  void success(PaymentMethodToken paymentMethodToken);

  /**
   * @param exception error that caused the request to fail.
   */
  void failure(Exception exception);
}
