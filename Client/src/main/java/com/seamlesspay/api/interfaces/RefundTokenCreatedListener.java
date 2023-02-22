package com.seamlesspay.api.interfaces;

import com.seamlesspay.api.models.RefundToken;

/**
 * Interface that defines callbacks to be called when {@link RefundToken}s are created.
 */
public interface RefundTokenCreatedListener extends SeamlesspayListener  {
	/**
	 * {@link #onRefundTokenCreated} will be called when a new {@link RefundToken} has been
	 * created.
	 *
	 * @param refundToken the {@link RefundToken}.
	 */
	void onRefundTokenCreated(RefundToken refundToken);
}
