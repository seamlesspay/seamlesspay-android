package com.seamlesspay.ui.common

import com.seamlesspay.ui.models.PaymentResponse

/**
 * Communicates {@link PaymentResponse} from a HTTP request on the main thread.
 * One and only one method will be invoked in response to a request.
 */
interface PaymentCallback {
  /**
   * @param tokenResponse parsed {@link PaymentResponse} from the HTTP request.
   */
  fun success(paymentResponse: PaymentResponse)

  /**
   * @param exception error that caused the request to fail.
   */
  fun failure(exception: Exception?)
}