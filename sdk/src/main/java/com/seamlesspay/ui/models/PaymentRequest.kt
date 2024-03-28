package com.seamlesspay.ui.models

import com.seamlesspay.api.models.BaseChargeBuilder.Keys.CURRENCY_USD
import org.json.JSONObject

data class PaymentRequest(
  val amount: String,
  val capture: Boolean = true,
  val currency: String = CURRENCY_USD,
  val description: String? = null,
  val descriptor: String? = null,
  val idempotencyKey: String? = null,
  val metadata: JSONObject? = null,
  val order: JSONObject? = null,
  val orderID: String? = null,
  val poNumber: String? = null,
  val surchargeFeeAmount: String? = null,
  val taxAmount: String? = null,
  val tip: String? = null,
  val taxExempt: Boolean? = null
) {
  constructor(amount: String, capture: Boolean): this(amount = amount, capture = capture, currency = CURRENCY_USD)
}