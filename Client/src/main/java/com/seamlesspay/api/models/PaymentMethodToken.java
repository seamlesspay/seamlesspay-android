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
 * Base class representing a method of payment for a customer. {@link PaymentMethodToken} represents the
 * common interface of all payment methods, and can be handled by a server interchangeably.
 */
public abstract class PaymentMethodToken implements Parcelable {
  private static final String VERIFICATION_OBJECT_KEY = "verification";
  private static final String AVS_RESULT_KEY = "addressLine1";
  private static final String CVV_RESULT_KEY = "cvv";
  private static final String POSTAL_CODE_RESULT_KEY = "addressPostalCode";
  private static final String LAST_FOUR_KEY = "lastfour";
  private static final String NAME_KEY = "name";
  private static final String TOKEN_KEY = "token";
  private static final String TXN_TYPE_KEY = "paymentType";

  private String mAvsResult;
  private String mCvvResult;
  private String mPostalCodeResult;
  private String mInfo;
  private String mLastFour;
  private String mName;
  private String mToken;
  private String mTxnType;

  protected void fromJson(JSONObject json) throws JSONException {
    mLastFour = json.getString(LAST_FOUR_KEY);
    mToken = json.getString(TOKEN_KEY);
    mTxnType = json.getString(TXN_TYPE_KEY);

    try {
      mName = json.getString(NAME_KEY);
    } catch (JSONException ex) {
      mName = null;
    }

    try {
      JSONObject verification = json.getJSONObject(VERIFICATION_OBJECT_KEY);
      mAvsResult = verification.getString(AVS_RESULT_KEY);
      mCvvResult = verification.getString(CVV_RESULT_KEY);
      mPostalCodeResult = verification.getString(POSTAL_CODE_RESULT_KEY);
    } catch (JSONException ex) {
      mAvsResult = null;
      mCvvResult = null;
      mPostalCodeResult = null;
    }
  }

  /**
   * @return Payment type.
   */
  public String getTxnType() {
    return mTxnType;
  }

  /**
   * @return Last four digits of the card.
   */
  public String getLastFour() {
    return mLastFour;
  }

  /**
   * @return Token of the card.
   */
  public String getToken() {
    return mToken;
  }

  /**
   * @return Name of the card.
   */
  public String getName() {
    return mName;
  }

  /**
   * @return AVS Result. Enum: "pass" "fail" "unchecked" "unsupported" "retry".
   */
  public String getAvsResult() {
    return mAvsResult;
  }

  /**
   * @return CVV Result. Enum: "pass" "fail" "unchecked" "unsupported" "retry".
   */
  public String getCvvResult() {
    return mCvvResult;
  }

  /**
   * @return Postal Code Result. Enum: "pass" "fail" "unchecked" "unsupported" "retry".
   */
  public String getPostalCodeResult() {
    return mPostalCodeResult;
  }

  /**
   * @return Info of the card.
   */
  public String getInfo() {
    return mInfo;
  }

  public void setInfo(String info) {
    mInfo = info;
  }

  /**
   * Parses a {@link PaymentMethodToken} from json.
   *
   * @param json {@link String} representation of a {@link PaymentMethodToken}.
   * @param type The {@link String} type of the {@link PaymentMethodToken}.
   * @return {@link PaymentMethodToken}
   * @throws JSONException
   */

  public static PaymentMethodToken parsePaymentMethodToken(
    String json,
    String type
  )
    throws JSONException {
    return parsePaymentMethodToken(new JSONObject(json), type);
  }

  /**
   * Parses a {@link PaymentMethodToken} from json.
   *
   * @param json {@link JSONObject} representation of a {@link PaymentMethodToken}.
   * @param type The {@link String} type of the {@link PaymentMethodToken}.
   * @return {@link PaymentMethodToken}
   * @throws JSONException
   */

  public static PaymentMethodToken parsePaymentMethodToken(
    JSONObject json,
    String type
  )
    throws JSONException {
    switch (type) {
      case PaymentMethodBuilder.Keys.PLDEBIT_CARD_TYPE:
      case PaymentMethodBuilder.Keys.CREDIT_CARD_TYPE:
        CardToken cardToken = new CardToken();

        cardToken.fromJson(json);

        return cardToken;
      default:
        return null;
    }
  }

  public PaymentMethodToken() {}

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mTxnType);
    dest.writeString(mLastFour);
    dest.writeString(mName);
    dest.writeString(mToken);
    dest.writeString(mInfo);
    dest.writeString(mAvsResult);
    dest.writeString(mCvvResult);
    dest.writeString(mPostalCodeResult);
  }

  protected PaymentMethodToken(Parcel in) {
    mTxnType = in.readString();
    mLastFour = in.readString();
    mName = in.readString();
    mToken = in.readString();
    mInfo = in.readString();
    mAvsResult = in.readString();
    mCvvResult = in.readString();
    mPostalCodeResult = in.readString();
  }
}
