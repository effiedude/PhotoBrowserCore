package com.townspriter.android.photobrowser.core.model.scroll;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

/******************************************************************************
 * @Path PhotoBrowserCore:BaseScrollerProxy
 * @Describe 滑动手势代理类
 * @Describe 根据不同的安卓系统版本选择对应的手势滑动实现类
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public abstract class BaseScrollerProxy
{
    public static BaseScrollerProxy getScroller(Context context)
    {
        if(VERSION.SDK_INT<VERSION_CODES.GINGERBREAD)
        {
            return new PreGingerScroller(context);
        }
        else if(VERSION.SDK_INT<VERSION_CODES.ICE_CREAM_SANDWICH)
        {
            return new GingerScroller(context);
        }
        else
        {
            return new IcsScroller(context);
        }
    }
    
    /**
     * computeScrollOffset
     *
     * @return
     */
    public abstract boolean computeScrollOffset();
    
    /**
     * fling 开始滑动
     *
     * @return
     */
    public abstract void fling(int startX,int startY,int velocityX,int velocityY,int minX,int maxX,int minY,int maxY,int overX,int overY);
    
    /**
     * forceFinished 强制停止滑动
     */
    public abstract void forceFinished(boolean finished);
    
    /**
     * isFinished
     * 是否已经停止滑动
     *
     * @return true:是 false:否
     */
    public abstract boolean isFinished();
    
    /**
     * getCurrentX
     *
     * @return 当前滑动的X轴位置
     */
    public abstract int getCurrentX();
    
    /**
     * getCurrentY
     *
     * @return 当前滑动的Y轴位置
     */
    public abstract int getCurrentY();
}
