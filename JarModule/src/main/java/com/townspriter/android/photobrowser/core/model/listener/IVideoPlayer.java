package com.townspriter.android.photobrowser.core.model.listener;

import android.view.ViewGroup;
import androidx.annotation.NonNull;

/******************************************************************************
 * @path PhotoBrowserCore:IVideoPlayer
 * @version
 * @describe 视频播放器
 * @author 张飞
 * @email
 * @date 2022-06-17-15:02
 * CopyRight(C)2022 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public interface IVideoPlayer
{
    void bindData(@NonNull String url);
    
    void bindView(@NonNull ViewGroup videoView);
    
    void unbindView();
    
    void play(@NonNull String path);
    
    void pause();
    
    void resume();

    void stop();
    
    void destroy();
}
