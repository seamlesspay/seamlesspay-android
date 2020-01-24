package com.seamlesspay.cardform.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.Nullable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.widget.TextView;

import com.seamlesspay.cardform.utils.CardType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Display a set of icons for a list of supported card types.
 */
@SuppressLint("AppCompatCustomView")
public class SupportedCardTypesView extends TextView {

    private List<CardType> mSupportedCardTypes = new ArrayList<>();

    public SupportedCardTypesView(Context context) {
        super(context);
    }

    public SupportedCardTypesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SupportedCardTypesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SupportedCardTypesView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Sets the supported CardTypes on the view to display the card icons.
     *
     * @param cardTypes The CardTypes to display
     */
    public void setSupportedCardTypes(@Nullable CardType... cardTypes) {
        if (cardTypes == null) {
            cardTypes = new CardType[]{};
        }

        mSupportedCardTypes.clear();
        mSupportedCardTypes.addAll(Arrays.asList(cardTypes));

        setSelected(cardTypes);
    }

    /**
     * Selects the intersection between the CardTypes passed into
     *  #setSupportedCardTypes(CardType...) and CardTypes passed into
     * this method as visually enabled.
     *
     * The remaining supported card types will become visually disabled.
     *
     *  setSupportedCardTypes(CardType...) must be called prior to using this method.
     *
     * cardTypes The CardTypes to set as visually enabled.
     */
    public void setSelected( CardType... cardTypes) {
        if (cardTypes == null) {
            cardTypes = new CardType[]{};
        }

        SpannableString spannableString = new SpannableString(new String(new char[mSupportedCardTypes.size()]));
        PaddedImageSpan span;
        for (int i = 0; i < mSupportedCardTypes.size(); i++) {
            span = new PaddedImageSpan(getContext(), mSupportedCardTypes.get(i).getFrontResource());
            span.setDisabled(!Arrays.asList(cardTypes).contains(mSupportedCardTypes.get(i)));
            spannableString.setSpan(span, i, i + 1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        setText(spannableString);
    }
}
