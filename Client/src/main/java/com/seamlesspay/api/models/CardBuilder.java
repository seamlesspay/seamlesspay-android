package com.seamlesspay.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Builder used to construct a card tokenization request.
 */
public class CardBuilder extends BaseCardBuilder<CardBuilder> implements Parcelable {

    static final String VERIFICATION_KEY = "verification";
    static final String CVV_KEY = "cvv";
    Boolean mVerification = false;
    String mCvv;

    public CardBuilder() {
    }


    /**
     * @param cvv The card verification code (like CVV or CID). If you wish to create a CVV-only payment method to verify a card already stored in your Vault, omit all other properties to only collect CVV.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public CardBuilder cvv(String cvv) {
        if (TextUtils.isEmpty(cvv)) {
            mCvv = null;
        } else {
            mCvv = cvv;
        }
        return (CardBuilder) this;
    }

    /**
     * @param verification if true billingAddress, billingZip and cvv will be verified.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public CardBuilder verification(Boolean verification) {
        mVerification = verification;
        return (CardBuilder) this;
    }

    @Override
    protected void build(JSONObject json) throws JSONException {
        super.build(json);
        json.put(VERIFICATION_KEY, mVerification);
        json.put(CVV_KEY, mCvv);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mCvv);
        dest.writeByte(mVerification ? (byte) 1 : 0);
    }

    protected CardBuilder(Parcel in) {
        super(in);
        mCvv = in.readString();
        mVerification = in.readByte() > 0;
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