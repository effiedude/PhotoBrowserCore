package com.townspriter.android.photobrowser.core.model.gesture;

import com.townspriter.android.photobrowser.core.api.listener.OnGestureListener;
import android.content.Context;

/******************************************************************************
 * @Path PhotoBrowserCore:GestureDetectorFactory
 * @Describe 手势监听器静态工厂
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class GestureDetectorFactory
{
    public static GestureService newInstance(Context context,OnGestureListener listener)
    {
        GestureService detector=new PhotoGestureDetector(context);
        detector.addGestureListener(listener);
        return detector;
    }
}