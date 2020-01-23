package com.seamlesspay.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

public abstract class BaseChargeNonce implements Parcelable {

    protected static final String AUTHCODE_KEY = "authCode";
    protected static final String AVS_MESSAGE_KEY = "avsMessage";
    protected static final String AVS_RESULT_KEY = "avsResult";
    protected static final String CHARGEID_KEY = "id";
    protected static final String CVV_RESULT_KEY = "cvvResult";
    protected static final String AMOUNT_KEY = "amount";
    protected static final String CURRENCY_KEY = "currency";
    protected static final String CARD_BRAND_KEY = "cardBrand";
    protected static final String CARD_TYPE_KEY = "cardType";
    protected static final String IP_ADDRESS_KEY = "ipAddress";
    protected static final String LASTFOUR_KEY = "lastFour";
    protected static final String TXN_METHOD_KEY = "method";
    protected static final String TXN_STATUS_KEY = "status";
    protected static final String TXN_STATUSCODE_KEY = "statusCode";
    protected static final String TXN_STATUS_DESCRIPTION_KEY = "statusDescription";
    protected static final String SURCHARGE_FEEAMOUNT_KEY = "surchargeFeeAmount";
    protected static final String TOKEN_KEY = "token";
    protected static final String TIP_KEY = "tip";
    protected static final String TXN_DATE_KEY = "txnDate";
    protected static final String BATCH_KEY = "batch";
    protected static final String ORDER_KEY = "order";
    protected static final String BUSINESSCARD_KEY = "businessCard";

    private String mAuthCode;
    private String mAvsMessage;
    private String mAvsResult;
    private String mChargeId;
    private String mCvvResult;
    private String mAmount;
    private String mCurrency;
    private String mCardBrand;
    private String mCardType;
    private String mIpAddress;
    private String mLastFour;
    private String mMethod;
    private String mStatus;
    private String mStatusCode;
    private String mStatusDescription;
    private String mSurchargeFeeAmount;
    private String mToken;
    private String mTip;
    private String mTxnDate;
    private String mBatch;
    private String mBusinessCard;
    private JSONObject mOrder = new JSONObject();

    public BaseChargeNonce() {}

    @CallSuper
    protected void fromJson(JSONObject json) throws JSONException {

        mChargeId = json.getString(CHARGEID_KEY);
        mAmount = json.getString(AMOUNT_KEY);
        mAuthCode = json.getString(AUTHCODE_KEY);
        mMethod = json.getString(TXN_METHOD_KEY);
        mAvsMessage = json.getString(AVS_MESSAGE_KEY);
        mAvsResult = json.getString(AVS_RESULT_KEY);
        mCurrency = json.getString(CURRENCY_KEY);
        mIpAddress = json.getString(IP_ADDRESS_KEY);
        mToken = json.getString(TOKEN_KEY);
        mStatusDescription = json.getString(TXN_STATUS_DESCRIPTION_KEY);
        mStatusCode = json.getString(TXN_STATUSCODE_KEY);
        mStatus = json.getString(TXN_STATUS_KEY);
        mTxnDate = json.getString(TXN_DATE_KEY);

        try {
            mCardBrand = json.getString(CARD_BRAND_KEY);
        } catch (JSONException ex) {
            mCardBrand = null;
        }

        try {
            mLastFour = json.getString(LASTFOUR_KEY);
        } catch (JSONException ex) {
            mLastFour = null;
        }

        try {
            mCvvResult = json.getString(CVV_RESULT_KEY);
        } catch (JSONException ex) {
            mCvvResult = null;
        }

        try {
            mCardType = json.getString(CARD_TYPE_KEY);
        } catch (JSONException ex) {
            mCardType = null;
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
        } catch (JSONException ex) {

        }
    }

    @Nullable
    public static BaseChargeNonce parseChargeNonces(String json) throws JSONException {
        return parseChargeNonces(new JSONObject(json));
    }


    @Nullable
    public static BaseChargeNonce parseChargeNonces(JSONObject json) throws JSONException {
                ChargeNonce chargeNonce = new ChargeNonce();
                chargeNonce.fromJson(json);
                return chargeNonce;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mAuthCode);
        dest.writeString(mAvsMessage);
        dest.writeString(mAvsResult);
        dest.writeString(mChargeId);
        dest.writeString(mCvvResult);
        dest.writeString(mAmount);
        dest.writeString(mCurrency);
        dest.writeString(mCardBrand);
        dest.writeString(mCardType);
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

    protected BaseChargeNonce(Parcel in) {

        mAuthCode = in.readString();
        mAvsMessage = in.readString();
        mAvsResult = in.readString();
        mChargeId = in.readString();
        mCvvResult = in.readString();
        mAmount = in.readString();
        mCurrency = in.readString();
        mCardBrand = in.readString();
        mCardType = in.readString();
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

    public String getBatch() {
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

    public String getCardType() {
        return mCardType;
    }

    public String getCardBrand() {
        return mCardBrand;
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

    public String getAvsMessage() {
        return mAvsMessage;
    }

    public String getAuthCode() {
        return mAuthCode;
    }

}
