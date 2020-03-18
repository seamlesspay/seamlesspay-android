/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

public class SeamlesspaySharedPreferences {

  public static SharedPreferences getSharedPreferences(Context context) {
    return context
      .getApplicationContext()
      .getSharedPreferences("SeamlesspayApi", Context.MODE_PRIVATE);
  }

  public static void putParcelable(
    Context context,
    String key,
    Parcelable parcelable
  ) {
    Parcel parcel = Parcel.obtain();

    parcelable.writeToParcel(parcel, 0);

    getSharedPreferences(context)
      .edit()
      .putString(key, Base64.encodeToString(parcel.marshall(), 0))
      .apply();
  }

  public static Parcel getParcelable(Context context, String key) {
    try {
      byte[] requestBytes = Base64.decode(
        getSharedPreferences(context).getString(key, ""),
        0
      );

      Parcel parcel = Parcel.obtain();

      parcel.unmarshall(requestBytes, 0, requestBytes.length);
      parcel.setDataPosition(0);

      return parcel;
    } catch (Exception ignored) {}

    return null;
  }

  public static void remove(Context context, String key) {
    getSharedPreferences(context).edit().remove(key).apply();
  }

  public static void putString(Context context, String key, String value) {
    getSharedPreferences(context).edit().putString(key, value).apply();
  }

  public static String getString(Context context, String key) {
    return getSharedPreferences(context).getString(key, "");
  }
}
