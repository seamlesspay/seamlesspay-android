package com.seamlesspay.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Base class representing a method of payment for a customer. {@link PaymentMethodNonce} represents the
 * common interface of all payment method nonces, and can be handled by a server interchangeably.
 */
public abstract class PaymentMethodNonce implements Parcelable {

    private static final String TXN_TYPE_KEY = "txnType";
    private static final String LAST_FOUR_KEY = "lastfour";
    private static final String TOKEN_KEY = "token";
    private static final String NAME_KEY = "name";

    private String mTxnType;
    private String mLastFour;
    private String mName;
    private String mToken;
    private String mInfo;


    @CallSuper
    protected void fromJson(JSONObject json) throws JSONException {

        mTxnType = json.getString(TXN_TYPE_KEY);
        mLastFour = json.getString(LAST_FOUR_KEY);
        mToken = json.getString(TOKEN_KEY);
        try {
            mName = json.getString(NAME_KEY);
        } catch (JSONException ex) {
            mName = null;
        }
    }

    /**
     * @return Last four digits of the card.
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
     * @return Info of the card.
     */
    public String getInfo() {
        return mInfo;
    }


    public void setInfo(String info) {
        mInfo = info;
    }


    /**
     * Parses a {@link PaymentMethodNonce} from json.
     *
     * @param json {@link String} representation of a {@link PaymentMethodNonce}.
     * @param type The {@link String} type of the {@link PaymentMethodNonce}.
     * @return {@link PaymentMethodNonce}
     * @throws JSONException
     */
    @Nullable
    public static PaymentMethodNonce parsePaymentMethodNonces(String json, String type) throws JSONException {
        return parsePaymentMethodNonces(new JSONObject(json), type);
    }

    /**
     * Parses a {@link PaymentMethodNonce} from json.
     *
     * @param json {@link JSONObject} representation of a {@link PaymentMethodNonce}.
     * @param type The {@link String} type of the {@link PaymentMethodNonce}.
     * @return {@link PaymentMethodNonce}
     * @throws JSONException
     */
    @Nullable
    public static PaymentMethodNonce parsePaymentMethodNonces(JSONObject json, String type) throws JSONException {
        switch (type) {
            case PaymentMethodBuilder.Keys.PLDEBIT_CARD_TYPE:
            case PaymentMethodBuilder.Keys.CREDIT_CARD_TYPE:
                    CardNonce cardNonce = new CardNonce();
                    cardNonce.fromJson(json);
                    return cardNonce;
            default:
                return null;
        }
    }

    public PaymentMethodNonce() {}

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
    }

    protected PaymentMethodNonce(Parcel in) {
        mTxnType = in.readString();
        mLastFour = in.readString();
        mName = in.readString();
        mToken = in.readString();
        mInfo = in.readString();
    }
}
