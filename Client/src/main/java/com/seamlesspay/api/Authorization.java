/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;
import com.seamlesspay.api.exceptions.InvalidArgumentException;

/**
 * Generic base class for SeamlessPay authorization
 */
public class Authorization implements Parcelable {
  private String mAuthorizationApiKey;
  private String mAuthorizationKey;
  private String mEnvironment;

  Authorization(
    String environmentString,
    String authorizationKeyString,
    String authorizationApiKeyString
  )
    throws InvalidArgumentException {
    if (TextUtils.isEmpty(authorizationApiKeyString)) {
      throw new InvalidArgumentException("Authorization key was invalid");
    }

    mAuthorizationApiKey =
      new String(
        Base64.encodeToString(
          authorizationApiKeyString.getBytes(),
          Base64.NO_WRAP
        )
      );

    mAuthorizationKey =
      new String(
        Base64.encodeToString(authorizationKeyString.getBytes(), Base64.NO_WRAP)
      );

    mEnvironment = environmentString;
  }

  /**
   * Returns an {@link Authorization} of the correct type for a given authorizationKeyString,
   * authorizationApiKeyString,environmentString.
   *
   * @param authorizationKeyString Given string to transform into an Authorization}
   * @return {@link Authorization}
   * @throws InvalidArgumentException This method will throw this exception type if the string
   * passed does not meet any of the criteria supplied for AuthorizationKey and AuthorizationApiKey.
   */

  public static Authorization fromKeys(
    String environmentString,
    String authorizationKeyString
  )
    throws InvalidArgumentException {
    return Authorization.fromKeys(
      environmentString,
      authorizationKeyString,
      null
    );
  }

  public static Authorization fromKeys(
    String environmentString,
    String authorizationKeyString,
    String authorizationApiKeyString
  )
    throws InvalidArgumentException {
    if (
      TextUtils.isEmpty(authorizationApiKeyString) ||
      authorizationApiKeyString == null
    ) {
      authorizationApiKeyString = authorizationKeyString;
    }
    return new Authorization(
      environmentString,
      authorizationKeyString,
      authorizationApiKeyString
    );
  }

  /**
   * @return the current  environment.
   */
  public String getEnvironment() {
    return mEnvironment;
  }

  /**
   * @return The authorization bearer string for authorizing requests.
   */
  public String getBearer() {
    return mAuthorizationKey;
  }

  /**
   * @return The authorization bearer string for authorizing API requests.
   */
  public String getBearerApi() {
    return mAuthorizationApiKey;
  }

  /**
   * @return The original Authorization Key string, which can be used for serialization
   */
  @Override
  public String toString() {
    return new String(Base64.decode(mAuthorizationKey, Base64.DEFAULT));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(mAuthorizationKey);
    parcel.writeString(mAuthorizationApiKey);
    parcel.writeString(mEnvironment);
  }

  public Authorization(Parcel in) {
    mAuthorizationKey = in.readString();
    mAuthorizationApiKey = in.readString();
    mEnvironment = in.readString();
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
