package com.townspriter.android.photobrowser.core.api.listener;

import android.view.View;

/******************************************************************************
 * @Path PhotoBrowserCore:OnViewTapListener
 * @Describe
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface OnViewTapListener
{
    /**
     * 监听当前控件的轻触事件
     *
     * @param view
     * 需要被监听的控件
     * @param x
     * 触摸的位置
     * @param y
     * 触摸的位置
     */
    void onViewTap(View view,float x,float y);
}
