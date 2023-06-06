package com.seamlesspay.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

public class CardVerifyBuilder implements Parcelable {
	protected static final String CAPTURE_KEY = "capture";
	protected static final String DESCRIPTION_KEY = "description";
	protected static final String DESCRIPTOR_KEY = "descriptor";
	protected static final String IDEMPOTENCY_KEY = "idempotencyKey";
	protected static final String METADATA_KEY = "metadata";
	protected static final String PO_NUMBER_KEY = "poNumber";
	protected static final String ORDER_KEY = "order";
	protected static final String ORDER_ID_KEY = "orderID";
	protected static final String TOKEN_KEY = "token";
	protected static final String TXN_ENV_KEY = "entryType";
	protected static final String TXN_INITIATION_KEY = "transactionInitiation";
	protected static final String SCHEDULE_INDICATOR_KEY = "scheduleIndicator";
	protected static final String CREDENTIAL_INDICATOR_KEY =
			"credentialIndicator";
	protected static final String TAX_EXEMPT_KEY = "taxExempt";

	private Boolean mTaxExempt = false;
	private JSONObject mMetadata;
	private JSONObject mOrder;
	private String mCredentialIndicator;
	private String mCvv;
	private String mDescription;
	private String mDescriptort;
	private String mIdempotencyKey;
	private String mOrderID;
	private String mPoNumber;
	private String mScheduleIndicator;
	private String mToken;
	private String mTransactionInitiation;
	private String mTxnEnv;

	public CardVerifyBuilder() {}

	public static final Creator<CardVerifyBuilder> CREATOR = new Creator<CardVerifyBuilder>() {
		@Override
		public CardVerifyBuilder createFromParcel(Parcel in) {
			return new CardVerifyBuilder(in);
		}

		@Override
		public CardVerifyBuilder[] newArray(int size) {
			return new CardVerifyBuilder[size];
		}
	};

	/**
	 * @return String representation for API use.
	 */
	public String build() {
		JSONObject base = new JSONObject();

		try {
			base.put(TOKEN_KEY, mToken);
			base.put(CAPTURE_KEY, false);
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

			if (mOrderID != null) {
				base.put(ORDER_ID_KEY, mOrderID);
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
		} catch (JSONException ignored) {}

		return base.toString();
	}

	@SuppressWarnings("unchecked")
	public CardVerifyBuilder setCvv(String cvv) {
		mCvv = cvv;

		return (CardVerifyBuilder) this;
	}

	@SuppressWarnings("unchecked")
	public CardVerifyBuilder setDescription(String description) {
		mDescription = description;

		return (CardVerifyBuilder) this;
	}

	@SuppressWarnings("unchecked")
	public CardVerifyBuilder setDescriptort(String descriptort) {
		mDescriptort = descriptort;

		return (CardVerifyBuilder) this;
	}

	@SuppressWarnings("unchecked")
	public CardVerifyBuilder setIdempotencyKey(String idempotencyKey) {
		mIdempotencyKey = idempotencyKey;

		return (CardVerifyBuilder) this;
	}

	@SuppressWarnings("unchecked")
	public CardVerifyBuilder setMetadata(JSONObject metadata) {
		mMetadata = metadata;

		return (CardVerifyBuilder) this;
	}

	@SuppressWarnings("unchecked")
	public CardVerifyBuilder setPoNumber(String poNumber) {
		mPoNumber = poNumber;

		return (CardVerifyBuilder) this;
	}

	@SuppressWarnings("unchecked")
	public CardVerifyBuilder setOrder(JSONObject order) {
		mOrder = order;

		return (CardVerifyBuilder) this;
	}

	@SuppressWarnings("unchecked")
	public CardVerifyBuilder setOrderID(String orderID) {
		mOrderID = orderID;

		return (CardVerifyBuilder) this;
	}

	@SuppressWarnings("unchecked")
	public CardVerifyBuilder setToken(String token) {
		mToken = token;

		return (CardVerifyBuilder) this;
	}

	@SuppressWarnings("unchecked")
	public CardVerifyBuilder setTxnEnv(String txnEnv) {
		mTxnEnv = txnEnv;

		return (CardVerifyBuilder) this;
	}

	@SuppressWarnings("unchecked")
	public CardVerifyBuilder setCredentialIndicator(String credentialIndicator) {
		mCredentialIndicator = credentialIndicator;

		return (CardVerifyBuilder) this;
	}

	@SuppressWarnings("unchecked")
	public CardVerifyBuilder setScheduleIndicator(String scheduleIndicator) {
		mScheduleIndicator = scheduleIndicator;

		return (CardVerifyBuilder) this;
	}

	@SuppressWarnings("unchecked")
	public CardVerifyBuilder setTaxExempt(Boolean taxExempt) {
		mTaxExempt = taxExempt;
		return (CardVerifyBuilder) this;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	protected CardVerifyBuilder(Parcel in) {
		mCvv = in.readString();
		mDescription = in.readString();
		mDescriptort = in.readString();
		mIdempotencyKey = in.readString();
		mPoNumber = in.readString();
		mOrderID = in.readString();
		mToken = in.readString();
		mTxnEnv = in.readString();
		mTransactionInitiation = in.readString();
		mScheduleIndicator = in.readString();
		mCredentialIndicator = in.readString();
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
		dest.writeString(mCvv);
		dest.writeString(mDescription);
		dest.writeString(mDescriptort);
		dest.writeString(mIdempotencyKey);
		dest.writeString(mPoNumber);
		dest.writeString(mOrderID);
		dest.writeString(mToken);
		dest.writeString(mTxnEnv);
		dest.writeString(mTransactionInitiation);
		dest.writeString(mScheduleIndicator);
		dest.writeString(mCredentialIndicator);
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

	public String getIdempotencyKey() {
		return mIdempotencyKey;
	}

	public String getPoNumber() {
		return mPoNumber;
	}

	public String getOrderID() {
		return mOrderID;
	}

	public String getToken() {
		return mToken;
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
