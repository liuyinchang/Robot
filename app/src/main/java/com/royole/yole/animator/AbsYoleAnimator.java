package com.royole.yole.animator;

import android.animation.ValueAnimator;
import android.content.Context;
import com.royole.yole.YoleDrawable;

/**
 * @author liuyinchang, Easonliu
 * description:
 * date :2021/6/15 16:23
 */
public abstract class AbsYoleAnimator extends ValueAnimator {
    public abstract YoleDrawable getDrawable();

    private final static float mDefaultDensity = 2.625f;
    float mScaleSize;

    public final static int TYPE_WAVE_LEFT = 0;
    private final static int TYPE_WAVE_RIGHT = 1;
    private final static int TYPE_FLOAT_YOLE = 2;
    private final static int TYPE_ERROR_YOLE = 3;
    private final static int TYPE_SICK_YOLE = 4;
    public static int ANIMATION_NUM = 5;

    AbsYoleAnimator(Context context) {
        mScaleSize = context.getResources().getDisplayMetrics().density / mDefaultDensity;
    }

    public static class Factory {
        public static AbsYoleAnimator getInstance(Context context, int count) {
            AbsYoleAnimator absRobotAnimator;
            switch (count) {
                case TYPE_WAVE_LEFT:
                    absRobotAnimator = new WaveYoleAnimator(context, true);
                    break;
                case TYPE_WAVE_RIGHT:
                    absRobotAnimator = new WaveYoleAnimator(context, false);
                    break;
                case TYPE_FLOAT_YOLE:
                    absRobotAnimator = new FloatingYoleAnimator(context);
                    break;
                case TYPE_ERROR_YOLE:
                    absRobotAnimator = new ErrorYoleAnimator(context);
                    break;
                case TYPE_SICK_YOLE:
                    absRobotAnimator = new SickYoleAnimator(context);
                    break;
                default:
                    absRobotAnimator = new WaveYoleAnimator(context, true);
                    break;
            }
            return absRobotAnimator;
        }
    }
}
