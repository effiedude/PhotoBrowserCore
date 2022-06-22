package com.townspriter.android.photobrowser.core.model.view;

import com.townspriter.android.photobrowser.core.api.listener.OnGestureListener;
import com.townspriter.android.photobrowser.core.api.listener.OnPhotoTapListener;
import com.townspriter.android.photobrowser.core.api.listener.OnViewTapListener;
import com.townspriter.android.photobrowser.core.model.extension.IPhotoView;
import com.townspriter.android.photobrowser.core.model.extension.LongPhotoAnalysable;
import com.townspriter.android.photobrowser.core.model.extension.engine.PhotoViewEngine;
import com.townspriter.android.photobrowser.core.model.listener.OnMatrixChangedListener;
import com.townspriter.android.photobrowser.core.model.listener.OnScaleChangeListener;
import com.townspriter.android.photobrowser.core.model.listener.OnScrollListener;
import com.townspriter.base.foundation.utils.log.Logger;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;

/******************************************************************************
 * @path PhotoViewProxy
 * @describe 图片显示器代理
 * @author 张飞
 * @email zhangfei@personedu.com
 * @date 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class PhotoViewProxy extends AppCompatImageView implements IPhotoView
{
    private final String TAG="PhotoViewProxy";
    private IPhotoView mEngine;
    
    public PhotoViewProxy(Context context)
    {
        this(context,null);
    }
    
    public PhotoViewProxy(Context context,AttributeSet attrs)
    {
        this(context,attrs,0);
    }
    
    public PhotoViewProxy(Context context,AttributeSet attrs,int defStyleAttr)
    {
        super(context,attrs,defStyleAttr);
        init();
    }
    
    /**************************************** 公有方法 ****************************************/
    public void changeEngine(IPhotoView engine)
    {
        this.mEngine=engine;
    }
    
    /**************************************** ImageView ****************************************/
    @Override
    public void setImageDrawable(Drawable drawable)
    {
        super.setImageDrawable(drawable);
        if(null!=mEngine&&null!=drawable)
        {
            mEngine.update();
        }
    }
    
    @Override
    public void setImageResource(int resId)
    {
        super.setImageResource(resId);
        if(null!=mEngine)
        {
            mEngine.update();
        }
    }
    
    @Override
    public void setImageURI(Uri uri)
    {
        super.setImageURI(uri);
        if(null!=mEngine&&null!=uri)
        {
            mEngine.update();
        }
    }
    
    @Override
    protected void onAttachedToWindow()
    {
        init();
        super.onAttachedToWindow();
    }
    
    @Override
    protected void onDetachedFromWindow()
    {
        mEngine.clean();
        super.onDetachedFromWindow();
    }
    
    /**************************************** IPhotoView ****************************************/
    @Override
    public ImageView getImageView()
    {
        return mEngine.getImageView();
    }
    
    /**
     * @return IPhotoView
     */
    @Override
    public IPhotoView getPhotoView()
    {
        return mEngine.getPhotoView();
    }
    
    /**
     * 获取图片类型
     *
     * @return
     * @0 普通图片
     * @1 动态图片
     * @2 大长图
     */
    @Override
    public int getImageType()
    {
        return mEngine.getImageType();
    }
    
    /**
     * 设置图片类型
     *
     * @param imageType
     * @0 普通图片
     * @1 动态图片
     * @2 大长图
     */
    @Override
    public void setImageType(int imageType)
    {
        mEngine.setImageType(imageType);
    }
    
    /**
     * 重置图片状态
     */
    @Override
    public void resetStatus()
    {
        mEngine.resetStatus();
    }
    
    @Override
    public void update()
    {
        Logger.i(TAG,"update");
    }
    
    @Override
    public void clean()
    {
        Logger.i(TAG,"clean");
    }
    
    /**
     * 获得图片Y方向滑动的距离
     *
     * @return
     */
    @Override
    public int getTranslateY()
    {
        return mEngine.getTranslateY();
    }
    
    @Override
    public void setAllowParentInterceptOnEdge(boolean allow)
    {
        mEngine.setAllowParentInterceptOnEdge(allow);
    }
    
    /**
     * 检测图片是否允许缩放
     *
     * @return
     */
    @Override
    public boolean canZoom()
    {
        return mEngine.canZoom();
    }
    
    @Override
    public void setZoomable(boolean zoomable)
    {
        mEngine.setZoomable(zoomable);
    }
    
    @Override
    public void setZoomTransitionDuration(int milliseconds)
    {
        mEngine.setZoomTransitionDuration(milliseconds);
    }
    
    /**
     * 获取图片相对于原图的显示区域
     *
     * @return
     */
    @Override
    public RectF getDisplayRect()
    {
        return mEngine.getDisplayRect();
    }
    
    /**
     * 获取图片相对于原图的显示矩阵
     *
     * @return
     */
    @Override
    public Matrix getDisplayMatrix()
    {
        return mEngine.getDisplayMatrix();
    }
    
    /**
     * 设置图片的可显示区域
     *
     * @param displayMatrix
     * @return true 设置成功 false 设置失败
     */
    @Override
    public boolean setDisplayMatrix(Matrix displayMatrix)
    {
        return mEngine.setDisplayMatrix(displayMatrix);
    }
    
    /**
     * 获取大长图当前可见区域的图片
     *
     * @return
     */
    @Override
    public Bitmap getVisibleRectangleBitmap()
    {
        return mEngine.getVisibleRectangleBitmap();
    }
    
    /**
     * @return 获取缩放的最小值.依赖ImageView.ScaleType
     */
    @Override
    public float getMinimumScale()
    {
        return mEngine.getMinimumScale();
    }
    
    /**
     * 设置缩放的最小值.依赖ImageView.ScaleType
     *
     * @param minimumScale
     */
    @Override
    public void setMinimumScale(float minimumScale)
    {
        mEngine.setMinimumScale(minimumScale);
    }
    
    @Override
    public float getMediumScale()
    {
        return mEngine.getMediumScale();
    }
    
    @Override
    public void setMediumScale(float mediumScale)
    {
        mEngine.setMediumScale(mediumScale);
    }
    
    @Override
    public float getMaximumScale()
    {
        return mEngine.getMaximumScale();
    }
    
    @Override
    public void setMaximumScale(float maximumScale)
    {
        mEngine.setMaximumScale(maximumScale);
    }
    
    /**
     * 设置三种缩放等级的上限值
     *
     * @param minimumScale
     * @param mediumScale
     * @param maximumScale
     */
    @Override
    public void setScaleLevel(float minimumScale,float mediumScale,float maximumScale)
    {
        mEngine.setScaleLevel(minimumScale,mediumScale,maximumScale);
    }
    
    @Override
    public void setScaleTypeSafely(ScaleType scaleType)
    {
        if(null!=mEngine)
        {
            mEngine.setScaleTypeSafely(scaleType);
        }
        else
        {
            Logger.w(TAG,"setScaleTypeSafely:NULL");
        }
    }
    
    /**
     * 获取图片的当前缩放值
     *
     * @return
     */
    @Override
    public float getScale()
    {
        return mEngine.getScale();
    }
    
    /**
     * 设置图片的当前缩放值
     *
     * @param scale
     * @return
     */
    @Override
    public void setScale(float scale)
    {
        mEngine.setScale(scale);
    }
    
    /**
     * 设置缩放
     *
     * @param scale
     * 缩放数值
     * @param animate
     * 是否使用缩放动画
     */
    @Override
    public void setScale(float scale,boolean animate)
    {
        mEngine.setScale(scale,animate);
    }
    
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
    @Override
    public void setScale(float scale,float focusX,float focusY,boolean animate)
    {
        mEngine.setScale(scale,focusX,focusY,animate);
    }
    
    @Override
    public void setRotationTo(float rotationDegree)
    {
        mEngine.setRotationTo(rotationDegree);
    }
    
    @Override
    public void setRotationBy(float rotationDegree)
    {
        mEngine.setRotationBy(rotationDegree);
    }
    
    @Override
    public LongPhotoAnalysable getLongPhotoAnalysator()
    {
        return mEngine.getLongPhotoAnalysator();
    }
    
    @Override
    public void setLongPhotoAnalysator(LongPhotoAnalysable analysator)
    {
        mEngine.setLongPhotoAnalysator(analysator);
    }
    
    @Override
    public void setGestureListener(OnGestureListener listener)
    {
        mEngine.setGestureListener(listener);
    }
    
    @Override
    public void setLongClickListener(OnLongClickListener listener)
    {
        mEngine.setLongClickListener(listener);
    }
    
    @Override
    public void setMatrixChangeListener(OnMatrixChangedListener listener)
    {
        mEngine.setMatrixChangeListener(listener);
    }
    
    @Override
    public void setDoubleClickListener(GestureDetector.OnDoubleTapListener listener)
    {
        mEngine.setDoubleClickListener(listener);
    }
    
    @Override
    public void setScaleChangeListener(OnScaleChangeListener listener)
    {
        mEngine.setScaleChangeListener(listener);
    }
    
    @Override
    public void setScrollListener(OnScrollListener listener)
    {
        mEngine.setScrollListener(listener);
    }
    
    @Override
    public OnViewTapListener getViewTapListener()
    {
        return mEngine.getViewTapListener();
    }
    
    @Override
    public void setViewTapListener(OnViewTapListener listener)
    {
        mEngine.setViewTapListener(listener);
    }
    
    @Override
    public OnPhotoTapListener getPhotoTapListener()
    {
        return mEngine.getPhotoTapListener();
    }
    
    @Override
    public void setPhotoTapListener(OnPhotoTapListener listener)
    {
        mEngine.setPhotoTapListener(listener);
    }
    
    /**************************************** 私有方法 ****************************************/
    private void init()
    {
        if(null==mEngine||null==mEngine.getImageView())
        {
            mEngine=new PhotoViewEngine(this);
        }
    }
}
