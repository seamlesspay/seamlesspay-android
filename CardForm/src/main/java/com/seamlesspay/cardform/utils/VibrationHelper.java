/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.cardform.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Vibrator;

public class VibrationHelper {

  @SuppressLint("MissingPermission")
  public static void vibrate(Context context, int duration) {
    if (hasVibrationPermission(context)) {
      ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(
          duration
        );
    }
  }

  public static boolean hasVibrationPermission(Context context) {
    return (
      context
        .getPackageManager()
        .checkPermission(
          Manifest.permission.VIBRATE,
          context.getPackageName()
        ) ==
      PackageManager.PERMISSION_GRANTED
    );
  }
}
