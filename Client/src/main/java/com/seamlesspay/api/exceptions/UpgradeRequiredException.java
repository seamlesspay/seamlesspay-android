/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.exceptions;

/**
 * Exception thrown when a 426 HTTP_UPGRADE_REQUIRED response is encountered. Indicates that the
 * API used or current SDK version is deprecated and must be updated.
 */
public class UpgradeRequiredException extends Exception {

  public UpgradeRequiredException(String message) {
    super(message);
  }
}
