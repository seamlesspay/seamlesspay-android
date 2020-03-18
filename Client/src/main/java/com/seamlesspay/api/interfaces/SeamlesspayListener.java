/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.interfaces;

import com.seamlesspay.api.SeamlesspayFragment;

/**
 * Base interface for all event listeners. Only classes that implement this interface
 * (either directly or indirectly) can be registered with
 * {@link SeamlesspayFragment#addListener(SeamlesspayListener)}.
 */
public interface SeamlesspayListener {}
