package com.seamlesspay.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokenResponse(
  val cardToken: String,
  val tokenType: TokenType,
  val cardDetails: Map<String, String>
) : Parcelable {
  enum class TokenType(val code: String) {
    CARD("card")
  }

  companion object {
    const val PAYMENT_NETWORK_FIELD_KEY = "paymentNetwork"
    const val NAME_FIELD_KEY = "name"
    const val EXPIRATION_DATE_FIELD_KEY = "expirationDate"
    const val LAST_FOUR_FIELD_KEY = "lastFour"
  }
}