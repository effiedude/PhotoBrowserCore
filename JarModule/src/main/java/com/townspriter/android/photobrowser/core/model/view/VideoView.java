package com.townspriter.android.photobrowser.core.model.view;

import com.townspriter.android.photobrowser.core.model.listener.IVideoPlayer;
import com.townspriter.base.foundation.utils.lifecycle.AppLifeCycleMonitor;
import com.townspriter.base.foundation.utils.lifecycle.AppStateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @path VideoView
 * @version
 * @describe
 * @author
 * @email
 * @date 2022-06-17-14:56
 * CopyRight(C)2022 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class VideoView extends FrameLayout
{
    private @Nullable IVideoPlayer videoPlayer;
    
    public VideoView(@NonNull Context context)
    {
        this(context,null,0,0);
    }
    
    public VideoView(@NonNull Context context,@Nullable AttributeSet attrs)
    {
        this(context,attrs,0,0);
    }
    
    public VideoView(@NonNull Context context,@Nullable AttributeSet attrs,int defStyleAttr)
    {
        this(context,attrs,defStyleAttr,0);
    }
    
    public VideoView(@NonNull Context context,@Nullable AttributeSet attrs,int defStyleAttr,int defStyleRes)
    {
        super(context,attrs,defStyleAttr,defStyleRes);
        init();
    }
    
    public void setVideoPlayer(@NonNull IVideoPlayer videoPlayer)
    {
        this.videoPlayer=videoPlayer;
    }
    
    private void init()
    {
        AppLifeCycleMonitor.registerAppStateListener(new AppStateListener()
        {
            @Override
            public void onEnterBackground()
            {
                if(videoPlayer!=null)
                {
                    videoPlayer.pause();
                }
            }
            
            @Override
            public void onEnterForeground()
            {
                if(videoPlayer!=null)
                {
                    videoPlayer.resume();
                }
            }
        });
    }
}
