package com.townspriter.android.photobrowser.core.model.adapter;

import java.util.List;

import com.townspriter.android.photobrowser.core.api.bean.BrowserImageBean;
import com.townspriter.android.photobrowser.core.api.bean.ImageBean;
import com.townspriter.android.photobrowser.core.api.listener.OnGestureListener;
import com.townspriter.android.photobrowser.core.api.listener.OnPhotoTapListener;
import com.townspriter.android.photobrowser.core.api.listener.OnViewTapListener;
import com.townspriter.android.photobrowser.core.api.listener.UICallback;
import com.townspriter.android.photobrowser.core.api.view.IPhotoBrowserOverlay;
import com.townspriter.android.photobrowser.core.model.listener.IVideoPlayer;
import com.townspriter.android.photobrowser.core.model.listener.OnScrollListener;
import com.townspriter.android.photobrowser.core.model.view.MediaViewLayout;
import com.townspriter.android.photobrowser.core.model.view.PhotoViewCompat;
import com.townspriter.base.foundation.utils.collection.CollectionUtil;
import com.townspriter.base.foundation.utils.log.Logger;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

/******************************************************************************
 * @path PhotoViewPagerAdapter
 * @describe 图片滑动器适配器
 * @author 张飞
 * @email zhangfei@personedu.com
 * @date 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class PhotoViewPagerAdapter extends PagerAdapter
{
    private final String TAG="PhotoViewPagerAdapter";
    private final Context mContext;
    private int lastPosition=-1;
    private List<BrowserImageBean> mPhotoViewBeans;
    private View mCurrentView;
    private IPhotoBrowserOverlay mBrowserOverlay;
    private UICallback mCallback;
    private OnGestureListener mGestureListener;
    private View.OnLongClickListener mLongClickListener;
    private OnScrollListener mScrollListener;
    private OnViewTapListener mViewTapListener;
    private OnPhotoTapListener mPhotoTapListener;
    private @Nullable IVideoPlayer videoPlayer;
    
    public PhotoViewPagerAdapter(Context context)
    {
        mContext=context;
    }
    
    public void deleteItem(int position)
    {
        if(!CollectionUtil.isEmpty(mPhotoViewBeans))
        {
            mPhotoViewBeans.remove(position);
            notifyDataSetChanged();
        }
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
        MediaViewLayout mediaViewLayout=(MediaViewLayout)mCurrentView;
        if(mediaViewLayout==null)
        {
            return;
        }
        // 重新进入页面之后恢复到原始缩放矩阵
        if(mediaViewLayout.getPhotoView()==null)
        {
            mediaViewLayout.bindPhotoView();
        }
        mediaViewLayout.getPhotoView().bindData(photoViewBean);
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
    
    public @Nullable IVideoPlayer getVideoPlayer()
    {
        return videoPlayer;
    }
    
    public void setVideoPlayer(@Nullable IVideoPlayer videoPlayer)
    {
        this.videoPlayer=videoPlayer;
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
        Logger.d(TAG,"instantiateItem:"+position);
        MediaViewLayout mediaViewLayout=new MediaViewLayout(mContext,mBrowserOverlay,mPhotoViewBeans.get(position));
        if(!CollectionUtil.isEmpty(mPhotoViewBeans)&&position<mPhotoViewBeans.size())
        {
            ViewGroup.LayoutParams photoViewLayoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            mediaViewLayout.setLayoutParams(photoViewLayoutParams);
            container.addView(mediaViewLayout);
            if(ImageBean.MEDIAxTYPExVIDEO.equals(mPhotoViewBeans.get(position).mediaType)&&videoPlayer!=null)
            {
                videoPlayer.bindData(mPhotoViewBeans.get(position).url);
                mediaViewLayout.addVideoView(videoPlayer);
            }
            else
            {
                displayPhotoView(mPhotoViewBeans.get(position),mediaViewLayout);
            }
        }
        return mediaViewLayout;
    }
    
    @Override
    public void destroyItem(@NonNull ViewGroup container,int position,@NonNull Object object)
    {
        Logger.d(TAG,"destroyItem:"+position);
        if(object instanceof MediaViewLayout&&position<mPhotoViewBeans.size()-1)
        {
            MediaViewLayout target=(MediaViewLayout)object;
            if(ImageBean.MEDIAxTYPExVIDEO.equals(mPhotoViewBeans.get(position).mediaType)&&videoPlayer==null)
            {
                target.clearVideoView();
            }
            else
            {
                target.clearPhotoView();
            }
            container.removeView(target);
        }
    }
    
    @Override
    public void setPrimaryItem(@NonNull ViewGroup container,int position,@NonNull Object object)
    {
        if(lastPosition!=position)
        {
            Logger.d(TAG,"setPrimaryItem:"+position);
            lastPosition=position;
            mCurrentView=(View)object;
            if(object instanceof MediaViewLayout)
            {
                MediaViewLayout target=(MediaViewLayout)object;
                if(ImageBean.MEDIAxTYPExVIDEO.equals(mPhotoViewBeans.get(position).mediaType)&&videoPlayer!=null)
                {
                    if(target.getVideoView()!=null&&videoPlayer!=null)
                    {
                        videoPlayer.bindView(target.getVideoView());
                        videoPlayer.play(mPhotoViewBeans.get(position).url);
                    }
                }
                else
                {
                    if(videoPlayer!=null)
                    {
                        videoPlayer.unbindView();
                        videoPlayer.stop();
                    }
                    // 切换页面之后在此处更新当前的图片并回调
                    if(null!=mCallback)
                    {
                        mCallback.onPhotoSelected(getPhoto());
                    }
                }
            }
        }
    }
    
    @Override
    public int getItemPosition(@NonNull Object object)
    {
        if(object instanceof MediaViewLayout)
        {
            MediaViewLayout mediaViewLayout=(MediaViewLayout)object;
            int index=mPhotoViewBeans.indexOf(mediaViewLayout.getData());
            if(index!=-1)
            {
                return index;
            }
            else
            {
                return POSITION_NONE;
            }
        }
        return super.getItemPosition(object);
    }

    private void displayPhotoView(BrowserImageBean photoViewBean,MediaViewLayout mediaViewLayout)
    {
        if(photoViewBean==null)
        {
            return;
        }
        if(mediaViewLayout.getPhotoView()==null)
        {
            mediaViewLayout.bindPhotoView();
        }
        mediaViewLayout.getPhotoView().bindData(photoViewBean);
        if(null!=mGestureListener)
        {
            mediaViewLayout.getPhotoView().setGestureListener(mGestureListener);
        }
        if(null!=mLongClickListener)
        {
            mediaViewLayout.getPhotoView().setLongClickListener(mLongClickListener);
        }
        if(null!=mScrollListener)
        {
            mediaViewLayout.getPhotoView().setScrollListener(mScrollListener);
        }
        if(null!=mViewTapListener)
        {
            mediaViewLayout.getPhotoView().setViewTapListener(mViewTapListener);
        }
        if(null!=mPhotoTapListener)
        {
            mediaViewLayout.getPhotoView().setPhotoTapListener(mPhotoTapListener);
        }
    }
    
    private @Nullable Drawable getPhoto()
    {
        MediaViewLayout mediaViewLayout=(MediaViewLayout)mCurrentView;
        if(mediaViewLayout==null)
        {
            return null;
        }
        // 重新进入页面之后恢复到原始缩放矩阵
        PhotoViewCompat photoViewCompat=mediaViewLayout.getPhotoView();
        if(photoViewCompat!=null)
        {
            return photoViewCompat.getDrawable();
        }
        return null;
    }
}
