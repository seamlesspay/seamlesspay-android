package com.seamlesspay.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class Settings {

    private static final String SANDBOX_TOKENIZATION_KEY = "sk_XXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final String PRODUCTION_TOKENIZATION_KEY = "sk_XXXXXXXXXXXXXXXXXXXXXXXXXX";

    private static final String SANDBOX_SECRET_KEY = "sk_XXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final String PRODUCTION_SECRET_KEY = "sk_XXXXXXXXXXXXXXXXXXXXXXXXXX";

    protected static final String ENVIRONMENT = "environment";
    private static SharedPreferences sSharedPreferences;

    public static SharedPreferences getPreferences(Context context) {
        if (sSharedPreferences == null) {
            sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        }
        return sSharedPreferences;
    }

    public static int getEnvironment(Context context) {
        return getPreferences(context).getInt(ENVIRONMENT, 0);
    }

    public static void setEnvironment(Context context, int environment) {
        getPreferences(context)
                .edit()
                .putInt(ENVIRONMENT, environment)
                .apply();
    }

    public static String getEnvironmentName(Context context) {
        int environment = getEnvironment(context);
        if (environment == 0) {
            return "sandbox";
        } else {
            return "production";
        }
    }

    public static String getEnvironmentTokenizationKey(Context context) {
        int environment = getEnvironment(context);
        if (environment == 0) {
            return SANDBOX_TOKENIZATION_KEY;
        } else if (environment == 1) {
            return PRODUCTION_TOKENIZATION_KEY;
        } else {
            return "";
        }
    }

    public static String getEnvironmentSecretKey(Context context) {
        int environment = getEnvironment(context);
        if (environment == 0) {
            return SANDBOX_SECRET_KEY;
        } else if (environment == 1) {
            return PRODUCTION_SECRET_KEY;
        } else {
            return "";
        }
    }
}
