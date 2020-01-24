package com.seamlesspay.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * {@link PaymentMethodNonce} representing a credit or debit card.
 */
public class CardNonce extends PaymentMethodNonce implements Parcelable {

    private static final String EXPIRATION_DATE_KEY = "expDate";
    private String mExpDate;

    /**
     * Convert an API response to a {@link CardNonce}.
     *
     * @param json Raw JSON response from Seamlesspay of a {@link CardNonce}.
     * @return {@link CardNonce}.
     * @throws JSONException when parsing the response fails.
     */
    public static CardNonce fromJson(String json) throws JSONException {
        CardNonce cardNonce = new CardNonce();
        JSONObject jsonObject = new JSONObject(json);
        cardNonce.fromJson(jsonObject);
        return cardNonce;
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
    }

    /**
     * @return Expiration Date of the card.
     */
    public String getExpirationDate() {
        return mExpDate;
    }

    public CardNonce() {}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mExpDate);
    }

    protected CardNonce(Parcel in) {
        super(in);
        mExpDate = in.readString();
    }

    public static final Creator<CardNonce> CREATOR = new Creator<CardNonce>() {
        public CardNonce createFromParcel(Parcel source) {
            return new CardNonce(source);
        }

        public CardNonce[] newArray(int size) {
            return new CardNonce[size];
        }
    };
}