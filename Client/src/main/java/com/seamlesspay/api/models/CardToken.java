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

/**
 * {@link PaymentMethodToken} representing a credit or debit card.
 */
public class CardToken extends PaymentMethodToken implements Parcelable {
  private static final String CARD_BRAND_KEY = "cardBrand";
  private static final String EXPIRATION_DATE_KEY = "expDate";

  private String mCardBrand;
  private String mExpDate;

  /**
   * Convert an API response to a {@link CardToken}.
   *
   * @param json Raw JSON response from Seamlesspay of a {@link CardToken}.
   * @return {@link CardToken}.
   * @throws JSONException when parsing the response fails.
   */
  public static CardToken fromJson(String json) throws JSONException {
    CardToken cardToken = new CardToken();
    JSONObject jsonObject = new JSONObject(json);

    cardToken.fromJson(jsonObject);

    return cardToken;
  }

  /**
   * Populate properties with values from a {@link JSONObject} .
   *
   * @param json {@link JSONObject}
   * @throws JSONException when parsing fails.
   */
  protected void fromJson(JSONObject json) throws JSONException {
    super.fromJson(json);

    mExpDate = json.getString(EXPIRATION_DATE_KEY);

    try {
      mCardBrand = json.getString(CARD_BRAND_KEY);
    } catch (JSONException ex) {
      mCardBrand = null;
    }
  }

  /**
   * @return Expiration Date of the card.
   */
  public String getExpirationDate() {
    return mExpDate;
  }

  /**
   * @return Card Brand Name.
   */
  public String getCardBrand() {
    return mCardBrand;
  }

  public CardToken() {}

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);

    dest.writeString(mExpDate);
    dest.writeString(mCardBrand);
  }

  protected CardToken(Parcel in) {
    super(in);
    mExpDate = in.readString();
    mCardBrand = in.readString();
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
