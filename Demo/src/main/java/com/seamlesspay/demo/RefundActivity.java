/*
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.demo;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import com.seamlesspay.api.client.ApiClient;
import com.seamlesspay.api.interfaces.RefundTokenCallback;
import com.seamlesspay.api.models.PaymentMethodToken;
import com.seamlesspay.api.models.RefundBuilder;
import com.seamlesspay.api.models.RefundToken;

public class RefundActivity extends BaseActivity {
	public static final String EXTRA_PAYMENT_METHOD_TOKEN = "token";
	private ProgressBar mLoadingSpinner;
	private Long mStartTime, mEndTime;

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void reset() {
	}

	@Override
	protected void onAuthorizationFetched() {
		mApiClient = ApiClient.Companion.newInstance(mAuthorization);

		createRefund((PaymentMethodToken) getIntent().getParcelableExtra(EXTRA_PAYMENT_METHOD_TOKEN));

		mStartTime = System.currentTimeMillis();
	}

	public void onError(Exception error) {
		super.onError(error);

		setStatus(R.string.transaction_failed);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_transaction_activity);

		ActionBar actionBar = getSupportActionBar();

		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		mLoadingSpinner = findViewById(R.id.loading_spinner);

		setTitle(R.string.processing_transaction);
	}

	private void createRefund(PaymentMethodToken token) {
		RefundBuilder refundBuilder = new RefundBuilder().setAmount("1")
		                                                 .setCurrency(RefundBuilder.Keys.CURRENCY_USD)
		                                                 .setToken(token.getToken())
		                                                 .setDescriptor("Demo Android Client Refund");
		mApiClient.refund(refundBuilder, new RefundTokenCallback() {
			@Override
			public void success(RefundToken refundToken) {
				mEndTime = System.currentTimeMillis();

				long timeElapsed = mEndTime - mStartTime;

				setStatus(R.string.transaction_complete);
				setMessage("Amount: "
						+ refundToken.getAmount()
						+ "\nStatus: "
						+ refundToken.getStatus()
						+ "\nStatus message: "
						+ refundToken.getStatusDescription()
						+ "\ntxnID #: "
						+ refundToken.getId()
						+ "\nRefund runtime : "
						+ ((float) timeElapsed / 1000)
						+ " s");
			}

			@Override
			public void failure(Exception exception) {
				onError(exception);
			}
		});
	}

	private void setStatus(int message) {
		mLoadingSpinner.setVisibility(View.GONE);

		setTitle(message);

		TextView status = findViewById(R.id.transaction_status);

		status.setText(message);
		status.setVisibility(View.VISIBLE);
	}

	private void setMessage(String message) {
		mLoadingSpinner.setVisibility(View.GONE);

		TextView textView = findViewById(R.id.transaction_message);

		textView.setText(message);
		textView.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}

		return false;
	}
}