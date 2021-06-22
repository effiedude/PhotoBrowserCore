package com.townspriter.android.photobrowser.core.api.listener;
/******************************************************************************
 * @Path PhotoBrowserCore:OnGestureListener
 * @Describe 手势操作监听器
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface OnGestureListener
{
    /**
     * onDrag
     *
     * @param movedDistanceY
     * 图片已经被拖拽的距离
     * @param dx
     * @param dy
     */
    void onDrag(float movedDistanceY,float dx,float dy);
    
    /**
     * onDragRelease
     *
     * @param willExit
     * 是否退出看图模式
     * @param dragDistanceY
     * 松手后图片需要自动回弹或者弹出(退出)的距离
     * @param dragDistanceX
     * 松手后图片需要自动回弹或者弹出(退出)的距离
     */
    void onDragRelease(boolean willExit,float dragDistanceY,float dragDistanceX);
    
    /**
     * onFling
     *
     * @param startX
     * @param startY
     * @param velocityX
     * @param velocityY
     */
    void onFling(float startX,float startY,float velocityX,float velocityY);
    
    /**
     * onScale
     *
     * @param scaleFactor
     * @param focusX
     * @param focusY
     */
    void onScale(float scaleFactor,float focusX,float focusY);
    
    /**
     * getBackgroundAlphaByGesture
     *
     * @return
     */
    int getBackgroundAlphaByGesture();
    
    /**
     * onBackgroundAlphaChangingByGesture
     *
     * @param alphaValue
     */
    void onBackgroundAlphaChangingByGesture(int alphaValue);
    
    /**
     * onExit 退出看图模式
     */
    void onExit();
}
