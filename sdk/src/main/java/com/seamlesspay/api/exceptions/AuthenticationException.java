/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.exceptions;

/**
 * Exception thrown when a 401 HTTP_UNAUTHORIZED response is encountered.
 * Indicates authentication has failed in some way.
 */
public class AuthenticationException extends Exception {

  public AuthenticationException(String message) {
    super(message);
  }
}
