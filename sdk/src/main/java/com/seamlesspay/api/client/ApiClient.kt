package com.seamlesspay.api.client

import com.seamlesspay.api.interfaces.*
import com.seamlesspay.api.models.*
import com.seamlesspay.api.utils.AppHelper
import org.json.JSONException
import org.json.JSONObject

class ApiClient private constructor(authorization: Authorization) {

  private val configuration = Configuration.fromEnvironment(authorization.environment)

  private val apiHttpClient = SeamlesspayApiHttpClient(configuration.apiUrl, authorization.bearer)

  private val panVaultHttpClient =
    SeamlesspayApiHttpClient(configuration.panVaultUrl, authorization.bearer)

  /**
   * Create a {@link PaymentMethodToken} in the Seamlesspay Gateway.
   *
   * On completion, returns the {@link PaymentMethodToken} to {@link PaymentMethodTokenCallback}.
   *
   * If creation fails validation, {@link PaymentMethodTokenCallback#onError(Exception)}
   * will be called with the resulting {@link ErrorWithResponse}.
   *
   * If an error not due to validation (server error, network issue, etc.) occurs, {@link
   * PaymentMethodTokenCallback#onError(Exception)} (Throwable)}
   * will be called with the {@link Exception} that occurred.
   *
   * @param paymentMethodBuilder {@link PaymentMethodBuilder} for the {@link PaymentMethodToken}
   * to be created.
   */
  fun tokenize(cardBuilder: CardBuilder, callback: PaymentMethodTokenCallback) {
    val request = cardBuilder.build()
    try {
      panVaultHttpClient.post(
        PAYMENT_METHOD_ENDPOINT,
        addFingerprint(request),
        object : HttpResponseCallback {
          override fun success(responseBody: String?) {
            try {
              callback.success(
                PaymentMethodToken.parsePaymentMethodToken(
                  responseBody,
                  cardBuilder.responsePaymentMethodType
                )
              )
            } catch (e: JSONException) {
              callback.failure(e)
            }
          }

          override fun failure(exception: java.lang.Exception?) {
            callback.failure(exception)
          }

        }
      )
    } catch (e: Exception) {
      callback.failure(e)
    }
  }

  fun charge(chargeBuilder: CardChargeBulder, callback: BaseChargeTokenCallback) {
    val request = chargeBuilder.build()
    try {
      apiHttpClient.post(
        CHARGE_ENDPOINT,
        addFingerprint(request),
        object : HttpResponseCallback {
          override fun success(responseBody: String?) {
            try {
              callback.success(BaseChargeToken.parseChargeToken(responseBody))
            } catch (e: JSONException) {
              callback.failure(e)
            }
          }

          override fun failure(exception: java.lang.Exception?) {
            callback.failure(exception)
          }

        }
      )
    } catch (e: Exception) {
      callback.failure(e)
    }
  }

  fun refund(refundBuilder: RefundBuilder, callback: RefundTokenCallback) {
    val request = refundBuilder.build()
    try {
      apiHttpClient.post(
        REFUND_ENDPOINT,
        addFingerprint(request),
        object : HttpResponseCallback {
          override fun success(responseBody: String?) {
            try {
              callback.success(RefundToken.parseRefundToken(responseBody))
            } catch (e: JSONException) {
              callback.failure(e)
            }
          }

          override fun failure(exception: java.lang.Exception?) {
            callback.failure(exception)
          }

        }
      )
    } catch (e: Exception) {
      callback.failure(e)
    }
  }

  fun adjustCharge(adjustChargeBuilder: AdjustChargeBuilder, transactionId: String, callback: BaseAdjustCallback) {
    val request = adjustChargeBuilder.build()
    val path = "$CHARGE_ENDPOINT/$transactionId"
    try {
      apiHttpClient.update(
        path,
        addFingerprint(request),
        object : HttpResponseCallback {
          override fun success(responseBody: String?) {
            try {
              callback.success()
            } catch (e: JSONException) {
              callback.failure(e)
            }
          }

          override fun failure(exception: java.lang.Exception?) {
            callback.failure(exception)
          }

        }
      )
    } catch (e: Exception) {
      callback.failure(e)
    }
  }

  fun voidCharge(transactionId: String, callback: BaseVoidCallback) {
    val path = "$CHARGE_ENDPOINT/$transactionId"
    try {
      apiHttpClient.delete(
        path,
        object : HttpResponseCallback {
          override fun success(responseBody: String?) {
            try {
              callback.success()
            } catch (e: JSONException) {
              callback.failure(e)
            }
          }

          override fun failure(exception: java.lang.Exception?) {
            callback.failure(exception)
          }

        }
      )
    } catch (e: Exception) {
      callback.failure(e)
    }
  }


  private fun addFingerprint(data: String): String {
    val dataJson = JSONObject(data)

    dataJson.put(
      "deviceFingerprint",
      AppHelper.getDeviceFingerprint()
    )

    return dataJson.toString()
  }

  companion object {
    private const val PAYMENT_METHOD_ENDPOINT = "tokens"
    private const val CHARGE_ENDPOINT = "charges"
    private const val REFUND_ENDPOINT = "refunds"

    fun newInstance(authorization: Authorization): ApiClient = ApiClient(authorization)
  }
}