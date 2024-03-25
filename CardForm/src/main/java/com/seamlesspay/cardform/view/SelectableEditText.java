package com.seamlesspay.cardform.view;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Parent {@link android.widget.EditText} to stop selection.
 */
public class SelectableEditText extends TextInputEditText {

	public SelectableEditText(Context context) {
		super(context);
		setCustomSelectionMock();
	}

	public SelectableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomSelectionMock();
	}

	public SelectableEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomSelectionMock();
	}

	private void setCustomSelectionMock() {
		setCustomSelectionActionModeCallback(new ActionMode.Callback() {
			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {

			}
		});
		setCursorVisible(false);
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
