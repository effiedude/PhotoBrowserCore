package com.townspriter.android.photobrowser.core.model.extension;

import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.widget.ImageView;

/******************************************************************************
 * @path LongPhotoAnalysable
 * @describe 大长图解析器
 * @author 张飞
 * @email zhangfei@personedu.com
 * @date 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface LongPhotoAnalysable
{
    /**
     * getOriginBitmapHeight
     *
     * @return
     */
    int getOriginBitmapHeight();
    
    /**
     * setOriginBitmapHeight 设置原始图片的高度值(主要在大长图分块加载时使用)
     *
     * @param originBitmapHeight
     */
    void setOriginBitmapHeight(int originBitmapHeight);
    
    /**
     * getRegionRect
     *
     * @return
     */
    Rect getRegionRect();
    
    /**
     * 设置分块加载显示边界
     *
     * @param regionRect
     */
    void setRegionRect(Rect regionRect);
    
    /**
     * 读取输入流构造一张图片
     *
     * @param inputStream
     * @return
     */
    Bitmap decodeRegion(InputStream inputStream);
    
    /**
     * reloadBitmapIfNeeded 根据滑动区域检测是否需要继续进行分块加载
     *
     * @param imageView
     * @param dragDistance
     * @return
     */
    boolean reloadBitmapIfNeeded(ImageView imageView,float dragDistance);
}
