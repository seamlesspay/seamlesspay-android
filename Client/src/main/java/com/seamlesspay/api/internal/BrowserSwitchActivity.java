/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.internal;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

/**
 * <a href="https://developer.android.com/guide/topics/manifest/activity-element.html#lmode">singleTask</a>
 * Activity used to receive the response from a browser switch.
 * This Activity contains no UI and finishes during {@link Activity#onCreate(Bundle)}.
 */
public class BrowserSwitchActivity extends Activity {
  private static Uri sReturnUri;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    sReturnUri = null;

    if (getIntent() != null && getIntent().getData() != null) {
      sReturnUri = getIntent().getData();
    }

    finish();
  }

  /**
   * @return the uri returned from the browser switch, or {@code null}.
   */

  public static Uri getReturnUri() {
    return sReturnUri;
  }

  /**
   * Clears the return uri.
   */
  public static void clearReturnUri() {
    sReturnUri = null;
  }
}
