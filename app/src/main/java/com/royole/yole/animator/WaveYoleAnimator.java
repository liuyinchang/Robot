package com.royole.yole.animator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;

import com.royole.yole.R;
import com.royole.yole.YoleDrawable;
/**
 * @author liuyinchang, Easonliu
 * description:
 * date :2021/6/15 16:23
 */
class WaveYoleAnimator extends AbsYoleAnimator implements ValueAnimator.AnimatorUpdateListener {

    private MyDrawable mDrawable;
    private boolean mIsLeft;

    WaveYoleAnimator(Context context, boolean left) {
        super(context);
        mIsLeft = left;
        this.setDuration(1700);
        this.setIntValues(0, 1700);
        this.setInterpolator(new LinearInterpolator());
        mDrawable = new MyDrawable(context);
        addUpdateListener(this);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mDrawable.update((int) (animation.getAnimatedValue()));
        mDrawable.invalidateSelf();
    }

    @Override
    public YoleDrawable getDrawable() {
        return mDrawable;
    }

    private class MyDrawable extends YoleDrawable {

        private int mTime;
        private Body mBody;
        private EyeLeft mEyeLeft;
        private EyeRight mEyeRight;
        private HandLeft mHandLeft;
        private HandRight mHandRight;
        private Head mHead;
        private Mouth mMouth;
        private int mWidth;
        private int mHeight;

        MyDrawable(Context context) {
            mBody = new Body(context);
            mEyeLeft = new EyeLeft(context);
            mEyeRight = new EyeRight(context);
            mHandLeft = new HandLeft(context);
            mHandRight = new HandRight(context);
            mHead = new Head(context);
            mMouth = new Mouth(context);

        }


        @Override
        public void setAlpha(int alpha) {

        }

        private void update(int time) {
            mTime = time;
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            canvas.save();
            canvas.scale(mScaleSize, mScaleSize, canvas.getWidth() / 2f, canvas.getHeight() / 2f);
            if (mWidth == 0 || mHeight == 0) {
                mWidth = canvas.getWidth();
                mHeight = canvas.getHeight();
            }
            canvas.save();
            if (mIsLeft) {
                canvas.scale(-1, 1, canvas.getWidth() / 2f, canvas.getHeight() / 2f);
            }
            canvas.translate(mWidth / 2f - 130 / 2, mHeight / 2f - 164 / 2);
            float bodyProgress = mBody.getProgress(mTime);
            canvas.rotate(bodyProgress, 63, 164);

            canvas.save();
            float headProgress = mHead.getProgress(mTime);
            canvas.rotate(headProgress, 63, 103);

            mHead.draw(canvas);
            mEyeLeft.draw(canvas, mTime);
            mEyeRight.draw(canvas, mTime);
            mMouth.draw(canvas);
            canvas.restore();

            mBody.draw(canvas);
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

    private class Body extends Basic {
        private final Interpolator[] mInterpolator = {
                new PathInterpolator(0.48f, 0.06f, 0.52f, 0.94f),
                new PathInterpolator(0.47f, 0.00f, 0.77f, 0.63f),
                new PathInterpolator(0.34f, -0.68f, 0.55f, -1.14f),
                new PathInterpolator(0.48f, 0.00f, 0.52f, 1.00f)
        };
        private final float[] mProgress = {0f, 18f, 17.7f, 18.0f};

        Body(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_body);
            if (mBitmap.getWidth() != 57 || mBitmap.getHeight() != 59) {
                mBitmap = Bitmap.createScaledBitmap(mBitmap, 57, 59, true);
            }
        }

        private void draw(Canvas canvas) {
            if (mIsLeft) {
                mMatrix.setScale(-1, 1, mBitmap.getWidth() / 2f, mBitmap.getHeight() / 2f);
                mMatrix.postTranslate(34, 103);
            } else {
                mMatrix.setTranslate(34, 103);
            }
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }

        float getProgress(int time) {
            return getProgress(time, mTimes, mProgress, mInterpolator);
        }
    }

    private class EyeLeft extends Eye {

        EyeLeft(Context context) {
            mTransX = 28;
            mTransY = 57;
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_eye_left);
            if (mBitmap.getWidth() != 18 || mBitmap.getHeight() != 22) {
                mBitmap = Bitmap.createScaledBitmap(mBitmap, 18, 22, true);
            }
        }
    }

    private class EyeRight extends Eye {

        EyeRight(Context context) {
            mTransX = 80;
            mTransY = 57;
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_eye_right);
            if (mBitmap.getWidth() != 18 || mBitmap.getHeight() != 22) {
                mBitmap = Bitmap.createScaledBitmap(mBitmap, 18, 22, true);
            }
        }
    }

    class Eye extends Basic {
        final float[] mTimes = {50f, 200f};
        final float[] mProgress = {1f, 0.15f, 1f};
        final Interpolator[] mInterpolator = {
                new PathInterpolator(0.33f, 0.00f, 0.67f, 1.00f),
                new PathInterpolator(0.33f, 0.00f, 0.67f, 1.00f)
        };
        int mTransX;
        int mTransY;

        void draw(Canvas canvas, int time) {

            if (mIsLeft) {
                mMatrix.setScale(-1, 1, mBitmap.getWidth() / 2f, mBitmap.getHeight() / 2f);
                mMatrix.postTranslate(mTransX, mTransY);
            } else {
                mMatrix.setTranslate(mTransX, mTransY);
            }

            if (time >= 1000 && time <= 1250) {
                float scale = getProgress(time - 1000, mTimes, mProgress, mInterpolator);
                mMatrix.postScale(1, scale, mTransX + mBitmap.getWidth() / 2f, mTransY + mBitmap.getHeight() / 2f);
            }
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
    }

    private class HandLeft extends Basic {
        private final float[] mProgress = {0f, 116f, 80f, 116f};
        private final Interpolator[] mInterpolator = {
                new PathInterpolator(0.44f, 0.20f, 0.71f, 1.00f),
                new PathInterpolator(0.48f, 0.00f, 0.44f, 1.00f),
                new PathInterpolator(0.53f, 0.00f, 0.61f, 1.00f),
                new PathInterpolator(0.17f, 0.00f, 0.52f, 1.00f)
        };

        HandLeft(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_hand_left);
            if (mBitmap.getWidth() != 19 || mBitmap.getHeight() != 27) {
                mBitmap = Bitmap.createScaledBitmap(mBitmap, 19, 27, true);
            }
        }

        private void draw(Canvas canvas, int time) {
            float progress = getProgress(time, mTimes, mProgress, mInterpolator);
            mMatrix.setTranslate(16, 116);
            mMatrix.postRotate(progress, 37, 114);
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
    }

    private class HandRight extends Basic {
        private final float[] mProgress = {0f, -25f, -24.6f, -25f};
        private final Interpolator[] mInterpolator = {
                new PathInterpolator(0.48f, 0.06f, 0.52f, 0.94f),
                new PathInterpolator(0.47f, 0.00f, 0.77f, 0.63f),
                new PathInterpolator(0.34f, -0.68f, 0.55f, -1.14f),
                new PathInterpolator(0.48f, 0.00f, 0.52f, 1.00f)
        };

        HandRight(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_hand_right);
            if (mBitmap.getWidth() != 19 || mBitmap.getHeight() != 27) {
                mBitmap = Bitmap.createScaledBitmap(mBitmap, 19, 27, true);
            }
        }

        private void draw(Canvas canvas, int time) {
            float progress = getProgress(time, mTimes, mProgress, mInterpolator);
            mMatrix.setTranslate(90, 116);
            mMatrix.postRotate(progress, 88, 114);
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
    }

    private class Head extends Basic {
        private final float[] mProgress = {0f, 18f, 17.7f, 18f};
        private final Interpolator[] mInterpolator = {
                new PathInterpolator(0.48f, 0.60f, 0.52f, 0.94f),
                new PathInterpolator(0.47f, 0.00f, 0.77f, 0.63f),
                new PathInterpolator(0.34f, -0.68f, 0.55f, -1.14f),
                new PathInterpolator(0.48f, 0.00f, 0.52f, 1.00f)
        };

        Head(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_head);
            if (mBitmap.getWidth() != 126 || mBitmap.getHeight() != 103) {
                mBitmap = Bitmap.createScaledBitmap(mBitmap, 126, 103, true);
            }
        }

        private void draw(Canvas canvas) {
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }

        float getProgress(int time) {
            return getProgress(time, mTimes, mProgress, mInterpolator);
        }

    }

    private class Mouth extends Basic {

        Mouth(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_mouth);
            if (mBitmap.getWidth() != 12 || mBitmap.getHeight() != 8) {
                mBitmap = Bitmap.createScaledBitmap(mBitmap, 12, 8, true);
            }
        }

        private void draw(Canvas canvas) {
            mMatrix.setTranslate(57, 81);
            canvas.drawBitmap(mBitmap, mMatrix, null);

        }
    }

    private class Basic {

        final float[] mTimes = {700f, 200f, 250f, 550f};

        Bitmap mBitmap;
        Matrix mMatrix = new Matrix();

        float getProgress(int time, float[] times, float[] progresss, Interpolator[] interpolators) {
            float progress = 0f;
            float t;
            if (time <= times[0]) {
                t = 1f * time / times[0];
                progress = progresss[0] + interpolators[0].getInterpolation(t) * (progresss[1] - progresss[0]);
            } else if (time <= times[0] + times[1]) {
                t = 1f * (time - times[0]) / times[1];
                progress = progresss[1] + interpolators[1].getInterpolation(t) * (progresss[2] - progresss[1]);
            } else if (time <= times[0] + times[1] + times[2]) {
                t = 1f * (time - times[1] - times[0]) / times[2];
                progress = progresss[2] + interpolators[2].getInterpolation(t) * (progresss[3] - progresss[2]);
            } else if (time <= times[0] + times[1] + times[2] + times[3]) {
                t = 1f * (time - times[2] - times[1] - times[0]) / times[3];
                progress = progresss[3] + interpolators[3].getInterpolation(t) * (0 - progresss[3]);
            }
            return progress;
        }
    }

}
