package com.townspriter.android.photobrowser.core.model.view;

import com.townspriter.android.photobrowser.core.R;
import com.townspriter.base.foundation.utils.log.Logger;
import com.townspriter.base.foundation.utils.ui.ResHelper;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/******************************************************************************
 * @Path PhotoBrowserCore:PhotoViewPager
 * @Describe 图片滑动器
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class PhotoViewPager extends ViewPager
{
    private static final String TAG="PhotoViewPager";
    private float mLastMotionX;
    private float mLastMotionY;
    
    public PhotoViewPager(@NonNull Context context)
    {
        super(context);
        initView();
    }
    
    public PhotoViewPager(@NonNull Context context,@Nullable AttributeSet attrs)
    {
        super(context,attrs);
        initView();
    }
    
    private void initView()
    {
        // 设置背景黑色保持跟其它图层背景颜色一致.防止出现白边
        setBackgroundColor(Color.TRANSPARENT);
        setPageMargin(ResHelper.getDimenInt(R.dimen.browserxcorexdpx0));
        setPageMarginDrawable(new ColorDrawable(ResHelper.getColor(R.color.resxcolorxtranslate)));
    }
    
    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept)
    {
        final ViewParent parent=getParent();
        if(parent!=null)
        {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }
    
    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept)
    {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
        if(!disallowIntercept)
        {
            requestParentDisallowInterceptTouchEvent(true);
        }
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent)
    {
        final int action=motionEvent.getAction();
        final float x=motionEvent.getX();
        final float y=motionEvent.getY();
        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
            {
                mLastMotionX=x;
                mLastMotionY=y;
                requestParentDisallowInterceptTouchEvent(true);
                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                final float xDiff=Math.abs(x-mLastMotionX);
                final float yDiff=Math.abs(y-mLastMotionY);
                /** 这里只需判断是否禁止父控件拦截触摸事件.因此无需考虑触摸差值 */
                if(xDiff>yDiff)
                {
                    requestParentDisallowInterceptTouchEvent(true);
                    return true;
                }
                break;
            }
            /** 不拦截以下动作.使手势完整完成 */
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                requestParentDisallowInterceptTouchEvent(false);
                return false;
            default:
                break;
        }
        try
        {
            return super.onInterceptTouchEvent(motionEvent);
        }
        catch(Throwable throwable)
        {
            Logger.w(TAG,"onInterceptTouchEvent:Throwable",throwable);
            return false;
        }
    }
    
    private boolean canScrollXTo(float x)
    {
        return canScrollHorizontally((int)(mLastMotionX-x));
    }
    
    private boolean canScrollYTo(float y)
    {
        return canScrollVertically((int)(mLastMotionY-y));
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        switch(motionEvent.getAction())
        {
            case MotionEvent.ACTION_MOVE:
            {
                final float x=motionEvent.getX();
                final float y=motionEvent.getY();
                final float xDiff=Math.abs(x-mLastMotionX);
                final float yDiff=Math.abs(y-mLastMotionY);
                if((xDiff>yDiff&&!canScrollXTo(x))||(xDiff<yDiff&&!canScrollYTo(y)))
                {
                    requestParentDisallowInterceptTouchEvent(false);
                    return false;
                }
                break;
            }
            default:
                break;
        }
        try
        {
            boolean ret=super.onTouchEvent(motionEvent);
            return ret;
        }
        catch(Throwable throwable)
        {
            Logger.w(TAG,"onTouchEvent:Throwable",throwable);
            return false;
        }
    }
}
