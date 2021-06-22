package com.townspriter.android.photobrowser.core.api.listener;

import android.graphics.drawable.Drawable;

/******************************************************************************
 * @Path PhotoBrowserCore:UICallback
 * @Describe 图片浏览器入口类
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface UICallback
{
    /**
     * onPageChanged
     *
     * @param total
     * @param current
     */
    void onPageChanged(int total,int current);
    
    /**
     * onPhotoSelected
     *
     * @param drawable
     */
    void onPhotoSelected(Drawable drawable);
}
