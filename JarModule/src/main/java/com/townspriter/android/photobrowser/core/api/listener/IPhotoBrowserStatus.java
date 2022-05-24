package com.townspriter.android.photobrowser.core.api.listener;
/******************************************************************************
 * @Path PhotoBrowserCore:IPhotoBrowserStatus
 * @Describe 图片浏览器监听器
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface IPhotoBrowserStatus
{
    /**
     * 单张图片从下载到显示
     *
     * @param duration
     * 过程耗时
     */
    void onFirstPageShow(long duration);
}
