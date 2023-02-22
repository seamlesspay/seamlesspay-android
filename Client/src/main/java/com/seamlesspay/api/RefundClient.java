package com.seamlesspay.api;

import com.seamlesspay.api.interfaces.HttpResponseCallback;
import com.seamlesspay.api.interfaces.RefundTokenCallback;
import com.seamlesspay.api.internal.AppHelper;
import com.seamlesspay.api.models.RefundBuilder;
import com.seamlesspay.api.models.RefundToken;
import org.json.JSONException;
import org.json.JSONObject;

class RefundClient {
	static final String REFUND_ENDPOINT = "refunds";

	static void create(
			final SeamlesspayFragment fragment,
			final RefundBuilder refundBuilder,
			final RefundTokenCallback callback
	) {
		createRest(fragment, refundBuilder, callback);
	}

	private static void createRest(
			final SeamlesspayFragment fragment,
			final RefundBuilder refundBuilder,
			final RefundTokenCallback callback
	) {
		String data = refundBuilder.build();

		try {
			JSONObject dataJson = new JSONObject(data);

			dataJson.put(
					"deviceFingerprint",
					AppHelper.getDeviceFingerprint(fragment.getContext())
			);

			data = dataJson.toString();
		} catch (JSONException ignored) {}

		fragment
				.getApiHttpClient()
				.post(REFUND_ENDPOINT,
						data,
						new HttpResponseCallback() {

							@Override
							public void success(String responseBody) {
								try {
									callback.success(RefundToken.parseRefundToken(responseBody));
								} catch (JSONException e) {
									callback.failure(e);
								}
							}

							@Override
							public void failure(Exception exception) {
								callback.failure(exception);
							}
						}
				);
	}
}
