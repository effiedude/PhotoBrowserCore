package com.townspriter.android.photobrowser.core.model.gesture;

import java.util.ArrayList;
import java.util.List;
import com.townspriter.android.photobrowser.core.api.listener.OnGestureListener;
import com.townspriter.base.foundation.utils.log.Logger;
import com.townspriter.base.foundation.utils.system.SystemInfo;
import com.townspriter.base.foundation.utils.ui.ResHelper;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/******************************************************************************
 * @Path PhotoBrowserCore:GestureServiceImpl
 * @Describe 默认手势操作定制
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class GestureServiceImpl implements GestureService
{
    private static final int DRAG_DIRECTION_NONE=0;
    private static final int DRAG_DIRECTION_LEFT=1;
    private static final int DRAG_DIRECTION_RIGHT=-1;
    private static float MINIMUM_QUIT_DISTANCE;
    private final String TAG="GestureServiceImpl";
    protected float mLastTouchX;
    protected float mLastTouchY;
    protected List<OnGestureListener> gestureListeners;
    private int mDragDirection=DRAG_DIRECTION_NONE;
    private float mTouchSlop;
    private float mMinimumVelocity;
    private float mDraggedDistanceY;
    private float mEndingTouchPointY;
    private float mInvalidDraggedDistanceY;
    private boolean mIsDragging;
    private boolean mIsReleasing;
    private boolean mIsPointerDown;
    private float mBeginningTouchPointY;
    private Context mContext;
    private VelocityTracker mVelocityTracker;
    
    public GestureServiceImpl(Context context)
    {
        init(context);
    }
    
    public float getActiveX(MotionEvent motionEvent)
    {
        return motionEvent.getX();
    }
    
    public float getActiveY(MotionEvent motionEvent)
    {
        return motionEvent.getY();
    }
    
    @Override
    public void processOnScale(float scaleFactor,float focusX,float focusY)
    {
        if(null!=gestureListeners&&gestureListeners.size()>0)
        {
            for(OnGestureListener listener:gestureListeners)
            {
                listener.onScale(scaleFactor,focusX,focusY);
            }
        }
    }
    
    @Override
    public void processOnExit()
    {
        if(null!=gestureListeners&&gestureListeners.size()>0)
        {
            for(OnGestureListener listener:gestureListeners)
            {
                listener.onExit();
            }
        }
    }
    
    @Override
    public boolean isScaling()
    {
        return false;
    }
    
    @Override
    public boolean isDragging()
    {
        return mIsDragging;
    }
    
    @Override
    public float getInvalidDraggedDistanceY()
    {
        return mInvalidDraggedDistanceY;
    }
    
    @Override
    public void setInvalidDraggedDistanceY(float invalidDraggedDistanceY)
    {
        mInvalidDraggedDistanceY=invalidDraggedDistanceY;
    }
    
    @Override
    public boolean isReleasing()
    {
        return mIsReleasing;
    }
    
    @Override
    public void setIsReleasing(boolean isReleasing)
    {
        mIsReleasing=isReleasing;
    }
    
    @Override
    public void addGestureListener(OnGestureListener listener)
    {
        if(null!=gestureListeners&&!gestureListeners.contains(listener))
        {
            gestureListeners.add(listener);
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        if(!isReleasing())
        {
            final int action=motionEvent.getAction();
            if(mVelocityTracker==null)
            {
                mVelocityTracker=VelocityTracker.obtain();
            }
            switch(action&MotionEvent.ACTION_MASK)
            {
                case MotionEvent.ACTION_DOWN:
                    mIsPointerDown=false;
                    setInvalidDraggedDistanceY(0);
                    if(null!=mVelocityTracker)
                    {
                        mVelocityTracker.addMovement(motionEvent);
                    }
                    mLastTouchX=getActiveX(motionEvent);
                    mLastTouchY=getActiveY(motionEvent);
                    mBeginningTouchPointY=getActiveY(motionEvent);
                    mIsDragging=false;
                    mDragDirection=DRAG_DIRECTION_NONE;
                    break;
                case MotionEvent.ACTION_MOVE:
                    final float x=getActiveX(motionEvent);
                    final float y=getActiveY(motionEvent);
                    final float dx=x-mLastTouchX,dy=y-mLastTouchY;
                    mDraggedDistanceY=Math.abs(y-mBeginningTouchPointY);
                    if(!mIsDragging)
                    {
                        mIsDragging=Math.sqrt((dx*dx)+(dy*dy))>=mTouchSlop;
                    }
                    if(mIsDragging&&!mIsPointerDown)
                    {
                        // 判断滑动方向
                        if(mDragDirection==DRAG_DIRECTION_NONE)
                        {
                            if(dx>0)
                            {
                                mDragDirection=DRAG_DIRECTION_LEFT;
                            }
                            else if(dx<0)
                            {
                                mDragDirection=DRAG_DIRECTION_RIGHT;
                            }
                        }
                        else if((mDragDirection==DRAG_DIRECTION_LEFT&&dx<0)||(mDragDirection==DRAG_DIRECTION_RIGHT&&dx>0))
                        {
                            // 滑动的方向发生变化时.为排除触摸误差需要大于TouchSlop的值才判做有效
                            if(Math.sqrt((dx*dx)+(dy*dy))<mTouchSlop)
                            {
                                return true;
                            }
                            else
                            {
                                if(dx>0)
                                {
                                    mDragDirection=DRAG_DIRECTION_LEFT;
                                }
                                else if(dx<0)
                                {
                                    mDragDirection=DRAG_DIRECTION_RIGHT;
                                }
                            }
                        }
                        // 执行拖拽回调
                        if(null!=gestureListeners&&gestureListeners.size()>0)
                        {
                            for(OnGestureListener listener:gestureListeners)
                            {
                                listener.onDrag(mDraggedDistanceY,dx,dy);
                            }
                        }
                        mLastTouchX=x;
                        mLastTouchY=y;
                        if(null!=mVelocityTracker)
                        {
                            mVelocityTracker.addMovement(motionEvent);
                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    if(null!=mVelocityTracker)
                    {
                        mVelocityTracker.recycle();
                        mVelocityTracker=null;
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    mIsPointerDown=true;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_UP:
                    mLastTouchX=getActiveX(motionEvent);
                    mLastTouchY=getActiveY(motionEvent);
                    mEndingTouchPointY=motionEvent.getY();
                    mDraggedDistanceY=mEndingTouchPointY-mBeginningTouchPointY;
                    if(mIsDragging)
                    {
                        dragRelease();
                        mIsDragging=false;
                    }
                    if(mVelocityTracker!=null)
                    {
                        // 计算最后一秒滑动的速度
                        mVelocityTracker.addMovement(motionEvent);
                        mVelocityTracker.computeCurrentVelocity(1000);
                        final float vX=mVelocityTracker.getXVelocity(),vY=mVelocityTracker.getYVelocity();
                        // 如果最后一秒的速度大于最小滑动速度则执行回调
                        if(Math.max(Math.abs(vX),Math.abs(vY))>=mMinimumVelocity)
                        {
                            if(null!=gestureListeners&&gestureListeners.size()>0)
                            {
                                for(OnGestureListener listener:gestureListeners)
                                {
                                    listener.onFling(mLastTouchX,mLastTouchY,-vX,-vY);
                                }
                            }
                        }
                    }
                    // 回收速度计算器
                    if(null!=mVelocityTracker)
                    {
                        mVelocityTracker.recycle();
                        mVelocityTracker=null;
                    }
                    break;
                default:
                    break;
            }
        }
        return true;
    }
    
    private void init(Context context)
    {
        mContext=context;
        gestureListeners=new ArrayList<>(8);
        final ViewConfiguration configuration=ViewConfiguration.get(context);
        mMinimumVelocity=configuration.getScaledMinimumFlingVelocity();
        mTouchSlop=configuration.getScaledTouchSlop();
        mInvalidDraggedDistanceY=0.0f;
        MINIMUM_QUIT_DISTANCE=ResHelper.dpToPxF(150);
    }
    
    private void dragRelease()
    {
        float adsDraggedDistanceY=Math.abs(mDraggedDistanceY);
        Logger.d(TAG,"dragRelease-adsDraggedDistanceY:"+adsDraggedDistanceY);
        if(adsDraggedDistanceY>MINIMUM_QUIT_DISTANCE)
        {
            if(mDraggedDistanceY>0)
            {
                // 执行下滑回调
                if(null!=gestureListeners&&gestureListeners.size()>0)
                {
                    for(OnGestureListener listener:gestureListeners)
                    {
                        listener.onDragRelease(true,SystemInfo.INSTANCE.getScreenHeight(mContext)-mDraggedDistanceY,0);
                    }
                }
            }
            else
            {
                // 执行上滑回调
                if(null!=gestureListeners&&gestureListeners.size()>0)
                {
                    for(OnGestureListener listener:gestureListeners)
                    {
                        listener.onDragRelease(true,-(SystemInfo.INSTANCE.getScreenHeight(mContext)+mDraggedDistanceY),0);
                    }
                }
            }
        }
        else
        {
            if(null!=gestureListeners&&gestureListeners.size()>0)
            {
                for(OnGestureListener listener:gestureListeners)
                {
                    listener.onDragRelease(false,-(mDraggedDistanceY-(getInvalidDraggedDistanceY())),0);
                }
            }
        }
        setInvalidDraggedDistanceY(0);
    }
}
