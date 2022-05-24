package com.townspriter.android.photobrowser.core.model.listener;
/******************************************************************************
 * @Path PhotoBrowserCore:OnScaleChangeListener
 * @Describe 控件缩放监听器
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface OnScaleChangeListener
{
    /**
     * 滑动监听
     *
     * @param scaleFactor
     * the scale factor (<1 for zoom out, >1 for zoom in)
     * @param focusX
     * focal point X position
     * @param focusY
     * focal point Y position
     */
    void onScaleChange(float scaleFactor,float focusX,float focusY);
}
