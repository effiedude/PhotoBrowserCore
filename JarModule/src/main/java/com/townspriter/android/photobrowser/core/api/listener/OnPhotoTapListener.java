package com.townspriter.android.photobrowser.core.api.listener;

import android.view.View;

/******************************************************************************
 * @Path PhotoBrowserCore:OnPhotoTapListener
 * @Describe 图片轻点事件监听类
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface OnPhotoTapListener
{
    /**
     * A callback to receive where the user taps on a photo. You will only receive a callback if
     * the user taps on the actual photo, tapping on 'whitespace' will be ignored.
     *
     * @param view
     * - View the user tapped.
     * @param x
     * - where the user tapped from the of the Drawable, as percentage of the
     * Drawable width.
     * @param y
     * - where the user tapped from the top of the Drawable, as percentage of the
     * Drawable height.
     */
    void onPhotoTap(View view,float x,float y);
}
