package com.royole.yole.animator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;

import com.royole.yole.R;
import com.royole.yole.YoleDrawable;

/**
 * @author liuyinchang, Easonliu
 * description:
 * date :2021/6/18 16:23
 */
public class ErrorYoleAnimator extends AbsYoleAnimator implements ValueAnimator.AnimatorUpdateListener {
    private MyDrawable mDrawable;
    private float mFinalY = 0f;

    ErrorYoleAnimator(Context context) {
        super(context);
        int DURATION = 2600;
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
        private Ears mEars;


        MyDrawable(Context context) {
            mFace = new Face(context);
            mEars = new Ears(context);
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
            mEars.draw(canvas, mTime);
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
        private Bitmap mEarsBitmap;
        private Bitmap mMaskBitmap;
        private Bitmap mBitmap;
        private Matrix mMatrix = new Matrix();

        Face(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.yole_face);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, Math.round(121), Math.round(105), true);

            mMaskBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.yole_mask);
            mMaskBitmap = Bitmap.createScaledBitmap(mMaskBitmap, Math.round(122), Math.round(106), true);

            mEarsBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_ears);
            mEarsBitmap = Bitmap.createScaledBitmap(mEarsBitmap, Math.round(130), Math.round(41), true);
        }

        private void draw(Canvas canvas, int time) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            if(time <= 100){
                float translate = mFinalY + getBasicProgress(time, 100f, new LinearInterpolator()) * (0 - mFinalY);
                //Log.i("TestLog", " mFinalY = " + mFinalY + " translate = " + translate + " time = " + time);
                if(time == 100){
                    mFinalY = 0f;
                }
                mMatrix.setTranslate(-2, (42 - translate));
                canvas.drawBitmap(mEarsBitmap, mMatrix , null);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                mMatrix.setTranslate(3, (0 - translate));
                canvas.drawBitmap(mBitmap, mMatrix, paint);
            }else {
                if (time >= 250 && time <= 1900) {
                    float translateTime = (time - 250) / 1000f;
                    float progress = getErrorProgress(time - 250);
                    Bitmap result = Bitmap.createBitmap(mMaskBitmap.getWidth(), mMaskBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas mCanvas = new Canvas(result);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                    float translateX = (float) Math.sin(translateTime * 6 * 1.8) * 3 * progress / 100;
                    mMatrix.setTranslate(translateX, 0);
                    mCanvas.drawBitmap(mBitmap, mMatrix, null);
                    mCanvas.drawBitmap(mMaskBitmap, 0, 0, paint);
                    paint.setXfermode(null);
                    canvas.drawBitmap(result, 3, 0, null);
                } else {
                    mMatrix.setTranslate(3, 0);
                    canvas.drawBitmap(mBitmap, mMatrix, paint);
                }
            }
        }
    }

    private class Ears extends Basic {
        private Bitmap mBitmap;
        private Matrix mMatrix = new Matrix();

        Ears(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_ears);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, Math.round(130), Math.round(41 ), true);
        }

        private void draw(Canvas canvas, int time) {
            if (time <= 100) {
                float translate = mFinalY + getBasicProgress(time, 100f, new LinearInterpolator()) * (0 - mFinalY);
                mMatrix.setTranslate(-2, (42 - translate));
                if (time == 100) {
                    mFinalY = 0f;
                }
            } else if (time >= 250 && time <= 1900) {
                float translateTime = (time - 250) / 1000f;
                float progress = getErrorProgress(time - 250);
                float translateX = (float) Math.sin(translateTime * 6 * 1.8) * 1 * progress / 100;
                mMatrix.setTranslate((-2 + translateX), 42 );
            }
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
    }

    private class Body extends Basic {
        private Bitmap mBitmap;
        private Matrix mMatrix = new Matrix();

        Body(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_body);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, Math.round(57), Math.round(59), true);
        }

        private void draw(Canvas canvas, int time) {
            if(time <= 100){
                float translate = mFinalY + getBasicProgress(time, 100f, new LinearInterpolator()) * (0 - mFinalY);
                mMatrix.setTranslate(34, (103 - translate));
                if(time == 100){
                    mFinalY = 0f;
                }
            }
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
    }

    private class EyeLeft extends Basic {
        private Bitmap mSmallBitmap;

        private Bitmap mBitmap;
        private Matrix mMatrix = new Matrix();
        private float mEyeProgress = 1f;

        EyeLeft(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_eye_left);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, Math.round(18), Math.round(22), true);

            mSmallBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.small_eye_left);
            mSmallBitmap = Bitmap.createScaledBitmap(mSmallBitmap, Math.round(17), Math.round(5), true);
        }

        private void draw(Canvas canvas, int time) {
            Bitmap currentBitmap = mBitmap;
            if(time <= 100){
                float translate = mFinalY + getBasicProgress(time, 100f, new LinearInterpolator()) * (0 - mFinalY);
                mMatrix.setTranslate(28, (57 - translate));
                if(time == 100){
                    mFinalY = 0f;
                }
            }else {
                if (time > 250 && time < 2400) {
                    currentBitmap = mSmallBitmap;
                    mMatrix.setTranslate(29, 68 );
                } else {
                    mMatrix.setTranslate(28 , 57 );
                }
                if (time >= 200 && time <= 250) {
                    mEyeProgress = 1.0f + getBasicProgress(time - 200, 50f, new PathInterpolator(0.33f, 0.00f, 0.67f, 1.00f)) * (0.15f - 1.00f);
                    mMatrix.postScale(1, mEyeProgress, (28 + mBitmap.getWidth() / 2f), (57 + mBitmap.getHeight() / 2f));
                } else if (time >= 250 && time <= 1900) {
                    float translateTime = (time - 250) / 1000f;
                    float errorProgress = getErrorProgress(time - 250);
                    float translateX = (float) Math.sin(translateTime * 6 * 1.8) * 6 * errorProgress / 100f;
                    mMatrix.setTranslate((29 + translateX), 68 );
                } else if (time >= 2400) {
                    mEyeProgress = 0.15f + getBasicProgress(time - 2400, 200f, new PathInterpolator(0.33f, 0.00f, 0.67f, 1.00f)) * (1.00F - 0.15f);
                    mMatrix.postScale(1, mEyeProgress, (28 + mBitmap.getWidth() / 2f), (57 + mBitmap.getHeight() / 2f));
                }
            }
            canvas.drawBitmap(currentBitmap, mMatrix, null);
        }
    }

    private class EyeRight extends Basic {
        private Bitmap mSmallBitmap;

        private Bitmap mBitmap;
        private Matrix mMatrix = new Matrix();
        private float mEyeProgress = 1.0f;

        EyeRight(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_eye_right);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, Math.round(18), Math.round(22), true);

            mSmallBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.small_eye_right);
            mSmallBitmap = Bitmap.createScaledBitmap(mSmallBitmap, Math.round(17), Math.round(5), true);
        }

        private void draw(Canvas canvas, int time) {
            Bitmap currentBitmap = mBitmap;
            if(time <= 100){
                float translate = mFinalY + getBasicProgress(time, 100f, new LinearInterpolator()) * (0 - mFinalY);
                mMatrix.setTranslate(80, (57 - translate));
                if(time == 100){
                    mFinalY = 0f;
                }
            }else {
                if (time > 250 && time < 2400) {
                    currentBitmap = mSmallBitmap;
                    mMatrix.setTranslate(80, 68);
                } else {
                    mMatrix.setTranslate(80, 57);
                }
                if (time >= 200 && time <= 250) {
                    mEyeProgress = 1.0f + getBasicProgress(time - 200, 50f, new PathInterpolator(0.33f, 0.00f, 0.67f, 1.00f)) * (0.15f - 1.00f);
                    mMatrix.postScale(1, mEyeProgress, (80 + mBitmap.getWidth() / 2f), (57 + mBitmap.getHeight() / 2f));
                } else if (time >= 250 && time <= 1900) {
                    float translateTime = (time - 250) / 1000f;
                    float errorProgress = getErrorProgress(time - 250);
                    float translateX = (float) Math.sin(translateTime * 6 * 1.8) * 6 * errorProgress / 100f;
                    mMatrix.setTranslate((80 + translateX), 68);
                } else if (time >= 2400) {
                    mEyeProgress = 0.15f + getBasicProgress(time - 2400, 200f, new PathInterpolator(0.33f, 0.00f, 0.67f, 1.00f)) * (1.00f - 0.15f);
                    mMatrix.postScale(1, mEyeProgress, (80 + mBitmap.getWidth() / 2f), (57 + mBitmap.getHeight() / 2f));
                }
            }
            canvas.drawBitmap(currentBitmap, mMatrix, null);
        }
    }

    private class HandLeft extends Basic {
        private Bitmap mBitmap;
        private Matrix mMatrix = new Matrix();
        private float mHandleProgress = 0f;

        HandLeft(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_hand_left);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, Math.round(19), Math.round(27), true);
        }

        private void draw(Canvas canvas, int time) {
            if(time <= 100){
                float translate = mFinalY + getBasicProgress(time, 100f, new LinearInterpolator()) * (0 - mFinalY);
                mMatrix.setTranslate(16, (116 - translate));
                if(time == 100){
                    mFinalY = 0f;
                }
            }else {
                if (time >= 100 && time <= 400) {
                    mHandleProgress = getBasicProgress(time - 100, 300f, new PathInterpolator(0.35f, 0.00f, 0.37f, 1.0f)) * 80f;
                } else if (time >= 1850 && time <= 2100) {
                    mHandleProgress = 80f + getBasicProgress(time - 1850, 250f, new PathInterpolator(0.17f, 0.00f, 0.83f, 1.0f)) * (0 - 80f);
                }
                mMatrix.setTranslate(16, 116);
                mMatrix.postRotate(mHandleProgress, 37, 114);
            }
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
    }

    private class HandRight extends Basic {
        private Bitmap mBitmap;
        private Matrix mMatrix = new Matrix();
        private float mHandleProgress = 0f;

        HandRight(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_hand_right);
            if (mBitmap.getWidth() != 19 || mBitmap.getHeight() != 27) {
                mBitmap = Bitmap.createScaledBitmap(mBitmap, 19, 27, true);
            }
        }

        private void draw(Canvas canvas, int time) {
            if(time <= 100){
                float translate = mFinalY + getBasicProgress(time, 100f, new LinearInterpolator()) * (0 - mFinalY);
                mMatrix.setTranslate(90, (116 - translate));
                if(time == 100){
                    mFinalY = 0f;
                }
            }else {
                if (time > 100 && time <= 400) {
                    mHandleProgress = getBasicProgress(time - 100, 300f, new PathInterpolator(0.35f, 0.00f, 0.37f, 1.0f)) * -80f;
                } else if (time >= 1850 && time <= 2100) {
                    mHandleProgress = -80f + getBasicProgress(time - 1850, 250f, new PathInterpolator(0.17f, 0.00f, 0.83f, 1.0f)) * 80f;
                }
                mMatrix.setTranslate(90, 116 );
                mMatrix.postRotate(mHandleProgress, 88, 114);
            }
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
    }

    private class Mouth extends Basic {

        private Bitmap mSmallBitmap;
        private Bitmap mBitmap;
        private Matrix mMatrix = new Matrix();

        Mouth(Context context) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_mouth);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, Math.round(12), Math.round(8), true);

            mSmallBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.anim_yole_small_mouth);
            mSmallBitmap = Bitmap.createScaledBitmap(mSmallBitmap, Math.round(13), Math.round(7), true);
        }

        private void draw(Canvas canvas, int time) {
            Bitmap currentBitmap = mBitmap;
            if(time <= 100){
                float translate = mFinalY + getBasicProgress(time, 100f, new LinearInterpolator()) * (0 - mFinalY);
                mMatrix.setTranslate(57, (81 - translate));
                if(time == 100){
                    mFinalY = 0f;
                }
            }else if(time >= 250 && time <= 1900) {
                float translateTime = (time - 250) / 1000f;
                float progress = getErrorProgress(time - 250);
                float translateX = (float) Math.sin(translateTime * 6 * 1.8) * 8 * progress / 100;
                mMatrix.setTranslate((57 + translateX), 81);
            }
            if(time >= 250 && time <= 2400){
                currentBitmap = mSmallBitmap;
            }
            canvas.drawBitmap(currentBitmap, mMatrix, null);
        }
    }

    private class Basic {
        final float[] mTimes = {300f, 500f, 350f, 500f};
        private final float[] mProgress = {0f, 120f, 150f, 120f};

        float getErrorProgress(int time) {
            float progress = 0f;
            float t;
            if (time <= mTimes[0]) {
                t = 1f * time / mTimes[0];
                progress = mProgress[0] + new PathInterpolator(0.28f, 0.15f, 0.82f, 0.88f).getInterpolation(t) * (mProgress[1] - mProgress[0]);
            } else if (time <= mTimes[0] + mTimes[1]) {
                t = 1f * (time - mTimes[0]) / mTimes[1];
                progress = mProgress[1] + new PathInterpolator(0.11f, 0.67f, 0.66f, 1.06f).getInterpolation(t) * (mProgress[2] - mProgress[1]);
            } else if (time <= mTimes[0] + mTimes[1] + mTimes[2]) {
                t = 1f * (time - mTimes[1] - mTimes[0]) / mTimes[2];
                progress = mProgress[2] + new PathInterpolator(0.52f, 0.05f, 0.86f, 0.39f).getInterpolation(t) * (mProgress[3] - mProgress[2]);
            } else if (time <= mTimes[0] + mTimes[1] + mTimes[2] + mTimes[3]) {
                t = 1f * (time - mTimes[2] - mTimes[1] - mTimes[0]) / mTimes[3];
                progress = mProgress[3] + new PathInterpolator(0.09f, 0.15f, 0.00f, 1.00f).getInterpolation(t) * (0 - mProgress[3]);
            }
            return progress;
        }

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

        public float getBasicProgress(int time, float allTime, Interpolator interpolator) {
            float t = 1f * time / allTime;
            float progress = interpolator.getInterpolation(t);
            if (interpolator.getInterpolation(t) > 1) {
                Log.v("TestLog", "Error: interpolators[1].getInterpolation(t):" + interpolator.getInterpolation(t));
            }
            return progress;
        }
    }
}
