/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.models;

import android.os.Parcel;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * An abstract class to extend when creating a builder for
 * a payment method. Contains logic and
 * implementations shared by all payment methods.
 */
public abstract class PaymentMethodBuilder<T> {

  public final class Keys {
    public static final String ACH_TYPE = "ACH";
    public static final String CREDIT_CARD_TYPE = "CREDIT_CARD";
    public static final String GIFT_CARD_TYPE = "GIFT_CARD";
    public static final String PLDEBIT_CARD_TYPE = "PLDEBIT_CARD";
  }

  protected static final String TXN_TYPE_KEY = "txnType";

  private String mTxnType;

  public PaymentMethodBuilder() {}

  /**
   * @param txnType PAN Vault support five types of payments "Credit Card",
   * "PINLess Debit Card", "ACH", "Gift Card"
   */
  @SuppressWarnings("unchecked")
  public T setTxnType(String txnType) {
    mTxnType = txnType;
    return (T) this;
  }

  /**
   * @return String representation of {@link PaymentMethodToken} for API use.
   */
  public String build() {
    JSONObject base = new JSONObject();

    try {
      base.put(TXN_TYPE_KEY, mTxnType);

      build(base);
    } catch (JSONException ignored) {}

    return base.toString();
  }

  protected PaymentMethodBuilder(Parcel in) {
    mTxnType = in.readString();
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mTxnType);
  }

  protected abstract void build(JSONObject base) throws JSONException;

  public String getResponsePaymentMethodType() {
    return mTxnType;
  }
}
