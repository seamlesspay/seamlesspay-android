/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.cardform.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.text.style.ReplacementSpan;

/**
 * A {@link android.text.style.ReplacementSpan} used for blank space slashes in
 * {@link android.widget.EditText}
 */
public class SlashSpan extends ReplacementSpan {

  @Override
  public int getSize(
    Paint paint,
    CharSequence text,
    int start,
    int end,
    FontMetricsInt fm
  ) {
    float padding = paint.measureText(" ", 0, 1) * 7;
    float textSize = paint.measureText(text, start, end);

    return (int) (padding + textSize);
  }

  @Override
  public void draw(
    Canvas canvas,
    CharSequence text,
    int start,
    int end,
    float x,
    int top,
    int y,
    int bottom,
    Paint paint
  ) {
    canvas.drawText(text.subSequence(start, end) + "   ", x, y, paint);
  }
}
