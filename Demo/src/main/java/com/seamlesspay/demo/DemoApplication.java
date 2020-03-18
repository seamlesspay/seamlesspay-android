/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.demo;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import java.lang.Thread.UncaughtExceptionHandler;
import retrofit.RestAdapter;

public class DemoApplication
  extends Application
  implements UncaughtExceptionHandler {
  private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

  @Override
  public void onCreate() {
    super.onCreate();

    mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

    Thread.setDefaultUncaughtExceptionHandler(this);
  }

  @Override
  public void uncaughtException(Thread thread, Throwable ex) {
    Log.e("Exception", "Uncaught Exception", ex);

    mDefaultExceptionHandler.uncaughtException(thread, ex);
  }
}
