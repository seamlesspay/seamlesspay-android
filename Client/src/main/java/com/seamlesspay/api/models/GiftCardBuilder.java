package com.seamlesspay.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Builder used to construct a gift card tokenization request.
 */
public class GiftCardBuilder extends BaseCardBuilder<BaseCardBuilder> implements Parcelable {

    static final String PINNUMBER_KEY = "pinNumber";
    String mPinNumber;

    public GiftCardBuilder() {
    }

    /**
     * @param pinNumber The gift card number.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public BaseCardBuilder pinNumber(String pinNumber) {
        if (TextUtils.isEmpty(pinNumber)) {
            mPinNumber = null;
        } else {
            mPinNumber = pinNumber;
        }
        return (BaseCardBuilder) this;
    }

    @Override
    protected void build(JSONObject json) throws JSONException {
        super.build(json);
        json.put(PINNUMBER_KEY, mPinNumber);
        json.put(TXN_TYPE_KEY, Keys.GIFT_CARD_TYPE);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mPinNumber);
    }

    protected GiftCardBuilder(Parcel in) {
        super(in);
        mPinNumber = in.readString();
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
