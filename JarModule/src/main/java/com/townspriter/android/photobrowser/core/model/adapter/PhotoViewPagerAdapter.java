package com.townspriter.android.photobrowser.core.model.adapter;

import java.util.List;
import com.townspriter.android.foundation.utils.collection.CollectionUtil;
import com.townspriter.android.foundation.utils.log.Logger;
import com.townspriter.android.foundation.utils.ui.ResHelper;
import com.townspriter.android.photobrowser.core.api.bean.BrowserImageBean;
import com.townspriter.android.photobrowser.core.api.listener.OnGestureListener;
import com.townspriter.android.photobrowser.core.api.listener.OnPhotoTapListener;
import com.townspriter.android.photobrowser.core.api.listener.OnViewTapListener;
import com.townspriter.android.photobrowser.core.api.listener.UICallback;
import com.townspriter.android.photobrowser.core.api.view.IPhotoBrowserOverlay;
import com.townspriter.android.photobrowser.core.model.listener.OnScrollListener;
import com.townspriter.android.photobrowser.core.model.view.PhotoViewCompat;
import com.townspriter.android.photobrowser.core.model.view.PhotoViewLayout;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

/******************************************************************************
 * @Path PhotoBrowserCore:PhotoViewPagerAdapter
 * @Describe 图片滑动器适配器
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class PhotoViewPagerAdapter extends PagerAdapter
{
    private final String TAG="PhotoViewPagerAdapter";
    private final Context mContext;
    private List<BrowserImageBean> mPhotoViewBeans;
    private View mCurrentView;
    private IPhotoBrowserOverlay mBrowserOverlay;
    private UICallback mCallback;
    private OnGestureListener mGestureListener;
    private View.OnLongClickListener mLongClickListener;
    private OnScrollListener mScrollListener;
    private OnViewTapListener mViewTapListener;
    private OnPhotoTapListener mPhotoTapListener;
    
    public PhotoViewPagerAdapter(Context context)
    {
        mContext=context;
    }
    
    public void setViewData(@NonNull List<BrowserImageBean> imageBeans)
    {
        mPhotoViewBeans=imageBeans;
    }
    
    public void setPhotoBrowserOverlay(@NonNull IPhotoBrowserOverlay browserOverlay)
    {
        this.mBrowserOverlay=browserOverlay;
    }
    
    public void setUICallback(@Nullable UICallback callback)
    {
        if(null!=callback)
        {
            mCallback=callback;
        }
    }
    
    public void reloadPhoto(@NonNull BrowserImageBean photoViewBean)
    {
        PhotoViewLayout photoViewLayout=(PhotoViewLayout)mCurrentView;
        if(photoViewLayout==null)
        {
            Logger.w(TAG,"reloadPhoto-photoViewLayout:NULL");
            return;
        }
        // 重新进入页面之后恢复到原始缩放矩阵
        if(photoViewLayout.getPhotoView()==null)
        {
            Logger.w(TAG,"reloadPhoto-photoViewLayout.getPhotoView():NULL");
            photoViewLayout.bindPhotoView();
        }
        photoViewLayout.getPhotoView().bindData(photoViewBean);
    }
    
    public View getPrimaryItem()
    {
        return mCurrentView;
    }
    
    public void setGestureListener(OnGestureListener listener)
    {
        mGestureListener=listener;
    }
    
    public void setLongClickListener(View.OnLongClickListener listener)
    {
        mLongClickListener=listener;
    }
    
    public void setScrollListener(OnScrollListener listener)
    {
        mScrollListener=listener;
    }
    
    public void setViewTapListener(OnViewTapListener listener)
    {
        mViewTapListener=listener;
    }
    
    public void setPhotoTapListener(OnPhotoTapListener listener)
    {
        mPhotoTapListener=listener;
    }
    
    @Override
    public int getCount()
    {
        return mPhotoViewBeans==null?0:mPhotoViewBeans.size();
    }
    
    @Override
    public boolean isViewFromObject(@NonNull View view,@NonNull Object object)
    {
        return view==object;
    }
    
    @Override
    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container,int position)
    {
        PhotoViewLayout photoViewLayout=new PhotoViewLayout(mContext,mBrowserOverlay);
        if(!CollectionUtil.isEmpty(mPhotoViewBeans)&&position<mPhotoViewBeans.size())
        {
            ViewGroup.LayoutParams photoViewLayoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            photoViewLayout.setLayoutParams(photoViewLayoutParams);
            container.addView(photoViewLayout);
            displayPhotoView(mPhotoViewBeans.get(position),photoViewLayout);
        }
        else
        {
            Logger.w(TAG,"instantiateItem:FAIL");
        }
        return photoViewLayout;
    }
    
    @Override
    public void destroyItem(@NonNull ViewGroup container,int position,@NonNull Object object)
    {
        Logger.i(TAG,"destroyItem:"+position);
        if(object instanceof PhotoViewLayout)
        {
            PhotoViewLayout target=(PhotoViewLayout)object;
            target.clearPhotoView();
            container.removeView(target);
        }
    }
    
    @Override
    public void setPrimaryItem(@NonNull ViewGroup container,int position,@NonNull Object object)
    {
        Logger.i(TAG,"setPrimaryItem:"+position);
        mCurrentView=(View)object;
        // 切换页面之后在此处更新当前的图片并回调
        if(null!=mCallback)
        {
            mCallback.onPhotoSelected(getPhoto());
        }
    }
    
    @Override
    public int getItemPosition(@NonNull Object object)
    {
        // 解决重新加载时数据不刷新的问题
        return POSITION_NONE;
    }
    
    private void displayPhotoView(BrowserImageBean photoViewBean,PhotoViewLayout photoViewLayout)
    {
        if(photoViewBean==null)
        {
            Logger.w(TAG,"displayPhotoView-photoViewBean:NULL");
            return;
        }
        if(photoViewLayout.getPhotoView()==null)
        {
            photoViewLayout.bindPhotoView();
        }
        photoViewLayout.getPhotoView().bindData(photoViewBean);
        if(null!=mGestureListener)
        {
            photoViewLayout.getPhotoView().setGestureListener(mGestureListener);
        }
        if(null!=mLongClickListener)
        {
            photoViewLayout.getPhotoView().setLongClickListener(mLongClickListener);
        }
        if(null!=mScrollListener)
        {
            photoViewLayout.getPhotoView().setScrollListener(mScrollListener);
        }
        if(null!=mViewTapListener)
        {
            photoViewLayout.getPhotoView().setViewTapListener(mViewTapListener);
        }
        if(null!=mPhotoTapListener)
        {
            photoViewLayout.getPhotoView().setPhotoTapListener(mPhotoTapListener);
        }
    }
    
    private @Nullable Drawable getPhoto()
    {
        PhotoViewLayout photoViewLayout=(PhotoViewLayout)mCurrentView;
        if(photoViewLayout==null)
        {
            Logger.w(TAG,"getPhoto-photoViewLayout:NULL");
            return null;
        }
        // 重新进入页面之后恢复到原始缩放矩阵
        PhotoViewCompat photoViewCompat=photoViewLayout.getPhotoView();
        if(photoViewCompat!=null)
        {
            return photoViewCompat.getDrawable();
        }
        else
        {
            Logger.w(TAG,"getPhoto-photoViewCompat:NULL");
        }
        return null;
    }
}
