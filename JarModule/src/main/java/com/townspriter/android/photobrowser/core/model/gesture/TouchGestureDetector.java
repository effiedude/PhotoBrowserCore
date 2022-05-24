package com.townspriter.android.photobrowser.core.model.gesture;

import android.content.Context;
import android.view.MotionEvent;
import androidx.core.view.MotionEventCompat;

/******************************************************************************
 * @Path PhotoBrowserCore:TouchGestureDetector
 * @Describe
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class TouchGestureDetector extends GestureServiceImpl
{
    private static final int INVALID_POINTER_ID=-1;
    private int mActivePointerId=INVALID_POINTER_ID;
    private int mActivePointerIndex=0;
    
    public TouchGestureDetector(Context context)
    {
        super(context);
    }
    
    @Override
    public float getActiveX(MotionEvent motionEvent)
    {
        try
        {
            return motionEvent.getX(mActivePointerIndex);
        }
        catch(Exception exception)
        {
            return motionEvent.getX();
        }
    }
    
    @Override
    public float getActiveY(MotionEvent motionEvent)
    {
        try
        {
            return motionEvent.getY(mActivePointerIndex);
        }
        catch(Exception exception)
        {
            return motionEvent.getY();
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        final int actionMasked=MotionEventCompat.getActionMasked(motionEvent);
        switch(actionMasked)
        {
            default:
                break;
            case MotionEvent.ACTION_DOWN:
                mActivePointerId=motionEvent.getPointerId(0);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mActivePointerId=INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                final int pointerIndex=MotionEventCompat.getActionIndex(motionEvent);
                final int pointerId=motionEvent.getPointerId(pointerIndex);
                if(pointerId==mActivePointerId)
                {
                    final int newPointerIndex=pointerIndex==0?1:0;
                    mActivePointerId=motionEvent.getPointerId(newPointerIndex);
                    mLastTouchX=motionEvent.getX(newPointerIndex);
                    mLastTouchY=motionEvent.getY(newPointerIndex);
                }
                break;
        }
        mActivePointerIndex=motionEvent.findPointerIndex(mActivePointerId!=INVALID_POINTER_ID?mActivePointerId:0);
        return super.onTouchEvent(motionEvent);
    }
}
