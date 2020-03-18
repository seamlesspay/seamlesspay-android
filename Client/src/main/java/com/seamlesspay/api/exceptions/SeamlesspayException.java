/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.exceptions;

import java.io.IOException;

/**
 * Parent class for exceptions encountered when using the SDK.
 */
public class SeamlesspayException extends IOException {

  public SeamlesspayException(String message, Throwable cause) {
    super(message, cause);
  }

  public SeamlesspayException(String message) {
    super(message);
  }

  public SeamlesspayException() {
    super();
  }
}
