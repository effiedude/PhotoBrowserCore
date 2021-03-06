package com.townspriter.android.photobrowser.core.model.listener;
/******************************************************************************
 * @path PhotoBrowserCore:OnScrollListener
 * @describe 图片滑动监听器
 * @author 张飞
 * @email zhangfei@personedu.com
 * @date 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface OnScrollListener
{
    /**
     * 上滑
     *
     * @param velocityX
     * x轴速率
     * @param velocityY
     * y轴速率
     */
    void onFlingUp(float velocityX,float velocityY);
    
    /**
     * 下滑
     *
     * @param velocityX
     * x轴速率
     * @param velocityY
     * y轴速率
     */
    void onFlingDown(float velocityX,float velocityY);
    
    /**
     * getBackgroundAlphaByScroll
     *
     * @return
     */
    int getBackgroundAlphaByScroll();
    
    /**
     * onBackgroundAlphaChangingByScroll
     *
     * @param alpha
     */
    void onBackgroundAlphaChangingByScroll(int alpha);
    
    /**
     * onScrollExit 拖动退出
     */
    void onScrollExit();
}
