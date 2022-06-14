package com.townspriter.android.photobrowser.core.model.scroll;

import android.content.Context;

/******************************************************************************
 * @path IcsScroller
 * @describe 手势实现类
 * @author 张飞
 * @email zhangfei@personedu.com
 * @date 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class IcsScroller extends GingerScroller
{
    public IcsScroller(Context context)
    {
        super(context);
    }
    
    @Override
    public boolean computeScrollOffset()
    {
        return mScroller.computeScrollOffset();
    }
}
