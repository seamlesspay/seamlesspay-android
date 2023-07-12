package com.seamlesspay.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

public class AdjustChargeBuilder implements Parcelable {
	protected static final String AMOUNT_KEY = "amount";
	protected static final String CAPTURE_KEY = "capture";

	private String mAmount;
	private Boolean mCapture;

	/**
	 * @return String representation for API use.
	 */
	public String build() {
		JSONObject base = new JSONObject();

		try {
			if (mAmount != null) {
				base.put(AMOUNT_KEY, mAmount);
			}
			if (mCapture != null) {
				base.put(CAPTURE_KEY, mCapture);
			}
		} catch (JSONException ignored) {}

		return base.toString();
	}

	public AdjustChargeBuilder setAmount(String amount) {
		mAmount = amount;

		return this;
	}

	public AdjustChargeBuilder setCapture(Boolean capture) {
		mCapture = capture;

		return this;
	}

	public AdjustChargeBuilder() {}

	protected AdjustChargeBuilder(Parcel in) {
		mAmount = in.readString();
	}

	public static final Creator<AdjustChargeBuilder> CREATOR = new Creator<AdjustChargeBuilder>() {
		@Override
		public AdjustChargeBuilder createFromParcel(Parcel in) {
			return new AdjustChargeBuilder(in);
		}

		@Override
		public AdjustChargeBuilder[] newArray(int size) {
			return new AdjustChargeBuilder[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mAmount);
	}
}
