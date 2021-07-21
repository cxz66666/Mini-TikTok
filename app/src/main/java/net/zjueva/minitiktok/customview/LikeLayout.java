package net.zjueva.minitiktok.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import net.zjueva.minitiktok.R;

import java.lang.ref.WeakReference;
import java.util.Random;


public class LikeLayout extends FrameLayout {
    public interface LikeLayoutInterface {
        void onLikeListener();
        void onPauseListerner();
    }
    public LikeLayout(Context context) {
        super(context);
    }

    public LikeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LikeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Drawable icon=getResources().getDrawable(R.drawable.icon_heart_red);
    private int mClickCount=0;

    public void setLikeLayoutInterface(LikeLayoutInterface likeLayoutInterface) {
        mLikeLayoutInterface = likeLayoutInterface;
    }

    private LikeLayoutInterface mLikeLayoutInterface;
    private LikeLayoutHandle mHandler = new LikeLayoutHandle(this);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            float x=event.getX();
            float y=event.getY();
            mClickCount++;
            mHandler.removeCallbacksAndMessages(null);
            if(mClickCount>=2){
                addHeartView(x,y);
                mLikeLayoutInterface.onLikeListener();
                mHandler.sendEmptyMessageDelayed(1, 500);
            } else{
                mHandler.sendEmptyMessageDelayed(0, 500);

            }
        }
        return false;
    }
    private void pauseClick(){
        if(mClickCount==1){
            mLikeLayoutInterface.onPauseListerner();
        }
        mClickCount=0;
    }
    public void onPause(){
        mClickCount=0;
        mHandler.removeCallbacksAndMessages(null);

    }
    private void addHeartView(float x,float y){
       LayoutParams lp=new  LayoutParams(icon.getIntrinsicWidth(),icon.getIntrinsicHeight());
       lp.leftMargin=(int) (x-icon.getIntrinsicWidth()/2);
       lp.topMargin=(int)(y-icon.getIntrinsicHeight());
        ImageView img=new ImageView(getContext());
        img.setScaleType(ImageView.ScaleType.MATRIX);
        Matrix matrix=new Matrix();
        matrix.postRotate(getRandomRotate());
        img.setImageMatrix(matrix);
        img.setImageDrawable(icon);
        img.setLayoutParams(lp);

        addView(img);

        AnimatorSet animSet=getShowAnimSet(img);
        AnimatorSet hideSet=getHideAnimSet(img);
        animSet.start();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                hideSet.start();
            }
        });
        hideSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeView(img);
            }
        });


    }
    /**
     * 刚点击的时候的一个缩放效果
     */
    private AnimatorSet getShowAnimSet(ImageView view){
        ObjectAnimator scaleX= ObjectAnimator.ofFloat(view, "scaleX", 1.2f, 1f);
        ObjectAnimator scaleY=ObjectAnimator.ofFloat(view, "scaleY", 1.2f, 1f);
        AnimatorSet animatorSet= new  AnimatorSet();
        animatorSet.playTogether(scaleX,scaleY);
        animatorSet.setDuration(100);
        return animatorSet;
    }
    /**
     * 缩放结束后到红心消失的效果
     */
    private AnimatorSet getHideAnimSet(ImageView view){
        ObjectAnimator alpha=  ObjectAnimator.ofFloat(view, "alpha", 1f, 0.1f);
        ObjectAnimator scaleX= ObjectAnimator.ofFloat(view, "scaleX", 1f, 2f);
        ObjectAnimator scaleY= ObjectAnimator.ofFloat(view, "scaleY", 1f, 2f);
        ObjectAnimator translation=ObjectAnimator.ofFloat(view, "translationY", 0f, -150f);
        AnimatorSet animatorSet= new  AnimatorSet();
        animatorSet.playTogether(alpha, scaleX, scaleY, translation);
        animatorSet.setDuration(500);
        return animatorSet;
    }

    private float getRandomRotate(){
        return (float)(new Random().nextInt(30) - 15);
    }


    private class LikeLayoutHandle extends Handler {
        private WeakReference<LikeLayout> mView;
         public LikeLayoutHandle(LikeLayout view){
            mView=new  WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    mView.get().pauseClick();
                    break;
                case 1:
                    mView.get().onPause();
            }
        }
    }
}
