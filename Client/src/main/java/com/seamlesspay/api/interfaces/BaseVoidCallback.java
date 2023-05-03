package com.seamlesspay.api.interfaces;

import com.seamlesspay.api.models.BaseChargeToken;

/**
 * Communicates from a HTTP request on the main thread.
 * One and only one method will be invoked in response to a request.
 */
public interface BaseVoidCallback {

	void success();

	/**
	 * @param exception error that caused the request to fail.
	 */
	void failure(Exception exception);
}
