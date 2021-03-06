/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.cardform.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import com.seamlesspay.cardform.utils.ViewUtils;

public class PaddedImageSpan extends ImageSpan {
  private boolean mDisabled;
  private int mPadding;
  private int mResourceId;

  public PaddedImageSpan(Context context, int resourceId) {
    super(context, resourceId);
    mResourceId = resourceId;
    mPadding = ViewUtils.dp2px(context, 8);
  }

  public void setDisabled(boolean disabled) {
    mDisabled = disabled;
  }

  int getResourceId() {
    return mResourceId;
  }

  boolean isDisabled() {
    return mDisabled;
  }

  @Override
  public int getSize(
    Paint paint,
    CharSequence text,
    int start,
    int end,
    Paint.FontMetricsInt fm
  ) {
    return super.getSize(paint, text, start, end, fm) + (2 * mPadding);
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
    super.draw(canvas, text, start, end, x + mPadding, top, y, bottom, paint);
  }

  @Override
  public Drawable getDrawable() {
    Drawable drawable = super.getDrawable();

    if (mDisabled) {
      drawable.mutate().setAlpha(80);
    }

    return drawable;
  }
}
