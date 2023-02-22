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

public abstract class BaseChargeBuilder<T> implements Parcelable {

  public final class Keys {
    public static final String CREDENTIAL_INDICATOR_INITIAL = "Initial";
    public static final String CREDENTIAL_INDICATOR_SUBSEQUENT = "Subsequent";
    public static final String CURRENCY_CAD = "CAD";
    public static final String CURRENCY_USD = "USD";
    public static final String SCHEDULE_INDICATOR_SCHEDULED = "Scheduled";
    public static final String SCHEDULE_INDICATOR_UNSCHEDULED = "Unscheduled";
    public static final String TXN_ENV_CARD_ON_FILE = "card_on_file";
    public static final String TXN_ENV_ECOMMERCE_INTERNET = "ecommerce";
    public static final String TXN_ENV_RECURRING = "recurring";
    public static final String TXN_ENV_KEYED = "keyed";
    public static final String TXN_INITIATION_CUSTOMER = "Customer";
    public static final String TXN_INITIATION_MERCHANT = "Merchant";
    public static final String TXN_INITIATION_TERMINAL = "Terminal";
  }

  protected static final String AMOUNT_KEY = "amount";
  protected static final String CAPTURE_KEY = "capture";
  protected static final String RECURRING_KEY = "recurring";
  protected static final String CURRENCY_KEY = "currency";
  protected static final String CVV_KEY = "cvv";
  protected static final String DESCRIPTION_KEY = "description";
  protected static final String DESCRIPTOR_KEY = "descriptor";
  protected static final String IDEMPOTENCY_KEY = "idempotencyKey";
  protected static final String METADATA_KEY = "metadata";
  protected static final String PO_NUMBER_KEY = "poNumber";
  protected static final String SURCHARGE_FEE_AMOUNT_KEY = "surchargeFeeAmount";
  protected static final String ORDER_KEY = "order";
  protected static final String ORDER_ID_KEY = "orderID";
  protected static final String TOKEN_KEY = "token";
  protected static final String TAX_AMOUNT_KEY = "taxAmount";
  protected static final String TIP_KEY = "tip";
  protected static final String TXN_ENV_KEY = "entryType";
  protected static final String TXN_INITIATION_KEY = "transactionInitiation";
  protected static final String SCHEDULE_INDICATOR_KEY = "scheduleIndicator";
  protected static final String CREDENTIAL_INDICATOR_KEY =
    "credentialIndicator";
  protected static final String TAX_EXEMPT_KEY = "taxExempt";

  private Boolean mCapture = false;
  private Boolean mRecurring = false;
  private Boolean mTaxExempt = false;
  private JSONObject mMetadata;
  private JSONObject mOrder;
  private String mAmount;
  private String mCredentialIndicator;
  private String mCurrency = Keys.CURRENCY_USD;
  private String mCvv;
  private String mDescription;
  private String mDescriptort;
  private String mIdempotencyKey;
  private String mOrderID;
  private String mPoNumber;
  private String mScheduleIndicator;
  private String mSurchargeFeeAmount;
  private String mTaxAmount;
  private String mTip;
  private String mToken;
  private String mTransactionInitiation;
  private String mTxnEnv;

  public BaseChargeBuilder() {}

  /**
   * @return String representation for API use.
   */
  public String build() {
    JSONObject base = new JSONObject();

    try {
      base.put(TOKEN_KEY, mToken);
      base.put(AMOUNT_KEY, mAmount);
      base.put(CURRENCY_KEY, mCurrency);
      base.put(CAPTURE_KEY, mCapture);
      base.put(RECURRING_KEY, mRecurring);
      base.put(TAX_EXEMPT_KEY, mTaxExempt);

      if (mDescription != null) {
        base.put(DESCRIPTION_KEY, mDescription);
      }

      if (mDescription != null) {
        base.put(DESCRIPTION_KEY, mDescription);
      }

      if (mDescriptort != null) {
        base.put(DESCRIPTOR_KEY, mDescriptort);
      }

      if (mIdempotencyKey != null) {
        base.put(IDEMPOTENCY_KEY, mIdempotencyKey);
      }

      if (mPoNumber != null) {
        base.put(PO_NUMBER_KEY, mPoNumber);
      }

      if (mSurchargeFeeAmount != null) {
        base.put(SURCHARGE_FEE_AMOUNT_KEY, mSurchargeFeeAmount);
      }

      if (mOrderID != null) {
        base.put(ORDER_ID_KEY, mOrderID);
      }

      if (mTaxAmount != null) {
        base.put(TAX_AMOUNT_KEY, mTaxAmount);
      }

      if (mTip != null) {
        base.put(TIP_KEY, mTip);
      }

      if (mTxnEnv != null) {
        base.put(TXN_ENV_KEY, mTxnEnv);
      }

      if (mTransactionInitiation != null) {
        base.put(TXN_INITIATION_KEY, mTransactionInitiation);
      }

      if (mScheduleIndicator != null) {
        base.put(SCHEDULE_INDICATOR_KEY, mScheduleIndicator);
      }

      if (mCredentialIndicator != null) {
        base.put(CREDENTIAL_INDICATOR_KEY, mCredentialIndicator);
      }

      if (mMetadata != null && mMetadata.length() != 0) {
        base.put(METADATA_KEY, mMetadata.toString());
      }

      if (mOrder != null && mOrder.length() != 0) {
        base.put(ORDER_KEY, mOrder);
      }

      build(base);
    } catch (JSONException ignored) {}

    return base.toString();
  }

  protected abstract void build(JSONObject base) throws JSONException;

  @SuppressWarnings("unchecked")
  public T setAmount(String amount) {
    mAmount = amount;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setCapture(Boolean capture) {
    mCapture = capture;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setRecurring(Boolean recurring) {
    mRecurring = recurring;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setCurrency(String currency) {
    mCurrency = currency;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setCvv(String cvv) {
    mCvv = cvv;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setDescription(String description) {
    mDescription = description;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setDescriptort(String descriptort) {
    mDescriptort = descriptort;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setIdempotencyKey(String idempotencyKey) {
    mIdempotencyKey = idempotencyKey;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setMetadata(JSONObject metadata) {
    mMetadata = metadata;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setPoNumber(String poNumber) {
    mPoNumber = poNumber;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setSurchargeFeeAmount(String surchargeFeeAmount) {
    mSurchargeFeeAmount = surchargeFeeAmount;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setOrder(JSONObject order) {
    mOrder = order;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setOrderID(String orderID) {
    mOrderID = orderID;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setToken(String token) {
    mToken = token;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setTaxAmount(String taxAmount) {
    mTaxAmount = taxAmount;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setTip(String tip) {
    mTip = tip;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setTxnEnv(String txnEnv) {
    mTxnEnv = txnEnv;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setCredentialIndicator(String credentialIndicator) {
    mCredentialIndicator = credentialIndicator;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setScheduleIndicator(String scheduleIndicator) {
    mScheduleIndicator = scheduleIndicator;

    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T setTaxExempt(Boolean taxExempt) {
    mTaxExempt = taxExempt;
    return (T) this;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  protected BaseChargeBuilder(Parcel in) {
    mAmount = in.readString();
    mCurrency = in.readString();
    mCvv = in.readString();
    mDescription = in.readString();
    mDescriptort = in.readString();
    mIdempotencyKey = in.readString();
    mPoNumber = in.readString();
    mSurchargeFeeAmount = in.readString();
    mOrderID = in.readString();
    mToken = in.readString();
    mTaxAmount = in.readString();
    mTip = in.readString();
    mTxnEnv = in.readString();
    mTransactionInitiation = in.readString();
    mScheduleIndicator = in.readString();
    mCredentialIndicator = in.readString();
    mCapture = in.readByte() > 0;
    mRecurring = in.readByte() > 0;
    mTaxExempt = in.readByte() > 0;

    try {
      mMetadata = new JSONObject(in.readString());
    } catch (JSONException ex) {
      mMetadata = new JSONObject();
    }

    try {
      mOrder = new JSONObject(in.readString());
    } catch (JSONException ex) {
      mOrder = new JSONObject();
    }
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mAmount);
    dest.writeString(mCurrency);
    dest.writeString(mCvv);
    dest.writeString(mDescription);
    dest.writeString(mDescriptort);
    dest.writeString(mIdempotencyKey);
    dest.writeString(mPoNumber);
    dest.writeString(mSurchargeFeeAmount);
    dest.writeString(mOrderID);
    dest.writeString(mToken);
    dest.writeString(mTaxAmount);
    dest.writeString(mTip);
    dest.writeString(mTxnEnv);
    dest.writeString(mTransactionInitiation);
    dest.writeString(mScheduleIndicator);
    dest.writeString(mCredentialIndicator);
    dest.writeByte(mCapture ? (byte) 1 : 0);
    dest.writeByte(mRecurring ? (byte) 1 : 0);
    dest.writeByte(mTaxExempt ? (byte) 1 : 0);
    dest.writeString(mMetadata.toString());
    dest.writeString(mOrder.toString());
  }

  public String getCvv() {
    return mCvv;
  }

  public String getDescription() {
    return mDescription;
  }

  public String getDescriptort() {
    return mDescriptort;
  }

  public String getAmount() {
    return mAmount;
  }

  public String getCurrency() {
    return mCurrency;
  }

  public String getIdempotencyKey() {
    return mIdempotencyKey;
  }

  public String getPoNumber() {
    return mPoNumber;
  }

  public String getSurchargeFeeAmount() {
    return mSurchargeFeeAmount;
  }

  public String getOrderID() {
    return mOrderID;
  }

  public String getToken() {
    return mToken;
  }

  public String getTaxAmount() {
    return mTaxAmount;
  }

  public String getTip() {
    return mTip;
  }

  public String getTxnEnv() {
    return mTxnEnv;
  }

  public String getTransactionInitiation() {
    return mTransactionInitiation;
  }

  public String getCredentialIndicator() {
    return mCredentialIndicator;
  }

  public Boolean getCapture() {
    return mCapture;
  }

  public Boolean getRecurring() {
    return mRecurring;
  }

  public Boolean getTaxExempt() {
    return mTaxExempt;
  }

  public JSONObject getMetadata() {
    return mMetadata;
  }

  public JSONObject getOrder() {
    return mOrder;
  }
}
