package com.townspriter.android.photobrowser.core.model.gesture;

import com.townspriter.android.photobrowser.core.api.listener.OnGestureListener;
import android.view.MotionEvent;

/******************************************************************************
 * @Path PhotoBrowserCore:GestureService
 * @Describe 手势操作定制
 * @Describe 如果需要自定义手势操作.可以实现此接口.通过PhotoViewWindow的setGestureService()来设置
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface GestureService
{
    /**
     * onTouchEvent 处理触摸逻辑并下发手势操作
     *
     * @param motionEvent
     * @return
     */
    boolean onTouchEvent(MotionEvent motionEvent);
    
    /**
     * isScaling
     *
     * @return 是否正在执行缩放操作
     */
    boolean isScaling();
    
    /**
     * isDragging
     *
     * @return 是否正在执行拖拽操作
     */
    boolean isDragging();
    
    /**
     * getInvalidDraggedDistanceY
     *
     * @return 获取执行拖拽的最小阀值
     */
    float getInvalidDraggedDistanceY();
    
    /**
     * setInvalidDraggedDistanceY 设置执行拖拽的最小阀值
     *
     * @param invalidDraggedDistanceY
     */
    void setInvalidDraggedDistanceY(float invalidDraggedDistanceY);
    
    /**
     * isReleasing
     *
     * @return
     */
    boolean isReleasing();
    
    /**
     * setIsReleasing
     *
     * @param isReleasing
     */
    void setIsReleasing(boolean isReleasing);
    
    /**
     * setGestureListener
     *
     * @param listener
     */
    void addGestureListener(OnGestureListener listener);
    
    /**
     * processOnScale 下发缩放手势
     *
     * @param scaleFactor
     * @param focusX
     * @param focusY
     */
    void processOnScale(float scaleFactor,float focusX,float focusY);
    
    /**
     * processOnExit 下发退出手势
     */
    void processOnExit();
}
