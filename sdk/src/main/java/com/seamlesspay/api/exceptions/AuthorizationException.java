/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.exceptions;

/**
 * Exception thrown when a 403 HTTP_FORBIDDEN response is encountered.
 * Indicates the current authorization does not have permission to make the request.
 */
public class AuthorizationException extends Exception {

  public AuthorizationException(String message) {
    super(message);
  }
}
