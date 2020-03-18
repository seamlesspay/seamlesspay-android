/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.interfaces;

import com.seamlesspay.api.models.BaseChargeToken;

/**
 * Interface that defines callbacks to be called when {@link BaseChargeToken}s are created.
 */

public interface BaseChargeTokenCreatedListener extends SeamlesspayListener {
  /**
   * {@link #onBaseChargeTokenCreated} will be called when a new {@link BaseChargeToken} has been
   * created.
   *
   * @param baseChargeToken the {@link BaseChargeToken}.
   */
  void onBaseChargeTokenCreated(BaseChargeToken baseChargeToken);
}
