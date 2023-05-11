package com.seamlesspay.api.interfaces;

/**
 * Interface that defines callbacks to be called when charge is voided.
 */

public interface BaseVoidListener extends SeamlesspayListener {
	/**
	 * {@link #onChargeVoided} will be called when a charge has been
	 * voided.
	 */
	void onChargeVoided();
}