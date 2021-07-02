package com.royole.yole;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.Drawable;

/**
 * @author liuyinchang, Easonliu
 * description:
 * date :2021/6/15 16:23
 */
public abstract class YoleDrawable extends Drawable {
    private float mProgress = 1;
    private Path mPath = new Path();

    public void update(float progress) {
        mProgress = progress;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        mPath.reset();
        float x = canvas.getWidth() / 2f;
        float y = canvas.getHeight() / 2f;
        float radius = Math.min(x, y) * mProgress;
        mPath.addCircle(x, y, radius, Path.Direction.CW);
        canvas.clipPath(mPath);
    }
}
