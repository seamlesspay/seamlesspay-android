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
 * Builder used to construct a gift card tokenization request.
 */
public class GiftCardBuilder
  extends BaseCardBuilder<GiftCardBuilder>
  implements Parcelable {

  public GiftCardBuilder() {}


  @Override
  protected void build(JSONObject json) throws JSONException {
    super.build(json);

    json.put(TXN_TYPE_KEY, Keys.GIFT_CARD_TYPE);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
  }

  protected GiftCardBuilder(Parcel in) {
    super(in);
  }

  public static final Creator<GiftCardBuilder> CREATOR = new Creator<GiftCardBuilder>() {

    @Override
    public GiftCardBuilder createFromParcel(Parcel in) {
      return new GiftCardBuilder(in);
    }

    @Override
    public GiftCardBuilder[] newArray(int size) {
      return new GiftCardBuilder[size];
    }
  };
}
