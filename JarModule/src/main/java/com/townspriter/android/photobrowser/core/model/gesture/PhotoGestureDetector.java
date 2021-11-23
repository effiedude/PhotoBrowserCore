package com.townspriter.android.photobrowser.core.model.gesture;

import com.townspriter.android.photobrowser.core.api.listener.OnGestureListener;
import com.townspriter.base.foundation.utils.log.Logger;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/******************************************************************************
 * @Path PhotoBrowserCore:PhotoGestureDetector
 * @Describe
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class PhotoGestureDetector extends TouchGestureDetector
{
    protected final ScaleGestureDetector mDetector;
    private final String TAG="PhotoGestureDetector";
    
    public PhotoGestureDetector(Context context)
    {
        super(context);
        ScaleGestureDetector.OnScaleGestureListener mScaleListener=new ScaleGestureDetector.OnScaleGestureListener()
        {
            @Override
            public boolean onScale(ScaleGestureDetector detector)
            {
                Logger.i(TAG,"onScale");
                // scaleFactor>0:两个手指之间的滑动距离是不断增加
                // scaleFactor<0:两个手指之间的滑动距离是不断减少
                float scaleFactor=detector.getScaleFactor();
                if(Float.isNaN(scaleFactor)||Float.isInfinite(scaleFactor))
                {
                    return false;
                }
                // 执行缩放回调
                if(null!=gestureListeners&&gestureListeners.size()>0)
                {
                    for(OnGestureListener listener:gestureListeners)
                    {
                        listener.onScale(scaleFactor,detector.getFocusX(),detector.getFocusY());
                    }
                }
                return true;
            }
            
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector)
            {
                Logger.i(TAG,"onScaleBegin");
                return true;
            }
            
            @Override
            public void onScaleEnd(ScaleGestureDetector detector)
            {
                Logger.i(TAG,"onScaleEnd");
            }
        };
        mDetector=new ScaleGestureDetector(context,mScaleListener);
    }
    
    @Override
    public boolean isScaling()
    {
        return mDetector.isInProgress();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        mDetector.onTouchEvent(motionEvent);
        return super.onTouchEvent(motionEvent);
    }
}
