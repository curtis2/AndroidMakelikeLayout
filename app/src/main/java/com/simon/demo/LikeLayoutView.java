package com.simon.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 *  点赞动画的实现思路
 *  1.首先会生成很多的布局，所以应该是一个layout,然后动态的添加view
 *  2.每个view添加的时候是一个完整的动画，两部分：首先在底部由小变大，然后是一个曲线的上升过程. 在曲线上升的过程中，会有一个渐隐的效果。
 *  3.点击的的星星的颜色是随机的,曲线上升的速率也是随机的。
 */

public class LikeLayoutView extends RelativeLayout {

    /**
     * 添加不同的插值器
     */
    LinearInterpolator linearinterpolator=new LinearInterpolator();
    AccelerateInterpolator accelerateinterpolator=new AccelerateInterpolator();
    DecelerateInterpolator decelerateinterpolator=new DecelerateInterpolator();
    AccelerateDecelerateInterpolator acceleratedecelerateinterpolator = new AccelerateDecelerateInterpolator();//先加速后减速

    Interpolator[] mInterpolators;
    private Drawable[] drawables;
    private int mHeartImageWidth;
    private int mHeartImageHeight;

    Random randowm=new Random();

    RelativeLayout.LayoutParams lp;

    private int mWidth;
    private int mHeight;

    public LikeLayoutView(Context context) {
        super(context);
        init();
    }

    public LikeLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LikeLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        drawables=new Drawable[3];
        Drawable red=getResources().getDrawable(R.drawable.pl_red);
        Drawable blug=getResources().getDrawable(R.drawable.pl_blue);
        Drawable yellow=getResources().getDrawable(R.drawable.pl_yellow);
        drawables[0]=red;
        drawables[1]=blug;
        drawables[2]=yellow;
        mHeartImageWidth=red.getIntrinsicWidth();
        mHeartImageHeight=red.getIntrinsicHeight();
        lp=new LayoutParams(mHeartImageWidth,mHeartImageHeight);
        lp.addRule(CENTER_HORIZONTAL, TRUE);
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE);

        mInterpolators=new Interpolator[4];
        mInterpolators[0]=linearinterpolator;
        mInterpolators[1]=accelerateinterpolator;
        mInterpolators[2]=decelerateinterpolator;
        mInterpolators[3]=acceleratedecelerateinterpolator;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth=getMeasuredWidth();
        mHeight=getMeasuredHeight();
    }


     public void addHeart(){
         ImageView view =new ImageView(getContext());
         view.setImageDrawable(drawables[randowm.nextInt(3)]);
         view.setLayoutParams(lp);
         addView(view);
         //添加动画
         AnimatorSet animatorset=getAnimationSet(view);
         animatorset.addListener(new MyAnimListener(view));
         animatorset.start();
     }

    private AnimatorSet getAnimationSet(ImageView view) {
        //首先是放大的动画
        AnimatorSet animatorSet = getEnterAnimatorSet(view);
        //移动动画
        ValueAnimator moveAnimator = getBezierMoveAnimator(view);
        AnimatorSet finallySet=new AnimatorSet();
        finallySet.playSequentially(animatorSet);
        finallySet.playSequentially(animatorSet,moveAnimator);
        animatorSet.setInterpolator(mInterpolators[randowm.nextInt(4)]);
        finallySet.setTarget(view);
        return finallySet;
    }

    private ValueAnimator getBezierMoveAnimator(final ImageView view) {
        //计算Bezier曲线的路径
        BezierPathEvaluator evaluator=new BezierPathEvaluator(getFlagPointF(2),getFlagPointF(1));
        ValueAnimator  valueAnimator= ValueAnimator.ofObject(evaluator,new PointF((mWidth-mHeartImageWidth)/2,mHeight-mHeartImageHeight),
                new PointF(randowm.nextInt(mWidth),0));
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF  pointF = (PointF) animation.getAnimatedValue();
                view.setX(pointF.x);
                view.setY(pointF.y);
                view.setAlpha(1-animation.getAnimatedFraction());
            }
        });
        return  valueAnimator;
    }

    /**
     * 计算Bezier曲线的控制点
     * @return
     */
    private PointF getFlagPointF(int scale) {
        PointF  pointF=new PointF();
        pointF.x= randowm.nextInt(mWidth-100);
        pointF.y= randowm.nextInt((mHeight-100)/scale);
        return  pointF;
    }


    @NonNull
    private AnimatorSet getEnterAnimatorSet(ImageView view) {
        //首先是放大的动画
        AnimatorSet animatorSet=new AnimatorSet();
        ObjectAnimator objectAnimatorX=ObjectAnimator.ofFloat(view, View.SCALE_Y,0.3f,1f);
        ObjectAnimator objectAnimatorY=ObjectAnimator.ofFloat(view, View.SCALE_X,0.3f,1f);
        ObjectAnimator alphaAnimator=ObjectAnimator.ofFloat(view,View.ALPHA,0f,1f);
        animatorSet.setDuration(1000);
        animatorSet.playTogether(objectAnimatorX,objectAnimatorY,alphaAnimator);
        return animatorSet;
    }


    /**
     * 动画结束后，移除view
     */
    private class MyAnimListener extends AnimatorListenerAdapter{
        private View target;
        public MyAnimListener(View target) {
            this.target=target;
        }
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            removeView(target);
        }
    }


}
