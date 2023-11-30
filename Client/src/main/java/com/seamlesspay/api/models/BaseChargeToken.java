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

public abstract class BaseChargeToken implements Parcelable {
  protected static final String AMOUNT_KEY = "amount";
  protected static final String AUTHCODE_KEY = "authCode";
  protected static final String BATCH_KEY = "batchId";
  protected static final String BUSINESSCARD_KEY = "businessCard";
  protected static final String PAYMENT_NETWORK_KEY = "paymentNetwork";
  protected static final String ACCOUNT_TYPE_KEY = "accountType";
  protected static final String CHARGEID_KEY = "id";
  protected static final String CURRENCY_KEY = "currency";
  private static final String VERIFICATION_OBJECT_KEY = "verification";
  private static final String AVS_RESULT_KEY = "addressLine1";
  private static final String CVV_RESULT_KEY = "cvv";
  private static final String POSTAL_CODE_RESULT_KEY = "addressPostalCode";
  protected static final String EXP_DATE_KEY = "expDate";
  protected static final String IP_ADDRESS_KEY = "ipAddress";
  protected static final String LASTFOUR_KEY = "lastFour";
  protected static final String ORDER_KEY = "order";
  protected static final String SURCHARGE_FEEAMOUNT_KEY = "surchargeFeeAmount";
  protected static final String TIP_KEY = "tip";
  protected static final String TOKEN_KEY = "token";
  protected static final String TXN_DATE_KEY = "transactionDate";
  protected static final String TXN_METHOD_KEY = "method";
  protected static final String TXN_STATUS_DESCRIPTION_KEY =
    "statusDescription";
  protected static final String TXN_STATUS_KEY = "status";
  protected static final String TXN_STATUSCODE_KEY = "statusCode";

  private String mAmount;
  private String mAuthCode;
  private String mAvsResult;
  private String mBatch;
  private String mBusinessCard;
  private String mPaymentNetwork;
  private String mAccountType;
  private String mChargeId;
  private String mCurrency;
  private String mCvvResult;
  private String mPostalCodeResult;
  private String mExpDate;
  private String mIpAddress;
  private String mLastFour;
  private String mMethod;
  private String mStatus;
  private String mStatusCode;
  private String mStatusDescription;
  private String mSurchargeFeeAmount;
  private String mTip;
  private String mToken;
  private String mTxnDate;
  private JSONObject mOrder = new JSONObject();

  public BaseChargeToken() {}

  protected void fromJson(JSONObject json) throws JSONException {
    mChargeId = json.getString(CHARGEID_KEY);
    mAmount = json.getString(AMOUNT_KEY);
    mAuthCode = json.getString(AUTHCODE_KEY);
    mMethod = json.getString(TXN_METHOD_KEY);
    mCurrency = json.getString(CURRENCY_KEY);
    mToken = json.getString(TOKEN_KEY);
    mStatusDescription = json.getString(TXN_STATUS_DESCRIPTION_KEY);
    mStatusCode = json.getString(TXN_STATUSCODE_KEY);
    mStatus = json.getString(TXN_STATUS_KEY);
    mTxnDate = json.getString(TXN_DATE_KEY);

    try {
      mIpAddress = json.getString(IP_ADDRESS_KEY);
    } catch (JSONException ex) {
      mIpAddress = null;
    }

    try {
      mPaymentNetwork = json.getString(PAYMENT_NETWORK_KEY);
    } catch (JSONException ex) {
      mPaymentNetwork = null;
    }

    try {
      mExpDate = json.getString(EXP_DATE_KEY);
    } catch (JSONException ex) {
      mExpDate = null;
    }

    try {
      mLastFour = json.getString(LASTFOUR_KEY);
    } catch (JSONException ex) {
      mLastFour = null;
    }

    try {
      mAccountType = json.getString(ACCOUNT_TYPE_KEY);
    } catch (JSONException ex) {
      mAccountType = null;
    }

    try {
      mTip = json.getString(TIP_KEY);
    } catch (JSONException ex) {
      mTip = null;
    }

    try {
      mSurchargeFeeAmount = json.getString(SURCHARGE_FEEAMOUNT_KEY);
    } catch (JSONException ex) {
      mSurchargeFeeAmount = null;
    }

    try {
      mBatch = json.getString(BATCH_KEY);
    } catch (JSONException ex) {
      mBatch = null;
    }

    try {
      mBusinessCard = json.getString(BUSINESSCARD_KEY);
    } catch (JSONException ex) {
      mBusinessCard = null;
    }

    try {
      mOrder = json.getJSONObject(ORDER_KEY);
    } catch (JSONException ex) {}

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

  public static BaseChargeToken parseChargeToken(String json)
    throws JSONException {
    return parseChargeToken(new JSONObject(json));
  }

  public static BaseChargeToken parseChargeToken(JSONObject json)
    throws JSONException {
    ChargeToken chargeToken = new ChargeToken();
    chargeToken.fromJson(json);
    return chargeToken;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mAuthCode);
    dest.writeString(mAvsResult);
    dest.writeString(mChargeId);
    dest.writeString(mCvvResult);
    dest.writeString(mPostalCodeResult);
    dest.writeString(mAmount);
    dest.writeString(mCurrency);
    dest.writeString(mPaymentNetwork);
    dest.writeString(mExpDate);
    dest.writeString(mAccountType);
    dest.writeString(mIpAddress);
    dest.writeString(mLastFour);
    dest.writeString(mMethod);
    dest.writeString(mStatus);
    dest.writeString(mStatusCode);
    dest.writeString(mStatusDescription);
    dest.writeString(mSurchargeFeeAmount);
    dest.writeString(mToken);
    dest.writeString(mTip);
    dest.writeString(mTxnDate);
    dest.writeString(mBatch);
    dest.writeString(mBusinessCard);
    dest.writeString(mOrder.toString());
  }

  protected BaseChargeToken(Parcel in) {
    mAuthCode = in.readString();
    mAvsResult = in.readString();
    mChargeId = in.readString();
    mCvvResult = in.readString();
    mPostalCodeResult = in.readString();
    mAmount = in.readString();
    mCurrency = in.readString();
    mPaymentNetwork = in.readString();
    mExpDate = in.readString();
    mAccountType = in.readString();
    mIpAddress = in.readString();
    mLastFour = in.readString();
    mMethod = in.readString();
    mStatus = in.readString();
    mStatusCode = in.readString();
    mStatusDescription = in.readString();
    mSurchargeFeeAmount = in.readString();
    mToken = in.readString();
    mTip = in.readString();
    mTxnDate = in.readString();
    mBatch = in.readString();
    mBusinessCard = in.readString();

    try {
      mOrder = new JSONObject(in.readString());
    } catch (JSONException ex) {
      mOrder = new JSONObject();
    }
  }

  public JSONObject getOrder() {
    return mOrder;
  }

  public String getBusinessCard() {
    return mBusinessCard;
  }

  public String getBatchId() {
    return mBatch;
  }

  public String getTransactionDate() {
    return mTxnDate;
  }

  public String getTip() {
    return mTip;
  }

  public String getToken() {
    return mToken;
  }

  public String getSurchargeFeeAmount() {
    return mSurchargeFeeAmount;
  }

  public String getStatusDescription() {
    return mStatusDescription;
  }

  public String getStatusCode() {
    return mStatusCode;
  }

  public String getStatus() {
    return mStatus;
  }

  public String getMethod() {
    return mMethod;
  }

  public String getLastFour() {
    return mLastFour;
  }

  public String getIpAddress() {
    return mIpAddress;
  }

  public String getAccountType() {
    return mAccountType;
  }

  public String getPaymentNetwork() {
    return mPaymentNetwork;
  }

  public String getExpDate() {
    return mExpDate;
  }

  public String getCurrency() {
    return mCurrency;
  }

  public String getAmount() {
    return mAmount;
  }

  public String getCvvResult() {
    return mCvvResult;
  }

  public String getChargeId() {
    return mChargeId;
  }

  public String getAvsResult() {
    return mAvsResult;
  }

  public String getAuthCode() {
    return mAuthCode;
  }

  public String getPostalCodeResult() {
    return mPostalCodeResult;
  }
}
