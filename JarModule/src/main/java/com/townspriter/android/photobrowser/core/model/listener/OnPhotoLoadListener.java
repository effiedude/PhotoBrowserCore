package com.townspriter.android.photobrowser.core.model.listener;
/******************************************************************************
 * @path OnPhotoLoadListener
 * @describe
 * @author 张飞
 * @email zhangfei@personedu.com
 * @date 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface OnPhotoLoadListener
{
    void onPhotoLoadFailed();
    
    void onPhotoLoadSucceed();
    
    void onPhotoLoading(int progress);
}
