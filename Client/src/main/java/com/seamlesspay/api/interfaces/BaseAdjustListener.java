package com.seamlesspay.api.interfaces;

/**
 * Interface that defines callbacks to be called when charge is adjusted.
 */

public interface BaseAdjustListener extends SeamlesspayListener {
	/**
	 * {@link #onChargeUpdated} will be called when a charge has been
	 * voided.
	 */
	void onChargeUpdated();
}
