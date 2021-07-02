package com.royole.yole;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.royole.yole.animator.AbsYoleAnimator;

public class MainActivity extends Activity {
    private ImageView mYoleImageView;
    private AbsYoleAnimator mRobotAnimator;
    private int mAnimationType = AbsYoleAnimator.TYPE_WAVE_LEFT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mYoleImageView = findViewById(R.id.royole_yole);

        initRobotAnim();

        findViewById(R.id.royole_yole_start).setOnClickListener(v -> startRobotAnim());
        findViewById(R.id.royole_yole_change).setOnClickListener(v -> endRobotAnim());
    }

    private void initRobotAnim() {
        mRobotAnimator = AbsYoleAnimator.Factory.getInstance(MainActivity.this, mAnimationType);
        mYoleImageView.setImageDrawable(mRobotAnimator.getDrawable());
        mAnimationType ++;
        if(mAnimationType >= AbsYoleAnimator.ANIMATION_NUM){
            mAnimationType = 0;
        }
    }

    public void startRobotAnim() {
        if (mYoleImageView == null || mYoleImageView.getVisibility() != View.VISIBLE) {
            return;
        }
        mRobotAnimator.start();
    }


    public void endRobotAnim() {
        if (mYoleImageView == null || mYoleImageView.getVisibility() != View.VISIBLE || mRobotAnimator == null) {
            return;
        }
        if (mRobotAnimator.isRunning()) {
            mRobotAnimator.end();
        }
        mRobotAnimator = null;
        initRobotAnim();
    }
}