/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.internal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import java.util.List;

public class AppHelper {

  public static boolean isIntentAvailable(Context context, Intent intent) {
    List<ResolveInfo> activities = context
      .getPackageManager()
      .queryIntentActivities(intent, 0);

    return activities != null && activities.size() == 1;
  }
}
