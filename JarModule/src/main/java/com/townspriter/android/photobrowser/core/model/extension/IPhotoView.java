package com.townspriter.android.photobrowser.core.model.extension;

import com.townspriter.android.photobrowser.core.api.listener.OnGestureListener;
import com.townspriter.android.photobrowser.core.api.listener.OnPhotoTapListener;
import com.townspriter.android.photobrowser.core.api.listener.OnViewTapListener;
import com.townspriter.android.photobrowser.core.model.listener.OnMatrixChangedListener;
import com.townspriter.android.photobrowser.core.model.listener.OnScaleChangeListener;
import com.townspriter.android.photobrowser.core.model.listener.OnScrollListener;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ImageView;

/******************************************************************************
 * @Path PhotoBrowserCore:IPhotoView
 * @Describe 图片显示接口
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface IPhotoView
{
    float DEFAULTxMAXxSCALE =2.0f;
    float DEFAULTxMIDxSCALE =1.75f;
    float DEFAULTxMINxSCALE =1.0f;
    int DEFAULTxZOOMxDURATION =200;
    
    /**
     * getPhotoView
     *
     * @return
     */
    IPhotoView getPhotoView();
    
    /**
     * getImageView
     *
     * @return
     */
    ImageView getImageView();
    
    /**
     * 获取图片类型
     *
     * @return
     * @0 普通图片
     * @1 动态图片
     * @2 大长图
     */
    int getImageType();
    
    /**
     * setImageType 设置图片类型
     *
     * @param imageType
     * @PhotoViewBean.IMAGExNORMAL 0 普通图片
     * @PhotoViewBean.IMAGExGIF 1 动态图片
     * @PhotoViewBean.IMAGExLONG 2 大长图
     */
    void setImageType(int imageType);
    
    /**
     * 重置图片状态
     */
    void resetStatus();
    
    /**
     * update
     */
    void update();
    
    /**
     * clean
     */
    void clean();
    
    /**
     * 获得图片Y方向滑动的距离
     *
     * @return
     */
    int getTranslateY();
    
    /**
     * setAllowParentInterceptOnEdge 设置父布局是否可以和子布局交界处响应触摸事件
     *
     * @param allow
     */
    void setAllowParentInterceptOnEdge(boolean allow);
    
    /**
     * 检测图片是否允许缩放
     *
     * @return
     */
    boolean canZoom();
    
    /**
     * setZoomable
     *
     * @param zoomable
     */
    void setZoomable(boolean zoomable);
    
    /**
     * setZoomTransitionDuration
     *
     * @param milliseconds
     */
    void setZoomTransitionDuration(int milliseconds);
    
    /**
     * 获取图片相对于原图的显示区域
     *
     * @return
     */
    RectF getDisplayRect();
    
    /**
     * 获取图片相对于原图的显示矩阵
     *
     * @return
     */
    Matrix getDisplayMatrix();
    
    /**
     * 设置图片的可显示区域
     *
     * @param displayMatrix
     * @return true 设置成功 false 设置失败
     */
    boolean setDisplayMatrix(Matrix displayMatrix);
    
    /**
     * 获取大长图当前可见区域的图片
     *
     * @return 可见区域构造的图片
     */
    Bitmap getVisibleRectangleBitmap();
    
    /**
     * @return 获取缩放的最小值.依赖ImageView.ScaleType
     */
    float getMinimumScale();
    
    /**
     * 设置缩放的最小值.依赖ImageView.ScaleType
     *
     * @param minimumScale
     */
    void setMinimumScale(float minimumScale);
    
    /**
     * getMediumScale
     *
     * @return
     */
    float getMediumScale();
    
    /**
     * setMediumScale
     *
     * @param mediumScale
     */
    void setMediumScale(float mediumScale);
    
    /**
     * getMaximumScale
     *
     * @return
     */
    float getMaximumScale();
    
    /**
     * setMaximumScale
     *
     * @param maximumScale
     */
    void setMaximumScale(float maximumScale);
    
    /**
     * setScaleLevel
     * 设置三种缩放等级的上限值
     *
     * @param minimumScale
     * @param mediumScale
     * @param maximumScale
     */
    void setScaleLevel(float minimumScale,float mediumScale,float maximumScale);
    
    /**
     * setScaleTypeSafely
     * 设置三种缩放等级的上限值
     *
     * @param scaleType
     */
    void setScaleTypeSafely(ImageView.ScaleType scaleType);
    
    /**
     * 获取图片的当前缩放值
     *
     * @return
     */
    float getScale();
    
    /**
     * 设置图片的当前缩放值
     *
     * @param scale
     * 缩放数值
     * @return
     */
    void setScale(float scale);
    
    /**
     * 设置缩放
     *
     * @param scale
     * 缩放数值
     * @param animate
     * 是否使用缩放动画
     */
    void setScale(float scale,boolean animate);
    
    /**
     * 以(focusX,focusY)为中心设置缩放
     *
     * @param scale
     * 缩放数值
     * @param focusX
     * 缩放中心位置
     * @param focusY
     * 缩放中心位置
     * @param animate
     * 是否使用缩放动画
     */
    void setScale(float scale,float focusX,float focusY,boolean animate);
    
    /**
     * setRotationTo
     *
     * @param rotationDegree
     */
    void setRotationTo(float rotationDegree);
    
    /**
     * setRotationBy
     *
     * @param rotationDegree
     */
    void setRotationBy(float rotationDegree);
    
    /**
     * getLongPhotoAnalysator
     *
     * @return
     */
    LongPhotoAnalysable getLongPhotoAnalysator();
    
    void setLongPhotoAnalysator(LongPhotoAnalysable analysator);
    
    /**
     * setGestureListener
     *
     * @param listener
     */
    void setGestureListener(OnGestureListener listener);
    
    /**
     * setLongClickListener
     *
     * @param listener
     */
    void setLongClickListener(View.OnLongClickListener listener);
    
    /**
     * setMatrixChangeListener
     *
     * @param listener
     */
    void setMatrixChangeListener(OnMatrixChangedListener listener);
    
    /**
     * setDoubleClickListener
     *
     * @param listener
     */
    void setDoubleClickListener(GestureDetector.OnDoubleTapListener listener);
    
    /**
     * setScaleChangeListener
     *
     * @param listener
     */
    void setScaleChangeListener(OnScaleChangeListener listener);
    
    /**
     * setScrollListener
     *
     * @param listener
     */
    void setScrollListener(OnScrollListener listener);
    
    /**
     * getViewTapListener
     *
     * @return
     */
    OnViewTapListener getViewTapListener();
    
    /**
     * setViewTapListener
     *
     * @param listener
     */
    void setViewTapListener(OnViewTapListener listener);
    
    /**
     * getPhotoTapListener
     *
     * @return
     */
    OnPhotoTapListener getPhotoTapListener();
    
    /**
     * setPhotoTapListener
     *
     * @param listener
     */
    void setPhotoTapListener(OnPhotoTapListener listener);
}
