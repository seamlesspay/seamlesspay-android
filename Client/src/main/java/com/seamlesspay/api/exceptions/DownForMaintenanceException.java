/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.exceptions;

/**
 * Exception thrown when a 503 HTTP_UNAVAILABLE response is encountered. Indicates the server is
 * unreachable or the request timed out.
 */
public class DownForMaintenanceException extends Exception {

  public DownForMaintenanceException(String message) {
    super(message);
  }
}
