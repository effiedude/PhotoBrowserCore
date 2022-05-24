package com.townspriter.android.photobrowser.core.model.listener;

import android.graphics.RectF;

/******************************************************************************
 * @Path PhotoBrowserCore:OnMatrixChangedListener
 * @Describe 监听图片显示矩阵变化
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface OnMatrixChangedListener
{
    /**
     * 当执行大图拖动或者放大或缩小等操作之后回回调此方法
     *
     * @param rect
     * 操作之后图片显示的新矩阵
     */
    void onMatrixChanged(RectF rect);
}
