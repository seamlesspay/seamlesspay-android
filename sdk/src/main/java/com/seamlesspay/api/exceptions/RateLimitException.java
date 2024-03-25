/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.exceptions;

/**
 * Exception thrown when a 429 HTTP_TOO_MANY_REQUESTS response is encountered. Indicates the client has hit a request
 * limit and should wait a period of time and try again.
 */
public class RateLimitException extends Exception {

  public RateLimitException(String message) {
    super(message);
  }
}
