/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.interfaces;

/**
 * Communicates responses from a HTTP request on the main thread.
 * One and only one method will be invoked in response to a request.
 */
public interface HttpResponseCallback {
  /**
   * @param responseBody response to the successful HTTP request.
   *        Successful is defined as requests with the response code
   *        {@link java.net.HttpURLConnection#HTTP_OK},
   *        {@link java.net.HttpURLConnection#HTTP_CREATED}
   *        or {@link java.net.HttpURLConnection#HTTP_ACCEPTED}.
   */

  void success(String responseBody);

  /**
   * @param exception error that caused the request to fail.
   */

  void failure(Exception exception);
}
