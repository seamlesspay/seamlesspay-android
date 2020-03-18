/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.exceptions;

/**
 * Error thrown when arguments provided to a method are invalid.
 */
public class InvalidArgumentException extends Exception {

  public InvalidArgumentException(String message) {
    super(message);
  }
}
