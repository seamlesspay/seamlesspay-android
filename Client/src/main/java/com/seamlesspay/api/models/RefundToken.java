package com.seamlesspay.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

public class RefundToken implements Parcelable {

	private static final String ACCOUNT_TYPE_KEY = "accountType";
	private static final String AMOUNT_KEY = "amount";
	private static final String AUTH_CODE_KEY = "authCode";
	private static final String BATCH_ID_KEY = "batchId";
	private static final String CREATED_AT_KEY = "createdAt";
	private static final String CURRENCY_KEY = "currency";
	private static final String ID_KEY = "id";
	private static final String IP_ADDRESS_KEY = "ipAddress";
	private static final String LAST_FOUR_KEY = "lastFour";
	private static final String METADATA_KEY = "metadata";
	private static final String PAYMENT_NETWORK_KEY = "paymentNetwork";
	private static final String STATUS_KEY = "status";
	private static final String STATUS_CODE_KEY = "statusCode";
	private static final String STATUS_DESCRIPTION_KEY = "statusDescription";
	private static final String TOKEN_KEY = "token";
	private static final String TRANSACTION_DATE_KEY = "transactionDate";
	private static final String UPDATED_AT_KEY = "updatedAt";

	private String mAccountType;
	private String mAmount;
	private String mAuthCode;
	private String mBatchId;
	private String mCreatedAt;
	private String mCurrency;
	private String mId;
	private String mIpAddress;
	private String mLastFour;
	private JSONObject mMetaData = new JSONObject();
	private String mPaymentNetwork;
	private String mStatus;
	private String mStatusCode;
	private String mStatusDescription;
	private String mToken;
	private String mTransactionDate;
	private String mUpdatedAt;

	public RefundToken() {}

	protected RefundToken(Parcel in) {
		mAccountType = in.readString();
		mAmount = in.readString();
		mAuthCode = in.readString();
		mBatchId = in.readString();
		mCreatedAt = in.readString();
		mCurrency = in.readString();
		mId = in.readString();
		mIpAddress = in.readString();
		mLastFour = in.readString();
		mPaymentNetwork = in.readString();
		mStatus = in.readString();
		mStatusCode = in.readString();
		mStatusDescription = in.readString();
		mToken = in.readString();
		mTransactionDate = in.readString();
		mUpdatedAt = in.readString();
		try {
			mMetaData = new JSONObject(in.readString());
		} catch (JSONException ex) {
			mMetaData = new JSONObject();
		}
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mAccountType);
		dest.writeString(mAmount);
		dest.writeString(mAuthCode);
		dest.writeString(mBatchId);
		dest.writeString(mCreatedAt);
		dest.writeString(mCurrency);
		dest.writeString(mId);
		dest.writeString(mIpAddress);
		dest.writeString(mLastFour);
		dest.writeString(mPaymentNetwork);
		dest.writeString(mStatus);
		dest.writeString(mStatusCode);
		dest.writeString(mStatusDescription);
		dest.writeString(mToken);
		dest.writeString(mTransactionDate);
		dest.writeString(mUpdatedAt);
		dest.writeString(mMetaData.toString());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<RefundToken> CREATOR = new Creator<RefundToken>() {
		@Override
		public RefundToken createFromParcel(Parcel in) {
			return new RefundToken(in);
		}

		@Override
		public RefundToken[] newArray(int size) {
			return new RefundToken[size];
		}
	};

	public void fromJson(JSONObject json) throws JSONException {

		try {
			mAmount = json.getString(AMOUNT_KEY);
		} catch (JSONException ex) {
			mAmount = null;
		}

		try {
			mAuthCode = json.getString(AUTH_CODE_KEY);
		} catch (JSONException ex) {
			mAuthCode = null;
		}

		try {
			mCurrency = json.getString(CURRENCY_KEY);
		} catch (JSONException ex) {
			mCurrency = null;
		}

		try {
			mIpAddress = json.getString(IP_ADDRESS_KEY);
		} catch (JSONException ex) {
			mIpAddress = null;
		}

		try {
			mToken = json.getString(TOKEN_KEY);
		} catch (JSONException ex) {
			mToken = null;
		}

		try {
			mStatusDescription = json.getString(STATUS_DESCRIPTION_KEY);
		} catch (JSONException ex) {
			mStatusDescription = null;
		}

		try {
			mStatusCode = json.getString(STATUS_CODE_KEY);
		} catch (JSONException ex) {
			mStatusCode = null;
		}

		try {
			mStatus = json.getString(STATUS_KEY);
		} catch (JSONException ex) {
			mStatus = null;
		}

		try {
			mTransactionDate = json.getString(TRANSACTION_DATE_KEY);
		} catch (JSONException ex) {
			mTransactionDate = null;
		}

		try {
			mUpdatedAt = json.getString(UPDATED_AT_KEY);
		} catch (JSONException ex) {
			mUpdatedAt = null;
		}

		try {
			mId = json.getString(ID_KEY);
		} catch (JSONException ex) {
			mId = null;
		}

		try {
			mCreatedAt = json.getString(CREATED_AT_KEY);
		} catch (JSONException ex) {
			mCreatedAt = null;
		}

		try {
			mPaymentNetwork = json.getString(PAYMENT_NETWORK_KEY);
		} catch (JSONException ex) {
			mPaymentNetwork = null;
		}


		try {
			mLastFour = json.getString(LAST_FOUR_KEY);
		} catch (JSONException ex) {
			mLastFour = null;
		}

		try {
			mAccountType = json.getString(ACCOUNT_TYPE_KEY);
		} catch (JSONException ex) {
			mAccountType = null;
		}

		try {
			mBatchId = json.getString(BATCH_ID_KEY);
		} catch (JSONException ex) {
			mBatchId = null;
		}


		try {
			mMetaData = json.getJSONObject(METADATA_KEY);
		} catch (JSONException ex) {}

	}

	public static RefundToken parseRefundToken(String json)
			throws JSONException {
		return parseRefundToken(new JSONObject(json));
	}

	public static RefundToken parseRefundToken(JSONObject json)
			throws JSONException {
		RefundToken chargeToken = new RefundToken();
		chargeToken.fromJson(json);
		return chargeToken;
	}

	public String getAccountType() {
		return mAccountType;
	}

	public String getAmount() {
		return mAmount;
	}

	public String getAuthCode() {
		return mAuthCode;
	}

	public String getBatchId() {
		return mBatchId;
	}

	public String getCreatedAt() {
		return mCreatedAt;
	}

	public String getCurrency() {
		return mCurrency;
	}

	public String getId() {
		return mId;
	}

	public String getIpAddress() {
		return mIpAddress;
	}

	public String getLastFour() {
		return mLastFour;
	}

	public JSONObject getMetaData() {
		return mMetaData;
	}

	public String getPaymentNetwork() {
		return mPaymentNetwork;
	}

	public String getStatus() {
		return mStatus;
	}

	public String getStatusCode() {
		return mStatusCode;
	}

	public String getStatusDescription() {
		return mStatusDescription;
	}

	public String getToken() {
		return mToken;
	}

	public String getTransactionDate() {
		return mTransactionDate;
	}

	public String getUpdatedAt() {
		return mUpdatedAt;
	}
}
