package com.royole.yole.animator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;

import com.royole.yole.R;
import com.royole.yole.YoleDrawable;

/**
 * @author liuyinchang, Easonliu
 * description:
 * date :2021/6/15 16:23
 */
public class FloatingYoleAnimator extends AbsYoleAnimator implements ValueAnimator.AnimatorUpdateListener {
    private MyDrawable mDrawable;

    FloatingYoleAnimator(Context context) {
        super(context);
        int DURATION = 2850;
        mDrawable = new MyDrawable(context);
        setDuration(DURATION);
        setIntValues(0, DURATION);
        setInterpolator(new LinearInterpolator());
        addUpdateListener(this);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mDrawable.updateTime((int) (animation.getAnimatedValue()));
        mDrawable.invalidateSelf();
    }

    @Override
    public YoleDrawable getDrawable() {
        return mDrawable;
    }

    private class MyDrawable extends YoleDrawable {

        private int mTime;

        private Face mFace;
        private Body mBody;
        private EyeLeft mEyeLeft;
        private EyeRight mEyeRight;
        private HandLeft mHandLeft;
        private HandRight mHandRight;
        private Mouth mMouth;


        MyDrawable(Context context) {
            mFace = new Face(context);
            mBody = new Body(context);
            mEyeLeft = new EyeLeft(context);
            mEyeRight = new EyeRight(context);
            mHandLeft = new HandLeft(context);
            mHandRight = new HandRight(context);
            mMouth = new Mouth(context);

        }


        @Override
        public void setAlpha(int alpha) {

        }

        void updateTime(int time) {
            mTime = time;
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            canvas.save();
            canvas.scale(mScaleSize, mScaleSize, canvas.getWidth() / 2f, canvas.getHeight() / 2f);
            canvas.save();
            canvas.translate(canvas.getWidth() / 2f - 130 / 2f, canvas.getHeight() / 2f - 164 / 2);
            mFace.draw(canvas, mTime);
            mEyeLeft.draw(canvas, mTime);
            mEyeRight.draw(canvas, mTime);
            mMouth.draw(canvas, mTime);

            mBody.draw(canvas, mTime);
            mHandLeft.draw(canvas, mTime);
            mHandRight.draw(canvas, mTime);
            canvas.restore();
            canvas.restore();
        }


        @Override
        public void setColorFilter(ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }
    }

    //------------------------------------------------------------------------------------------------
    private class Face extends Basic {
        private Bitmap mBitmap;
        private Matrix mMatrix = new Matrix();

        Face(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_head);
            if (mBitmap.getWidth() != 126 || mBitmap.getHeight() != 103) {
                mBitmap = Bitmap.createScaledBitmap(mBitmap, 126, 103, true);
            }
        }

        private void draw(Canvas canvas, int time) {
            double current = time * 2.2f / 1000f;
            float translateY = (float) Math.sin(current) * 20;
            mMatrix.setTranslate(3, 0 - translateY);
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
    }

    private class Body extends Basic {
        private Bitmap mBitmap;
        private Matrix mMatrix = new Matrix();

        Body(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_body);
            if (mBitmap.getWidth() != 57 || mBitmap.getHeight() != 59) {
                mBitmap = Bitmap.createScaledBitmap(mBitmap, 57, 59, true);
            }
        }

        private void draw(Canvas canvas, int time) {
            double current = time * 2.2f / 1000f;
            float translateY = (float) Math.sin(current) * 20;
            mMatrix.setTranslate(34, 103 - translateY);
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
    }

    private class EyeLeft extends Basic {
        private final float[] mTimes = {50f, 200f};
        private final float[] mProgress = {1f, 0.15f, 1f};
        private Bitmap mSmallBitmap;

        private Bitmap mBitmap;
        private Matrix mMatrix = new Matrix();

        EyeLeft(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_eye_left);
            if (mBitmap.getWidth() != 18 || mBitmap.getHeight() != 22) {
                mBitmap = Bitmap.createScaledBitmap(mBitmap, 18, 22, true);
            }

            mSmallBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.small_eye_left);
            if (mSmallBitmap.getWidth() != 17 || mSmallBitmap.getHeight() != 5) {
                mSmallBitmap = Bitmap.createScaledBitmap(mSmallBitmap, 17, 5, true);
            }
        }

        private void draw(Canvas canvas, int time) {
            Bitmap currentBitmap = mBitmap;
            float progress = getProgress(time, mTimes, mProgress);
            double current = time * 2.2f / 1000f;
            float translateY = (float) Math.sin(current) * 20;
            mMatrix.setTranslate(28, 57 - translateY);
            mMatrix.postScale(1, progress, 28 + mBitmap.getWidth() / 2f, 57 - translateY + mBitmap.getHeight() / 2f);
            canvas.drawBitmap(currentBitmap, mMatrix, null);
        }
    }

    private class EyeRight extends Basic {
        private final float[] mTimes = {50f, 200f};
        private final float[] mProgress = {1f, 0.15f, 1f};
        private Bitmap mSmallBitmap;

        private Bitmap mBitmap;
        private Matrix mMatrix = new Matrix();

        EyeRight(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_eye_right);
            if (mBitmap.getWidth() != 18 || mBitmap.getHeight() != 22) {
                mBitmap = Bitmap.createScaledBitmap(mBitmap, 18, 22, true);
            }

            mSmallBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.small_eye_right);
            if (mSmallBitmap.getWidth() != 17 || mSmallBitmap.getHeight() != 5) {
                mSmallBitmap = Bitmap.createScaledBitmap(mSmallBitmap, 17, 5, true);
            }
        }

        private void draw(Canvas canvas, int time) {
            Bitmap currentBitmap = mBitmap;
            float progress = getProgress(time, mTimes, mProgress);
            double current = time * 2.2f / 1000f;
            float translateY = (float) Math.sin(current) * 20;
            mMatrix.setTranslate(80, 57 - translateY);
            mMatrix.postScale(1, progress, 80 + mBitmap.getWidth() / 2f, 57 - translateY + mBitmap.getHeight() / 2f);
            canvas.drawBitmap(currentBitmap, mMatrix, null);
        }
    }

    private class HandLeft extends Basic {
        private Bitmap mBitmap;
        private Matrix mMatrix = new Matrix();

        HandLeft(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_hand_left);
            if (mBitmap.getWidth() != 19 || mBitmap.getHeight() != 27) {
                mBitmap = Bitmap.createScaledBitmap(mBitmap, 19, 27, true);
            }
        }

        private void draw(Canvas canvas, int time) {
            double current = time * 2.2f / 1000f;
            float translateY = (float) Math.sin(current) * 20;
            mMatrix.setTranslate(16, 116 - translateY);
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
    }

    private class HandRight extends Basic {
        private Bitmap mBitmap;
        private Matrix mMatrix = new Matrix();

        HandRight(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_hand_right);
            if (mBitmap.getWidth() != 19 || mBitmap.getHeight() != 27) {
                mBitmap = Bitmap.createScaledBitmap(mBitmap, 19, 27, true);
            }
        }

        private void draw(Canvas canvas, int time) {
            double current = time * 2.2f / 1000f;
            float translateY = (float) Math.sin(current) * 20;
            mMatrix.setTranslate(90, 116 - translateY);
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
    }

    private class Mouth extends Basic {

        private Bitmap mSmallBitmap;
        private Bitmap mBitmap;
        private Matrix mMatrix = new Matrix();

        Mouth(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_mouth);
            if (mBitmap.getWidth() != 12 || mBitmap.getHeight() != 8) {
                mBitmap = Bitmap.createScaledBitmap(mBitmap, 12, 8, true);
            }

            mSmallBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_small_mouth);
            if (mSmallBitmap.getWidth() != 13 || mSmallBitmap.getHeight() != 7) {
                mSmallBitmap = Bitmap.createScaledBitmap(mSmallBitmap, 13, 7, true);
            }
        }

        private void draw(Canvas canvas, int time) {
            Bitmap currentBitmap = mBitmap;
            double current = time * 2.2f / 1000f;
            float translateY = (float) Math.sin(current) * 20;
            mMatrix.setTranslate(57, 81 - translateY);
            canvas.drawBitmap(currentBitmap, mMatrix, null);
        }
    }

    private class Basic {

        float getProgress(int time, float[] times, float[] progresss) {
            float progress = 1f;
            float t;
            if (time <= 1000 + times[0]) {
                t = 1f * (time - 1000) / times[0];
                progress = progresss[0] + new PathInterpolator(0.33f, 0.00f, 0.67f, 1.0f).getInterpolation(t) * (progresss[1] - progresss[0]);
            } else if (time <= 1000 + times[0] + times[1]) {
                t = 1f * (time - 1000 - times[0]) / times[1];
                progress = progresss[1] + new PathInterpolator(0.33f, 0.00f, 0.67f, 1.0f).getInterpolation(t) * (progresss[2] - progresss[1]);
            }
            return progress;
        }
    }
}
