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
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Builder used to construct a ACH tokenization request.
 */
public class AchCardBuilder
  extends BaseCardBuilder<AchCardBuilder>
  implements Parcelable {
  static final String BANK_ACCOUNT_TYPE_KEY = "bankAccountType";
  static final String BANK_ROUTING_NUMBER_KEY = "routingNumber";

  String mBankAccountType;
  String mRoutingNumber;

  public AchCardBuilder() {}

  /**
   * @param bankAccountType The ACH.
   * @return {@link AchCardBuilder}
   */
  @SuppressWarnings("unchecked")
  public AchCardBuilder pinNumber(String bankAccountType) {
    if (TextUtils.isEmpty(bankAccountType)) {
      mBankAccountType = null;
    } else {
      mBankAccountType = bankAccountType;
    }

    return (AchCardBuilder) this;
  }

  /**
   * @param routingNumber The ACH.
   * @return {@link AchCardBuilder}
   */
  @SuppressWarnings("unchecked")
  public AchCardBuilder routingNumber(String routingNumber) {
    if (TextUtils.isEmpty(routingNumber)) {
      mRoutingNumber = null;
    } else {
      mRoutingNumber = routingNumber;
    }

    return (AchCardBuilder) this;
  }

  @Override
  protected void build(JSONObject json) throws JSONException {
    super.build(json);

    json.put(BANK_ACCOUNT_TYPE_KEY, mBankAccountType);
    json.put(BANK_ROUTING_NUMBER_KEY, mRoutingNumber);
    json.put(TXN_TYPE_KEY, Keys.ACH_TYPE);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);

    dest.writeString(mBankAccountType);
    dest.writeString(mRoutingNumber);
  }

  protected AchCardBuilder(Parcel in) {
    super(in);
    mBankAccountType = in.readString();
    mRoutingNumber = in.readString();
  }

  public static final Creator<AchCardBuilder> CREATOR = new Creator<AchCardBuilder>() {

    @Override
    public AchCardBuilder createFromParcel(Parcel in) {
      return new AchCardBuilder(in);
    }

    @Override
    public AchCardBuilder[] newArray(int size) {
      return new AchCardBuilder[size];
    }
  };
}
