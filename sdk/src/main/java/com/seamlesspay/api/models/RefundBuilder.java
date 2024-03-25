package com.seamlesspay.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

public class RefundBuilder implements Parcelable {

	public static final class Keys {
		public static final String CURRENCY_CAD = "CAD";
		public static final String CURRENCY_USD = "USD";
	}

	private static final String AMOUNT_KEY = "amount";
	private static final String CURRENCY_KEY = "currency";
	private static final String DESCRIPTOR_KEY = "descriptor";
	private static final String IDEMPOTENCY_KEY = "idempotencyKey";
	private static final String METADATA_KEY = "metadata";
	private static final String TOKEN_KEY = "token";

	private JSONObject mMetadata;
	private String mAmount;
	private String mCurrency = Keys.CURRENCY_USD;
	private String mDescriptor;
	private String mIdempotencyKey;
	private String mToken;

	public RefundBuilder(){}

	/**
	 * @return String representation for API use.
	 */
	public String build() {
		JSONObject base = new JSONObject();

		try {
			base.put(TOKEN_KEY, mToken);
			base.put(AMOUNT_KEY, mAmount);
			base.put(CURRENCY_KEY, mCurrency);


			if (mDescriptor != null) {
				base.put(DESCRIPTOR_KEY, mDescriptor);
			}

			if (mIdempotencyKey != null) {
				base.put(IDEMPOTENCY_KEY, mIdempotencyKey);
			}

			if (mMetadata != null && mMetadata.length() != 0) {
				base.put(METADATA_KEY, mMetadata.toString());
			}

		} catch (JSONException ignored) {}

		return base.toString();
	}

	protected RefundBuilder(Parcel in) {
		mAmount = in.readString();
		mCurrency = in.readString();
		mDescriptor = in.readString();
		mIdempotencyKey = in.readString();
		mToken = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mAmount);
		dest.writeString(mCurrency);
		dest.writeString(mDescriptor);
		dest.writeString(mIdempotencyKey);
		dest.writeString(mToken);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<RefundBuilder> CREATOR = new Creator<RefundBuilder>() {
		@Override
		public RefundBuilder createFromParcel(Parcel in) {
			return new RefundBuilder(in);
		}

		@Override
		public RefundBuilder[] newArray(int size) {
			return new RefundBuilder[size];
		}
	};

	public JSONObject getMetadata() {
		return mMetadata;
	}

	public RefundBuilder setMetadata(JSONObject mMetadata) {
		this.mMetadata = mMetadata;

		return this;
	}

	public String getAmount() {
		return mAmount;
	}

	public RefundBuilder setAmount(String mAmount) {
		this.mAmount = mAmount;

		return this;
	}

	public String getCurrency() {
		return mCurrency;
	}

	public RefundBuilder setCurrency(String mCurrency) {
		this.mCurrency = mCurrency;

		return this;
	}

	public String getDescriptor() {
		return mDescriptor;
	}

	public RefundBuilder setDescriptor(String mDescriptor) {
		this.mDescriptor = mDescriptor;

		return this;
	}

	public String getIdempotencyKey() {
		return mIdempotencyKey;
	}

	public RefundBuilder setIdempotencyKey(String mIdempotencyKey) {
		this.mIdempotencyKey = mIdempotencyKey;

		return this;
	}

	public String getToken() {
		return mToken;
	}

	public RefundBuilder setToken(String mToken) {
		this.mToken = mToken;

		return this;
	}
}
