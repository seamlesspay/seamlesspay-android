package com.seamlesspay.api.models;

import android.os.Parcel;
import android.os.Parcelable;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Builder used to construct a card tokenization request.
 */
public class CardBuilder extends BaseCardBuilder<CardBuilder> implements Parcelable {

    public CardBuilder() {
    }

    @Override
    protected void build(JSONObject json) throws JSONException {
        super.build(json);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected CardBuilder(Parcel in) {
        super(in);
    }

    public static final Creator<CardBuilder> CREATOR = new Creator<CardBuilder>() {
        @Override
        public CardBuilder createFromParcel(Parcel in) {
            return new CardBuilder(in);
        }

        @Override
        public CardBuilder[] newArray(int size) {
            return new CardBuilder[size];
        }
    };
}