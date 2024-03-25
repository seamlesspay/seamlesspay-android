package com.seamlesspay.api.interfaces;

import com.seamlesspay.api.models.RefundToken;

/**
 * Communicates {@link RefundToken} from a HTTP request on the main thread.
 * One and only one method will be invoked in response to a request.
 */
public interface RefundTokenCallback {
	/**
	 * @param refundToken parsed {@link RefundToken} from the HTTP request.
	 */
	void success(RefundToken refundToken);

	/**
	 * @param exception error that caused the request to fail.
	 */
	void failure(Exception exception);
}
