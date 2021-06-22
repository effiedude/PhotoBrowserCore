package com.townspriter.android.photobrowser.core.model.util;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;

/******************************************************************************
 * @Path PhotoBrowserCore:AnimationCompat
 * @Describe 动画兼容类
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class AnimationCompat
{
    private static final int SIXTY_FPS_INTERVAL=1000/60;
    
    public static void postOnAnimation(View view,Runnable runnable)
    {
        if(VERSION.SDK_INT>=VERSION_CODES.JELLY_BEAN)
        {
            view.postOnAnimation(runnable);
        }
        else
        {
            view.postDelayed(runnable,SIXTY_FPS_INTERVAL);
        }
    }
}
