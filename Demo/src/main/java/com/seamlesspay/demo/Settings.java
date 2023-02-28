/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.ArrayList;
import java.util.List;

public class Settings {
  private static final String SANDBOX_TOKENIZATION_KEY =
    "sk_XXXXXXXXXXXXXXXXXXXXXXXXXX";
  private static final String STAGING_TOKENIZATION_KEY =
      "sk_XXXXXXXXXXXXXXXXXXXXXXXXXX";
  private static final String QAT_TOKENIZATION_KEY =
      "sk_XXXXXXXXXXXXXXXXXXXXXXXXXX";
  private static final String PRODUCTION_TOKENIZATION_KEY =
    "pk_XXXXXXXXXXXXXXXXXXXXXXXXXX";

  private static final String SANDBOX_SECRET_KEY =
    "sk_XXXXXXXXXXXXXXXXXXXXXXXXXX";
  private static final String STAGING_SECRET_KEY =
      "sk_XXXXXXXXXXXXXXXXXXXXXXXXXX";
  private static final String QAT_SECRET_KEY =
      "sk_XXXXXXXXXXXXXXXXXXXXXXXXXX";
  private static final String PRODUCTION_SECRET_KEY =
    "sk_XXXXXXXXXXXXXXXXXXXXXXXXXX";

  protected static final String ENVIRONMENT = "environment";

  private static SharedPreferences sSharedPreferences;

  public static SharedPreferences getPreferences(Context context) {
    if (sSharedPreferences == null) {
      sSharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(
          context.getApplicationContext()
        );
    }

    return sSharedPreferences;
  }

  public static int getEnvironment(Context context) {
    return getPreferences(context).getInt(ENVIRONMENT, 0);
  }

  public static void setEnvironment(Context context, int environment) {
    getPreferences(context).edit().putInt(ENVIRONMENT, environment).apply();
  }

  public static String getEnvironmentName(Context context) {
    int environment = getEnvironment(context);

    switch (environment) {
      case 0:
        return "sandbox";
      case 1:
        return "staging";
      case 2:
        return "QAT";
      default:
        return "production";
    }
  }

  public static String getEnvironmentTokenizationKey(Context context) {
    int environment = getEnvironment(context);

    switch (environment) {
      case 0:
        return SANDBOX_TOKENIZATION_KEY;
      case 1:
        return STAGING_TOKENIZATION_KEY;
      case 2:
        return QAT_TOKENIZATION_KEY;
      default:
        return PRODUCTION_TOKENIZATION_KEY;
    }
  }

  public static String getEnvironmentSecretKey(Context context) {
    int environment = getEnvironment(context);

    switch (environment) {
      case 0:
        return SANDBOX_SECRET_KEY;
      case 1:
        return STAGING_SECRET_KEY;
      case 2:
        return QAT_SECRET_KEY;
      default:
        return PRODUCTION_SECRET_KEY;
    }
  }
}
