/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;
import com.seamlesspay.api.exceptions.InvalidArgumentException;

/**
 * Generic base class for SeamlessPay authorization
 */
public class Authorization implements Parcelable {

  private String mSecretKey;
  private String mEnvironment;

  Authorization(
    String environment,
    String secretKey
  )
    throws InvalidArgumentException {
    if (TextUtils.isEmpty(secretKey)) {
      throw new InvalidArgumentException("Authorization key was invalid");
    }

    mSecretKey = secretKey;
    mEnvironment = environment;
  }

  /**
   * Returns an {@link Authorization} of the correct type for a given authorizationKeyString,
   * authorizationApiKeyString,environmentString.
   *
   * @param secretKey Given string to transform into an Authorization}
   * @return {@link Authorization}
   * @throws InvalidArgumentException This method will throw this exception type if the string
   * passed does not meet any of the criteria supplied for AuthorizationKey and AuthorizationApiKey.
   */

  public static Authorization fromKeys(
      String environment,
      String secretKey
  )
    throws InvalidArgumentException {
    return new Authorization(
        environment,
        secretKey
    );
  }

  /**
   * @return the current  environment.
   */
  public String getEnvironment() {
    return mEnvironment;
  }

  /**
   * @return The authorization bearer string for API requests.
   */
  public String getBearer() {
    return mSecretKey;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(mEnvironment);
    parcel.writeString(mSecretKey);
  }

  public Authorization(Parcel in) {
    mEnvironment = in.readString();
    mSecretKey = in.readString();
  }

  public static final Creator<Authorization> CREATOR = new Creator<Authorization>() {

    public Authorization createFromParcel(Parcel source) {
      return new Authorization(source);
    }

    public Authorization[] newArray(int size) {
      return new Authorization[size];
    }
  };
}
