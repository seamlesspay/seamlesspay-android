package com.seamlesspay.ui.common

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.text.method.TextKeyListener
import android.util.AttributeSet
import com.seamlesspay.ui.R

class PostalCodeEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) : SeamlesspayEditText(context, attrs, defStyleAttr) {

    init {
        setErrorMessage(resources.getString(R.string.invalid_zip))
        maxLines = 1
        configureForUs()
    }

    /**
     * Configure the field for United States users
     */
    @JvmSynthetic
    internal fun configureForUs() {
        setHint(R.string.address_label_zip_code)
        filters = arrayOf(InputFilter.LengthFilter(MAX_LENGTH_US))
        keyListener = DigitsKeyListener.getInstance(false, true)
        inputType = InputType.TYPE_CLASS_NUMBER
    }

    /**
     * Configure the field for global users
     */
    @JvmSynthetic
    internal fun configureForGlobal() {
        setHint(R.string.address_label_postal_code)
        filters = arrayOf(InputFilter.LengthFilter(MAX_LENGTH_GLOBAL))
        keyListener = TextKeyListener.getInstance()
        inputType = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
    }

    private companion object {
        private const val MAX_LENGTH_US = 5
        private const val MAX_LENGTH_GLOBAL = 13
    }
}
