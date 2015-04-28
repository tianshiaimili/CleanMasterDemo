package com.yzy.supercleanmaster.ui;

import java.util.Random;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.yzy.supercleanmaster.R;
import com.yzy.supercleanmaster.base.BaseActivity;
import com.yzy.supercleanmaster.service.CleanerService;
import com.yzy.supercleanmaster.service.CoreService;
import com.yzy.supercleanmaster.utils.LogUtils;
import com.yzy.supercleanmaster.utils.SharedPreferencesUtils;


@SuppressLint("NewApi")
public class SplishActivity extends BaseActivity {

    /**
     * 三个切换的动画
     */
    private Animation mFadeIn;
    private Animation mFadeInScale;
    private Animation mFadeOut;
    
    ////
    @SuppressLint("NewApi")
	AnimatorSet animSet = new AnimatorSet();
    

    //  @InjectView(R.id.image)
    ImageView mImageView;

    public static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splish);
        mImageView = (ImageView) findViewById(R.id.image);
        int index = new Random().nextInt(2);
        if (index == 1) {
            mImageView.setImageResource(R.drawable.entrance3);
        } else {
            mImageView.setImageResource(R.drawable.entrance2);
        }
        startService(new Intent(this, CoreService.class));
        startService(new Intent(this, CleanerService.class));


        if (!SharedPreferencesUtils.isShortCut(mContext)) {
            createShortCut();
        }

        playWithAfter(mImageView);
//        initAnim();
//        setListener();
    }

    /**安装快捷小图标*/
    private void createShortCut() {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "一键加速");
        intent.putExtra("duplicate", false);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.short_cut_icon));
        Intent i = new Intent();
        i.setAction("com.yzy.shortcut");
        i.addCategory("android.intent.category.DEFAULT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, i);
        sendBroadcast(intent);
        SharedPreferencesUtils.setIsShortCut(mContext, true);
    }

    private void initAnim() {
        mFadeIn = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_in);
        mFadeIn.setDuration(500);
        mFadeInScale = AnimationUtils.loadAnimation(this,
                R.anim.welcome_fade_in_scale);
        mFadeInScale.setDuration(2000);
        mFadeOut = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_out);
        mFadeOut.setDuration(200);
        mImageView.startAnimation(mFadeIn);
    }

    
    
	public void playWithAfter(View view)
	{
		float cx = mImageView.getX();
		LogUtils.i("cx--"+cx);

		ObjectAnimator alphAnimator = ObjectAnimator.ofFloat(mImageView, "alpha", 0.5f,1f);
		alphAnimator.setDuration(2000);
		ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mImageView, "scaleX",
				1.0f, 1.1f);
		scaleXAnimator.setDuration(2000);
		//
		ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mImageView, "scaleY",
				1.0f, 1.1f);
		scaleYAnimator.setDuration(2000);
		
		PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f,
				1f, 0f);
		PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1.2f,
				 1f);
		PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1.2f,
				 1f);
		ObjectAnimator scaleOutAnimator = ObjectAnimator.ofPropertyValuesHolder(mImageView, pvhX, pvhY, pvhZ);
		
		/**
		 * anim1，anim2,anim3同时执行
		 * anim4接着执行
		 */
		AnimatorSet animSet = new AnimatorSet();
		animSet.play(scaleXAnimator).with(scaleYAnimator).with(alphAnimator);
//		animSet.play(scaleOutAnimator).after(alphAnimator);
//		animSet.setDuration(1000);
		animSet.start();
		animSet.setInterpolator(new DecelerateInterpolator());
		animSet.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
                startActivity(MainActivity.class);
                finish();
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				
			}
		});
	}
    

    /**
     * 监听事件
     */
    public void setListener() {
        /**
         * 动画切换原理:开始时是用第一个渐现动画,当第一个动画结束时开始第二个放大动画,当第二个动画结束时调用第三个渐隐动画,
         * 第三个动画结束时修改显示的内容并且重新调用第一个动画,从而达到循环效果
         */
        mFadeIn.setAnimationListener(new AnimationListener() {

            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                mImageView.startAnimation(mFadeInScale);
            }
        });
        mFadeInScale.setAnimationListener(new AnimationListener() {

            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                startActivity(MainActivity.class);
                finish();
//                 mImageView.startAnimation(mFadeOut);
            }
        });
        mFadeOut.setAnimationListener(new AnimationListener() {

            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                // startActivity(MainActivity.class);
                startActivity(MainActivity.class);
                finish();
            }
        });
    }
}
