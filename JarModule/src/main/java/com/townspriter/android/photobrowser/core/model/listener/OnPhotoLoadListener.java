package com.townspriter.android.photobrowser.core.model.listener;
/******************************************************************************
 * @Path PhotoBrowserCore:OnPhotoLoadListener
 * @Describe
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface OnPhotoLoadListener
{
    /**
     * onPhotoLoadFailed
     */
    void onPhotoLoadFailed();
    
    /**
     * onPhotoLoadSucceed
     */
    void onPhotoLoadSucceed();
    
    /**
     * onPhotoLoading
     *
     * @param progress
     * 加载进度(暂时保留)
     */
    void onPhotoLoading(int progress);
}
