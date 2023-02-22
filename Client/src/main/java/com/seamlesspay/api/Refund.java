package com.seamlesspay.api;

import com.seamlesspay.api.interfaces.RefundTokenCallback;
import com.seamlesspay.api.models.RefundBuilder;
import com.seamlesspay.api.models.RefundToken;

public class Refund {

	public static void create(
			final SeamlesspayFragment fragment,
			final RefundBuilder refundBuilder
	) {
		RefundClient.create(
				fragment,
				refundBuilder,
				new RefundTokenCallback() {

					@Override
					public void success(RefundToken refundToken) {
						fragment.postCallback(refundToken);
					}

					@Override
					public void failure(Exception exception) {
						fragment.postCallback(exception);
					}
				}
		);
	}
}
