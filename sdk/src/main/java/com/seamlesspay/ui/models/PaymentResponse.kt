package com.seamlesspay.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PaymentResponse(
  val paymentToken: String,
  val paymentType: PaymentType,
  val paymentDetails: Map<String, String>
) : Parcelable {
  enum class PaymentType(val code: String) {
    CARD("card")
  }

  companion object {
    const val AMOUNT_FIELD_KEY = "amount"
    const val AUTH_CODE_FIELD_KEY = "authCode"
    const val BATCH_ID_FIELD_KEY = "batchId"
    const val ID_FIELD_KEY = "id"
    const val CARD_BRAND_FIELD_KEY = "cardBrand"
    const val STATUS_FIELD_KEY = "status"
    const val STATUS_CODE_FIELD_KEY = "statusCode"
    const val STATUS_DESCRIPTION_FIELD_KEY = "statusDescription"
    const val SURCHARGE_FEE_FIELD_KEY = "surchargeFeeAmount"
    const val TRANSACTION_DATE_FIELD_KEY = "transactionDate"
  }
}