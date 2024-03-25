/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.exceptions;

/**
 * Exception thrown when an unrecognized error occurs while communicating with a server. This may
 * represent an {@link java.io.IOException} or an unexpected HTTP response.
 */
public class UnexpectedException extends Exception {

  public UnexpectedException(String message) {
    super(message);
  }
}
