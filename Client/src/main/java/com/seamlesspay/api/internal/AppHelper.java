package com.seamlesspay.api.internal;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.TimeZone;
import java.util.UUID;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AppHelper {

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public static boolean isIntentAvailable(Context context, Intent intent) {
        List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(intent, 0);
        return activities != null && activities.size() == 1;
    }

    public static String getDeviceFingerprint(Context context) {

        String uniqueID = AppHelper.uniqueID(context);
        String deviceModel = AppHelper.DeviceModel();
        String deviceSoftwareVersion = AppHelper.DeviceSoftwareVersion();
        Integer screenWidth = AppHelper.getScreenWidth();
        Integer screenHeight = AppHelper.getScreenHeight();
        String appName = AppHelper.getAppName(context);
        String deviceRooted = AppHelper.isDeviceRooted();
        String timeZone = AppHelper.getTimezone();

        String jsonString = "{\"fingerprint\":\"" + uniqueID + "\"," +
                "\"components\":[" +
                "{\"key\":\"user_agent\", \"value\":\"android sdk + v1\"}," +
                "{\"key\":\"language\", \"value\":\"en-US\"}," +
                "{\"key\":\"model\", \"value\":\"" + deviceModel + "\"}," +
                "{\"key\":\"merchant_app_id\", \"value\":\"" + context.getPackageName() + "\"}," +
                "{\"key\":\"merchant_app_name\", \"value\":\"" + appName + "\"}," +
                "{\"key\":\"device_rooted\", \"value\":\"" + deviceRooted + "\"}," +
                "{\"key\":\"time_zone\", \"value\":\"" + timeZone + "\"}," +
                "{\"key\":\"software_version\", \"value\":\"" + deviceSoftwareVersion + "\"}," +
                "{\"key\":\"resolution\",\"value\":[" + screenWidth + "," + screenHeight + "]}";

        return new String(Base64.encodeToString(jsonString.getBytes(), Base64.NO_WRAP));
    }

    public synchronized static String uniqueID(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
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
        String packageName = context.getPackageName();
        PackageManager packageManager = context.getPackageManager();
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
        try {
            check2 = new File("/system/app/Superuser.apk").exists();
        } catch (Exception e) {
            check2 = false;
        }

        boolean check3;
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            check3 = in.readLine() != null;
        } catch (Exception e) {
            check3 = false;
        }

        return Boolean.toString(check1 || check2 || check3);
    }

    private static String getTimezone() {
        long minutes = TimeUnit.MINUTES.convert(TimeZone.getDefault().getRawOffset(),
                TimeUnit.MILLISECONDS);
        if (minutes % 60 == 0) {
            return (minutes / 60) + "";
        }
        BigDecimal decimalValue = new BigDecimal(minutes)
                .setScale(2, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal decHours = decimalValue.divide(
                new BigDecimal(60),
                new MathContext(2))
                .setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return decHours.toString();
    }
}
