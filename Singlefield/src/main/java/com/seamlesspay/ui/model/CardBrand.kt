package com.seamlesspay.ui.model

import androidx.annotation.DrawableRes
import com.seamlesspay.ui.R

/**
 * A representation of supported card brands and related data
 */
enum class CardBrand(
    val code: String,
    val displayName: String,
    @DrawableRes val icon: Int,
    @DrawableRes val cvcIcon: Int = R.drawable.seamlesspay_ic_cvc,

    /**
     * Accepted CVC lengths
     */
    val cvcLength: Set<Int> = setOf(3),

    /**
     * The max length when the card number is formatted with spaces (e.g. "4242 4242 4242 4242")
     */
    val maxLengthWithSpaces: Int = 19,

    /**
     * The max length when the card number is formatted without spaces (e.g. "4242424242424242")
     */
    val maxLengthWithoutSpaces: Int = 16,

    /**
     * Based on [Issuer identification number table](http://en.wikipedia.org/wiki/Bank_card_number#Issuer_identification_number_.28IIN.29)
     */
    val prefixes: List<String> = emptyList(),

    /**
     * The position of spaces in a formatted card number. For example, "4242424242424242" is
     * formatted to "4242 4242 4242 4242". The spaces in that number are at the positions
     * specified by [spacePositions].
     */
    val spacePositions: Set<Int> = setOf(4, 9, 14)
) {
    AmericanExpress(
        "amex",
        "American Express",
        R.drawable.seamlesspay_ic_amex,
        cvcIcon = R.drawable.seamlesspay_ic_cvc_amex,
        cvcLength = setOf(3, 4),
        maxLengthWithSpaces = 17,
        maxLengthWithoutSpaces = 15,
        prefixes = listOf("34", "37"),
        spacePositions = setOf(4, 11)
    ),

    Discover(
        "discover",
        "Discover",
        R.drawable.seamlesspay_ic_discover,
        prefixes = listOf("60", "64", "65")
    ),

    JCB(
        "jcb",
        "JCB",
        R.drawable.seamlesspay_ic_jcb,
        prefixes = listOf("35")
    ),

    DinersClub(
        "diners",
        "Diners Club",
        R.drawable.seamlesspay_ic_diners,
        maxLengthWithSpaces = 17,
        maxLengthWithoutSpaces = 14,
        prefixes = listOf(
            "300", "301", "302", "303", "304", "305", "309", "36", "38", "39"
        )
    ),

    Visa(
        "visa",
        "Visa",
        R.drawable.seamlesspay_ic_visa,
        prefixes = listOf("4")
    ),

    MasterCard(
        "mastercard",
        "MasterCard",
        R.drawable.seamlesspay_ic_mastercard,
        prefixes = listOf(
            "2221", "2222", "2223", "2224", "2225", "2226", "2227", "2228", "2229", "223", "224",
            "225", "226", "227", "228", "229", "23", "24", "25", "26", "270", "271", "2720",
            "50", "51", "52", "53", "54", "55", "67"
        )
    ),

    Unknown(
        "unknown",
        "Unknown",
        R.drawable.seamlesspay_ic_unknown,
        cvcLength = setOf(3, 4)
    );

    val maxCvcLength: Int
        get() {
            return cvcLength.max() ?: CVC_COMMON_LENGTH
        }

    /**
     * Checks to see whether the input number is of the correct length, given the assumed brand of
     * the card. This function does not perform a Luhn check.
     *
     * @param cardNumber the card number with no spaces or dashes
     * @return `true` if the card number is the correct length for the assumed brand
     */
    fun isValidCardNumberLength(cardNumber: String?): Boolean {
        return cardNumber != null && Unknown != this &&
            cardNumber.length == maxLengthWithoutSpaces
    }

    fun isValidCvc(cvc: String): Boolean {
        return cvcLength.contains(cvc.length)
    }

    fun isMaxCvc(cvcText: String?): Boolean {
        val cvcLength = cvcText?.trim()?.length ?: 0
        return maxCvcLength == cvcLength
    }

    companion object {
        /**
         * @param cardNumber a card number
         * @return the [CardBrand] that matches the [cardNumber]'s prefix, if one is found;
         * otherwise, [CardBrand.Unknown]
         */
        fun fromCardNumber(cardNumber: String?): CardBrand {
            return values()
                .firstOrNull { cardBrand ->
                    cardBrand.prefixes
                        .takeIf {
                            it.isNotEmpty()
                        }?.any {
                            cardNumber?.startsWith(it) == true
                        } == true
                } ?: Unknown
        }

        /**
         * @param code a brand code, such as `Visa` or `American Express`.
         * See [PaymentMethod.Card.brand].
         */
        fun fromCode(code: String?): CardBrand {
            return values().firstOrNull { it.code.equals(code, ignoreCase = true) } ?: Unknown
        }

        private const val CVC_COMMON_LENGTH: Int = 3
    }
}
