package com.townspriter.android.photobrowser.core.model.extension.engine;

import android.graphics.Matrix;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

/******************************************************************************
 * @Path PhotoBrowserCore:SmoothScrollToOriginalRunnable
 * @Describe 图片滑动缓冲
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class SmoothScrollToOriginalRunnable implements Runnable
{
    private final ImageView mImageView;
    private final float dx;
    private final float mDragDistance;
    private final boolean willQuit;
    private final AccelerateDecelerateInterpolator mInterpolator;
    private final PhotoViewEngine engine;
    /**
     * 松开拖动的图片后.图片回弹或者退出.此时图片背景透明度变化所需时间
     */
    public long ALPHA_CHANGE_TIME=300L;
    private float dy;
    private float mTransDistance;
    private float mLastTransDistance;
    private long mStartTime=-1L;
    
    public SmoothScrollToOriginalRunnable(boolean willQuit,float mDragDistance,float dx,ImageView imageView,PhotoViewEngine engine)
    {
        mImageView=imageView;
        this.engine=engine;
        this.willQuit=willQuit;
        this.mDragDistance=mDragDistance;
        this.dx=dx;
        mInterpolator=new AccelerateDecelerateInterpolator();
    }
    
    @Override
    public void run()
    {
        if(mStartTime==-1L)
        {
            mStartTime=System.currentTimeMillis();
        }
        if(willQuit)
        {
            ALPHA_CHANGE_TIME=300L;
        }
        else
        {
            ALPHA_CHANGE_TIME=150L;
        }
        float normalizedTime=Math.min(System.currentTimeMillis()-mStartTime,ALPHA_CHANGE_TIME);
        mTransDistance=mDragDistance*mInterpolator.getInterpolation(normalizedTime/ALPHA_CHANGE_TIME);
        dy=mTransDistance-mLastTransDistance;
        // 添加校验
        if(normalizedTime==ALPHA_CHANGE_TIME&&!willQuit)
        {
            float currentDy=engine.getValue(engine.mSuppMatrix,Matrix.MTRANS_Y);
            if(dy+currentDy!=0)
            {
                dy=-currentDy;
            }
        }
        int alphaValue=(int)((Math.abs(mDragDistance)-Math.abs(mTransDistance))/PhotoViewEngine.SCREENxHEIGHT*PhotoViewEngine.PHOTOxBGxALPHA)>PhotoViewEngine.PHOTOxBGxALPHA?PhotoViewEngine.PHOTOxBGxALPHA:(int)((Math.abs(mDragDistance)-Math.abs(mTransDistance))/PhotoViewEngine.SCREENxHEIGHT*PhotoViewEngine.PHOTOxBGxALPHA);
        if(willQuit)
        {
            engine.onBackgroundAlphaChangingByGesture(alphaValue);
        }
        else
        {
            engine.onBackgroundAlphaChangingByGesture(255-alphaValue);
        }
        engine.mSuppMatrix.postTranslate(dx,dy);
        engine.checkAndDisplayMatrix();
        if(normalizedTime==ALPHA_CHANGE_TIME)
        {
            if(willQuit)
            {
                if(null!=engine.mScaleDragDetector)
                {
                    engine.mScaleDragDetector.processOnExit();
                }
            }
            else
            {
                mImageView.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        engine.mScaleDragDetector.setIsReleasing(false);
                    }
                });
            }
            mImageView.removeCallbacks(SmoothScrollToOriginalRunnable.this);
        }
        else
        {
            mLastTransDistance=mTransDistance;
            mImageView.post(SmoothScrollToOriginalRunnable.this);
        }
        ViewParent parent=mImageView.getParent();
        if(engine.mAllowParentInterceptOnEdge&&!engine.mScaleDragDetector.isScaling()&&!engine.mBlockParentIntercept)
        {
            if(engine.mScrollEdge==PhotoViewEngine.EDGE_BOTH||(engine.mScrollEdge==PhotoViewEngine.EDGE_LEFT&&dx>=1.0F)||(engine.mScrollEdge==PhotoViewEngine.EDGE_RIGHT&&dx<=-1.0F))
            {
                if(null!=parent)
                {
                    // 准许父类拦截此事件
                    parent.requestDisallowInterceptTouchEvent(false);
                }
            }
        }
        else
        {
            if(null!=parent)
            {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
    }
}
