package com.seamlesspay.cardform.view;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Parent {@link android.widget.EditText} to stop selection.
 */
public class SelectableEditText extends TextInputEditText {

	public SelectableEditText(Context context) {
		super(context);
	}

	public SelectableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SelectableEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onSelectionChanged(int selStart, int selEnd) {
		Editable text = getText();
		if (text != null) {
			if (selStart != text.length() || selEnd != text.length()) {
				setSelection(text.length(), text.length());
				return;
			}
		}
		super.onSelectionChanged(selStart, selEnd);
	}
}
