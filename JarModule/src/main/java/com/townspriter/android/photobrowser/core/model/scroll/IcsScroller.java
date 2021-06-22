package com.townspriter.android.photobrowser.core.model.scroll;

import android.annotation.TargetApi;
import android.content.Context;

/******************************************************************************
 * @Path PhotoBrowserCore:IcsScroller
 * @Describe 手势实现类.适配API-14
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
@TargetApi(14)
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
