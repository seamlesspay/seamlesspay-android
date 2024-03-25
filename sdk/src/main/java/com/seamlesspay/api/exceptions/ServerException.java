/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.exceptions;

/**
 * Exception thrown when a 500 HTTP_INTERNAL_ERROR response is encountered. Indicates an unexpected
 * error from the server.
 */
public class ServerException extends Exception {

  public ServerException(String message) {
    super(message);
  }
}
