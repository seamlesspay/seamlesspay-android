/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api;

import com.seamlesspay.api.interfaces.BaseAdjustCallback;
import com.seamlesspay.api.interfaces.BaseChargeTokenCallback;
import com.seamlesspay.api.interfaces.BaseVoidCallback;
import com.seamlesspay.api.interfaces.RefundTokenCallback;
import com.seamlesspay.api.interfaces.VerifyTokenCallback;
import com.seamlesspay.api.models.AdjustChargeBuilder;
import com.seamlesspay.api.models.BaseChargeToken;
import com.seamlesspay.api.models.CardChargeBulder;
import com.seamlesspay.api.models.CardVerifyBuilder;
import com.seamlesspay.api.models.RefundBuilder;
import com.seamlesspay.api.models.RefundToken;

public class Transaction {

  public static void create(
    final SeamlesspayFragment fragment,
    final CardChargeBulder chargeBuilder
  ) {
    ChargeClient.create(
      fragment,
      chargeBuilder,
      new BaseChargeTokenCallback() {

        @Override
        public void success(BaseChargeToken baseChargeToken) {
          fragment.postCallback(baseChargeToken);
        }

        @Override
        public void failure(Exception exception) {
          fragment.postCallback(exception);
        }
      }
    );
  }

  public static void verify(
      final SeamlesspayFragment fragment,
      final CardVerifyBuilder chargeBuilder
  ) {
    ChargeClient.verify(
        fragment,
        chargeBuilder,
        new VerifyTokenCallback() {

          @Override
          public void success(BaseChargeToken baseChargeToken) {
            fragment.postCallback(baseChargeToken);
          }

          @Override
          public void failure(Exception exception) {
            fragment.postCallback(exception);
          }
        }
    );
  }

  public static void delete(
      final SeamlesspayFragment fragment,
      final String transactionId
  ) {
    ChargeClient.delete(
        fragment,
        transactionId,
        new BaseVoidCallback() {

          @Override
          public void success() {
            fragment.postCallback();
          }

          @Override
          public void failure(Exception exception) {
            fragment.postCallback(exception);
          }
        }
    );
  }

  public static void adjust(
      final SeamlesspayFragment fragment,
      final String transactionId,
      final String amount
  ) {
    AdjustChargeBuilder builder = new AdjustChargeBuilder()
        .setAmount(amount);

    ChargeClient.adjust(
        fragment,
        transactionId,
        builder,
        new BaseAdjustCallback() {

          @Override
          public void success() {
            fragment.postAdjustCallback();
          }

          @Override
          public void failure(Exception exception) {
            fragment.postCallback(exception);
          }
        }
    );
  }

  public static void capture(
      final SeamlesspayFragment fragment,
      final String transactionId,
      final Boolean capture
  ) {

    AdjustChargeBuilder builder = new AdjustChargeBuilder()
        .setCapture(capture);

    ChargeClient.adjust(
        fragment,
        transactionId,
        builder,
        new BaseAdjustCallback() {

          @Override
          public void success() {
            fragment.postAdjustCallback();
          }

          @Override
          public void failure(Exception exception) {
            fragment.postCallback(exception);
          }
        }
    );
  }

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
