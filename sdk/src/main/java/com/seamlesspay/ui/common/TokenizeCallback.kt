package com.seamlesspay.ui.common

import com.seamlesspay.ui.models.TokenResponse

/**
 * Communicates {@link TokenResponse} from a HTTP request on the main thread.
 * One and only one method will be invoked in response to a request.
 */
interface TokenizeCallback {
  /**
   * @param tokenResponse parsed {@link TokenResponse} from the HTTP request.
   */
  fun success(tokenResponse: TokenResponse)

  /**
   * @param exception error that caused the request to fail.
   */
  fun failure(exception: Exception?)
}