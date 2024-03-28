/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.demosinglefield;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.seamlesspay.api.exceptions.InvalidArgumentException;
import com.seamlesspay.api.models.Authorization;
import com.seamlesspay.ui.common.PaymentCallback;
import com.seamlesspay.ui.common.TokenizeCallback;
import com.seamlesspay.ui.models.PaymentRequest;
import com.seamlesspay.ui.models.PaymentResponse;
import com.seamlesspay.ui.models.TokenResponse;
import com.seamlesspay.ui.paymentinputs.direct.SingleLineCardForm;

public class MainActivity
  extends AppCompatActivity {
  SingleLineCardForm mCardInputWidget;
  TextView mInfoView;
  ProgressBar mProgressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);

    Button payButton = findViewById(R.id.payButton);
    Button tokenizeButton = findViewById(R.id.tokenizeButton);
    mProgressBar = findViewById(R.id.progress);

    mInfoView = findViewById(R.id.infoView);

    mCardInputWidget = (SingleLineCardForm) findViewById(R.id.cardInputWidget);
    mCardInputWidget.configureForUs();

    try {
      Authorization authorization = Authorization.fromKeys(
          "staging",
          "pk_XXXXXXXXXXXXXXXXXXXXXXXXXX"
      );

      mCardInputWidget.init(authorization, null);
    } catch (InvalidArgumentException e) {
      mInfoView.setText(e.getMessage());
    }

    tokenizeButton.setOnClickListener(v -> {
      // Code here executes on main thread after user presses button
      mCardInputWidget.clearFocus();
      mInfoView.setText("");
      mProgressBar.setVisibility(View.VISIBLE);
      mCardInputWidget.tokenize(new TokenizeCallback() {
        @Override
        public void success(@NonNull TokenResponse tokenResponse) {
          mInfoView.setText(
              tokenResponse.toString()
          );
          mProgressBar.setVisibility(View.GONE);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void failure(@Nullable Exception exception) {
          mInfoView.setText("Error\n" + exception.getMessage());
          mProgressBar.setVisibility(View.GONE);
        }
      });
    });

    payButton.setOnClickListener(v -> {
      // Code here executes on main thread after user presses button
      mCardInputWidget.clearFocus();
      mInfoView.setText("");

      PaymentRequest paymentRequest = new PaymentRequest(
        "100",
          true
      );
      mProgressBar.setVisibility(View.VISIBLE);
      mCardInputWidget.submit(paymentRequest, new PaymentCallback() {
        @Override
        public void success(@NonNull PaymentResponse paymentResponse) {
          mInfoView.setText(
              paymentResponse.toString()
          );
          mProgressBar.setVisibility(View.GONE);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void failure(@Nullable Exception exception) {
          mInfoView.setText("Error\n" + exception.getMessage());
          mProgressBar.setVisibility(View.GONE);
        }
      });
    });
  }
}
