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

public class CardChargeBulder
  extends BaseChargeBuilder<CardChargeBulder>
  implements Parcelable {

  public CardChargeBulder() {}

  @Override
  protected void build(JSONObject json) throws JSONException {
    json.put(CVV_KEY, getCvv());
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
  }

  protected CardChargeBulder(Parcel in) {
    super(in);
  }

  public static final Creator<CardChargeBulder> CREATOR = new Creator<CardChargeBulder>() {

    @Override
    public CardChargeBulder createFromParcel(Parcel in) {
      return new CardChargeBulder(in);
    }

    @Override
    public CardChargeBulder[] newArray(int size) {
      return new CardChargeBulder[size];
    }
  };
}
