/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

public class ChargeToken extends BaseChargeToken implements Parcelable {

  public ChargeToken() {}

  public static ChargeToken fromJson(String json) throws JSONException {
    ChargeToken chargeToken = new ChargeToken();
    JSONObject jsonObject = new JSONObject(json);

    chargeToken.fromJson(jsonObject);

    return chargeToken;
  }

  /**
   * Populate properties with values from a {@link JSONObject} .
   *
   * @param json {@link JSONObject}
   * @throws JSONException when parsing fails.
   */
  protected void fromJson(JSONObject json) throws JSONException {
    super.fromJson(json);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
  }

  protected ChargeToken(Parcel in) {
    super(in);
  }

  public static final Creator<CardToken> CREATOR = new Creator<CardToken>() {

    public CardToken createFromParcel(Parcel source) {
      return new CardToken(source);
    }

    public CardToken[] newArray(int size) {
      return new CardToken[size];
    }
  };
}
