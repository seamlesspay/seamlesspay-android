package com.seamlesspay.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;


public class ChargeNonce extends BaseChargeNonce implements Parcelable {

    public ChargeNonce() {}

    public static ChargeNonce fromJson(String json) throws JSONException {
        ChargeNonce chargeNonce = new ChargeNonce();
        JSONObject jsonObject = new JSONObject(json);
        chargeNonce.fromJson(jsonObject);
        return chargeNonce;
    }

    /**
     * Populate properties with values from a {@link JSONObject} .
     *
     * @param json {@link JSONObject}
     * @throws JSONException when parsing fails.
     */
    protected void fromJson(JSONObject json) throws JSONException {
        super.fromJson(json);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected ChargeNonce(Parcel in) {
        super(in);
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
