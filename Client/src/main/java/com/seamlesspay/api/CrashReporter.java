/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import io.sentry.Sentry;

class CrashReporter implements UncaughtExceptionHandler {
  private SeamlesspayFragment mSeamlesspayFragment;
  private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

  static CrashReporter setup(SeamlesspayFragment fragment) {
    return new CrashReporter(fragment);
  }

  private CrashReporter(SeamlesspayFragment fragment) {
    mSeamlesspayFragment = fragment;
    mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

    Thread.setDefaultUncaughtExceptionHandler(this);
  }

  void tearDown() {
    Thread.setDefaultUncaughtExceptionHandler(mDefaultExceptionHandler);
  }

  @Override
  public void uncaughtException(Thread thread, Throwable ex) {
    StringWriter stringWriter = new StringWriter();

    ex.printStackTrace(new PrintWriter(stringWriter));
    Sentry.captureException(ex);

    if (mDefaultExceptionHandler != null) {
      mDefaultExceptionHandler.uncaughtException(thread, ex);
    }
  }
}
