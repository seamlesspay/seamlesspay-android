/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Build;
import android.util.Base64;

import com.seamlesspay.api.models.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AppHelper {
  private static String uniqueID = null;
  private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

  public static boolean isIntentAvailable(Context context, Intent intent) {
    List<ResolveInfo> activities = context
      .getPackageManager()
      .queryIntentActivities(intent, 0);

    return activities != null && activities.size() == 1;
  }

  public static String getDeviceFingerprint() {
    Integer screenHeight = AppHelper.getScreenHeight();
    Integer screenWidth = AppHelper.getScreenWidth();
    String deviceModel = AppHelper.DeviceModel();
    String deviceRooted = AppHelper.isDeviceRooted();
    String deviceSoftwareVersion = AppHelper.DeviceSoftwareVersion();
    String timeZone = AppHelper.getTimezone();
    String jsonString =
      "{\"fingerprint\":\"" +
      Build.FINGERPRINT +
      "\"," +
      "\"components\":[" +
      "{\"key\":\"user_agent\", \"value\":\"" + Configuration.getApiUserAgent() + "\"}," +
      "{\"key\":\"language\", \"value\":\"en-US\"}," +
      "{\"key\":\"model\", \"value\":\"" +
      deviceModel +
      "\"}," +
      "{\"key\":\"device_rooted\", \"value\":\"" +
      deviceRooted +
      "\"}," +
      "{\"key\":\"time_zone\", \"value\":\"" +
      timeZone +
      "\"}," +
      "{\"key\":\"software_version\", \"value\":\"" +
      deviceSoftwareVersion +
      "\"}," +
      "{\"key\":\"resolution\",\"value\":[" +
      screenWidth +
      "," +
      screenHeight +
      "]}]}";

    return new String(
      Base64.encodeToString(jsonString.getBytes(), Base64.NO_WRAP)
    );
  }

  public static synchronized String uniqueID(Context context) {
    if (uniqueID == null) {
      SharedPreferences sharedPrefs = context.getSharedPreferences(
        PREF_UNIQUE_ID,
        Context.MODE_PRIVATE
      );

      uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);

      if (uniqueID == null) {
        uniqueID = UUID.randomUUID().toString();

        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString(PREF_UNIQUE_ID, uniqueID);
        editor.commit();
      }
    }
    return uniqueID;
  }

  public static String DeviceSoftwareVersion() {
    String versionName = "";

    try {
      versionName = String.valueOf(Build.VERSION.RELEASE);
    } catch (Exception ignored) {}
    return versionName;
  }

  public static String DeviceModel() {
    String manufacturer = Build.MANUFACTURER;
    String model = Build.MODEL;

    if (model.startsWith(manufacturer)) {
      return model;
    } else {
      return manufacturer + " " + model;
    }
  }

  public static int getScreenWidth() {
    return Resources.getSystem().getDisplayMetrics().widthPixels;
  }

  public static int getScreenHeight() {
    return Resources.getSystem().getDisplayMetrics().heightPixels;
  }

  private static String getAppName(Context context) {
    ApplicationInfo applicationInfo;
    PackageManager packageManager = context.getPackageManager();
    String packageName = context.getPackageName();

    try {
      applicationInfo = packageManager.getApplicationInfo(packageName, 0);
    } catch (PackageManager.NameNotFoundException e) {
      applicationInfo = null;
    }

    String appName = null;

    if (applicationInfo != null) {
      appName = (String) packageManager.getApplicationLabel(applicationInfo);
    }

    if (appName == null) {
      return "ApplicationNameUnknown";
    }

    return appName;
  }

  private static String isDeviceRooted() {
    String buildTags = android.os.Build.TAGS;
    boolean check1 = buildTags != null && buildTags.contains("test-keys");
    boolean check2;
    boolean check3;

    try {
      check2 = new File("/system/app/Superuser.apk").exists();
    } catch (Exception e) {
      check2 = false;
    }

    try {
      Process process = Runtime
        .getRuntime()
        .exec(new String[] { "/system/xbin/which", "su" });

      BufferedReader in = new BufferedReader(
        new InputStreamReader(process.getInputStream())
      );

      check3 = in.readLine() != null;
    } catch (Exception e) {
      check3 = false;
    }

    return Boolean.toString(check1 || check2 || check3);
  }

  private static String getTimezone() {
    long minutes = TimeUnit.MINUTES.convert(
      TimeZone.getDefault().getRawOffset(),
      TimeUnit.MILLISECONDS
    );

    if (minutes % 60 == 0) {
      return (minutes / 60) + "";
    }

    BigDecimal decimalValue = new BigDecimal(minutes)
    .setScale(2, BigDecimal.ROUND_HALF_EVEN);

    BigDecimal decHours = decimalValue
      .divide(new BigDecimal(60), new MathContext(2))
      .setScale(2, BigDecimal.ROUND_HALF_EVEN);

    return decHours.toString();
  }
}
