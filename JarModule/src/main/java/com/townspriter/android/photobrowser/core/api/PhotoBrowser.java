package com.townspriter.android.photobrowser.core.api;

import java.util.List;

import com.townspriter.android.photobrowser.core.R;
import com.townspriter.android.photobrowser.core.api.bean.BrowserArticleItem;
import com.townspriter.android.photobrowser.core.api.bean.BrowserImageBean;
import com.townspriter.android.photobrowser.core.api.listener.OnGestureListener;
import com.townspriter.android.photobrowser.core.api.listener.OnPhotoTapListener;
import com.townspriter.android.photobrowser.core.api.listener.OnViewTapListener;
import com.townspriter.android.photobrowser.core.api.listener.UICallback;
import com.townspriter.android.photobrowser.core.api.view.IPhotoBrowserOverlay;
import com.townspriter.android.photobrowser.core.model.adapter.PhotoViewPagerAdapter;
import com.townspriter.android.photobrowser.core.model.listener.IVideoPlayer;
import com.townspriter.android.photobrowser.core.model.listener.OnScrollListener;
import com.townspriter.android.photobrowser.core.model.util.LogUtil;
import com.townspriter.android.photobrowser.core.model.view.MediaViewLayout;
import com.townspriter.android.photobrowser.core.model.view.PhotoViewCompat;
import com.townspriter.android.photobrowser.core.model.view.PhotoViewPager;
import com.townspriter.base.foundation.utils.collection.CollectionUtil;
import com.townspriter.base.foundation.utils.ui.ViewUtils;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/******************************************************************************
 * @path PhotoBrowser
 * @describe 图片浏览器入口类
 * @author 张飞
 * @email zhangfei@personedu.com
 * @date 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class PhotoBrowser extends FrameLayout
{
    private static final String TAG="PhotoBrowser";
    public static ViewUtils.Orientation mCurrentOrientation;
    private @Nullable List<BrowserImageBean> mPhotoViewBeans;
    private PhotoViewPager mViewPager;
    private PhotoViewPagerAdapter mAdapter;
    private UICallback mCallback;
    private @Nullable ViewGroup mBrowserLoadFailedPage;
    private @Nullable ViewGroup mBrowserOfflinePage;
    private @Nullable ViewGroup mBrowserLoadingPage;
    private @Nullable ViewGroup mBrowserNetworkErrorPage;
    private @Nullable ViewGroup mBottomBar;
    private int mCurrentPageIndex;
    private final ViewPager.OnPageChangeListener mPageChangeListener=new ViewPager.OnPageChangeListener()
    {
        @Override
        public void onPageScrolled(final int position,final float positionOffset,final int positionOffsetPixels)
        {}
        
        @Override
        public void onPageSelected(final int index)
        {
            LogUtil.logD(TAG,"onPageSelected-index:"+index);
            if(mPhotoViewBeans==null||mPhotoViewBeans.get(index)==null)
            {
                return;
            }
            MediaViewLayout mediaViewLayout;
            if(mAdapter!=null)
            {
                mediaViewLayout=(MediaViewLayout)mAdapter.getPrimaryItem();
                if(mediaViewLayout!=null)
                {
                    // 重新进入页面之后恢复到原始缩放矩阵
                    final PhotoViewCompat photoViewCompat=mediaViewLayout.getPhotoView();
                    if(photoViewCompat!=null)
                    {
                        photoViewCompat.resetMatrix();
                    }
                }
                mCurrentPageIndex=index;
                if(mCallback!=null)
                {
                    mCallback.onPageChanged(mAdapter.getCount(),mCurrentPageIndex+1);
                }
            }
        }
        
        @Override
        public void onPageScrollStateChanged(final int state)
        {}
    };
    private float mLastX;
    private float mLastY;
    private MotionEvent mLastDownMotionEvent;
    private final OnTouchListener mInfoTouchListener=new OnTouchListener()
    {
        @Override
        public boolean onTouch(final View view,final MotionEvent motionEvent)
        {
            float currentX=motionEvent.getX();
            float currentY=motionEvent.getY();
            switch(motionEvent.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    mLastX=currentX;
                    mLastY=currentY;
                    mLastDownMotionEvent=MotionEvent.obtain(motionEvent);
                    mCurrentOrientation=ViewUtils.Orientation.NONE;
                    break;
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // 滑动期间没有获取到滑动方向的前提下.先计算滑动方向.根据滑动方向下发按下事件到具体的控件
                    if(mCurrentOrientation==ViewUtils.Orientation.NONE)
                    {
                        mCurrentOrientation=ViewUtils.getScrollOrientation(getContext(),currentX-mLastX,currentY-mLastY);
                        switch(mCurrentOrientation)
                        {
                            case TOP:
                            case BOTTOM:
                                // 上下方向将模拟出来的按下事件交给文字控件处理.不再往下传递触摸事件
                                // 如果没有在文字控件范围之内则继续往下传递按下事件
                                if(null!=mBottomBar)
                                {
                                    mBottomBar.dispatchTouchEvent(mLastDownMotionEvent);
                                }
                                break;
                            case LEFT:
                            case RIGHT:
                                // 左右方向将模拟出来的按下事件交给图片控件处理.不再往下传递触摸事件
                                if(null!=mViewPager)
                                {
                                    mViewPager.dispatchTouchEvent(mLastDownMotionEvent);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    else
                    {
                        // 获取可用的滑动方向之后(上下或左右).根据滑动方向下发当前事件到具体的控件
                        switch(mCurrentOrientation)
                        {
                            case TOP:
                            case BOTTOM:
                                if(null!=mBottomBar)
                                {
                                    mBottomBar.dispatchTouchEvent(motionEvent);
                                }
                                break;
                            case LEFT:
                            case RIGHT:
                                if(null!=mViewPager)
                                {
                                    mViewPager.dispatchTouchEvent(motionEvent);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    };
    
    public PhotoBrowser(Context context)
    {
        this(context,null,0);
    }
    
    public PhotoBrowser(Context context,AttributeSet attrs)
    {
        this(context,attrs,0);
    }
    
    public PhotoBrowser(Context context,AttributeSet attrs,int defStyleAttr)
    {
        super(context,attrs,defStyleAttr);
        initView();
    }
    
    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        mViewPager.removeOnPageChangeListener(mPageChangeListener);
        if(mAdapter.getVideoPlayer()!=null)
        {
            mAdapter.getVideoPlayer().destroy();
        }
    }
    
    public void bindData(@Nullable BrowserArticleItem photoViewData)
    {
        if(photoViewData!=null)
        {
            dealWithData(photoViewData);
        }
    }
    
    public void bindView(IPhotoBrowserOverlay overlay,UICallback callback)
    {
        bindView(overlay);
        setCallback(callback);
    }
    
    public void bindView(IPhotoBrowserOverlay overlay)
    {
        initPhotoBrowserOverlay(overlay);
    }
    
    public void setCallback(UICallback callback)
    {
        mCallback=callback;
        if(null!=mCallback)
        {
            mCallback.onPageChanged(mAdapter.getCount(),mCurrentPageIndex+1);
        }
        if(mAdapter!=null)
        {
            mAdapter.setUICallback(mCallback);
        }
    }
    
    public void reloadPhoto()
    {
        if(null!=mAdapter&&mPhotoViewBeans!=null&&mPhotoViewBeans.size()>0)
        {
            mAdapter.reloadPhoto(mPhotoViewBeans.get(mCurrentPageIndex));
        }
    }
    
    public @Nullable String getCurrentPhotoUrl()
    {
        if(CollectionUtil.isEmpty(mPhotoViewBeans))
        {
            return null;
        }
        if(mPhotoViewBeans.get(mCurrentPageIndex)==null)
        {
            return null;
        }
        return mPhotoViewBeans.get(mCurrentPageIndex).url;
    }
    
    public void deleteImage(int position)
    {
        if(mAdapter!=null)
        {
            if(mCurrentPageIndex<mAdapter.getCount())
            {
                if(null!=mCallback)
                {
                    mCallback.onPageChanged(mAdapter.getCount()-1,mCurrentPageIndex+1);
                }
            }
            mAdapter.deleteItem(position);
        }
    }
    
    public void setVideoPlayer(@NonNull IVideoPlayer videoPlayer)
    {
        if(mAdapter!=null)
        {
            mAdapter.setVideoPlayer(videoPlayer);
        }
        if(mViewPager!=null)
        {
            mViewPager.setVideoPlayer(videoPlayer);
        }
    }
    
    public long getCurrentVideoDuration()
    {
        if(mAdapter!=null&&mAdapter.getVideoPlayer()!=null)
        {
            return mAdapter.getVideoPlayer().getDuration();
        }
        return 0;
    }
    
    public void showLoadingView(int visibility)
    {
        if(null!=mBrowserLoadingPage)
        {
            mBrowserLoadingPage.setVisibility(visibility);
        }
    }
    
    public void showLoadFailedView(int visibility)
    {
        if(null!=mBrowserLoadFailedPage)
        {
            mBrowserLoadFailedPage.setVisibility(visibility);
        }
    }
    
    public void showOfflinePage(int visibility)
    {
        if(null!=mBrowserOfflinePage)
        {
            mBrowserOfflinePage.setVisibility(visibility);
        }
    }
    
    public void showNetworkErrorPage(int visibility)
    {
        if(null!=mBrowserNetworkErrorPage)
        {
            mBrowserNetworkErrorPage.setVisibility(visibility);
        }
    }
    
    public void setGestureListener(@NonNull OnGestureListener listener)
    {
        if(null!=mAdapter)
        {
            mAdapter.setGestureListener(listener);
        }
    }
    
    public void setScrollListener(OnScrollListener listener)
    {
        if(null!=mAdapter)
        {
            mAdapter.setScrollListener(listener);
        }
    }
    
    public void setLongClickListener(OnLongClickListener listener)
    {
        if(null!=mAdapter)
        {
            mAdapter.setLongClickListener(listener);
        }
    }
    
    public void setViewTapListener(@NonNull OnViewTapListener listener)
    {
        if(null!=mAdapter)
        {
            mAdapter.setViewTapListener(listener);
        }
    }
    
    public void setPhotoTapListener(@NonNull OnPhotoTapListener listener)
    {
        if(null!=mAdapter)
        {
            mAdapter.setPhotoTapListener(listener);
        }
    }
    
    /**************************************** 私有方法 ****************************************/
    private void dealWithData(BrowserArticleItem browserArticleItem)
    {
        mPhotoViewBeans=browserArticleItem.browserImages;
        if(CollectionUtil.isEmpty(mPhotoViewBeans))
        {
            if(null!=mBrowserLoadFailedPage)
            {
                mBrowserLoadFailedPage.setVisibility(VISIBLE);
            }
            return;
        }
        if(mAdapter==null)
        {
            mAdapter=new PhotoViewPagerAdapter(getContext());
        }
        mAdapter.setViewData(mPhotoViewBeans);
        mViewPager.setAdapter(mAdapter);
        /** 图片的索引从零开始检查索引值.限制在合理的范围内 */
        mCurrentPageIndex=browserArticleItem.imageIndex;
        if(mCurrentPageIndex<0)
        {
            mCurrentPageIndex=0;
        }
        else if(mCurrentPageIndex>=mAdapter.getCount())
        {
            mCurrentPageIndex=mAdapter.getCount()-1;
        }
        mViewPager.setCurrentItem(mCurrentPageIndex);
    }
    
    private void initView()
    {
        // 确保监听到物理按键
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        requestFocusFromTouch();
        initViewPager();
        setBackgroundColor(Color.BLACK);
    }
    
    private void initViewPager()
    {
        mViewPager=new PhotoViewPager(getContext());
        LayoutParams mPagerParams=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        mPagerParams.gravity=Gravity.CENTER;
        mViewPager.setLayoutParams(mPagerParams);
        mViewPager.setBackgroundColor(Color.TRANSPARENT);
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mViewPager.setPageMargin((int)getResources().getDimension(R.dimen.browserxcorexdpx10));
        mAdapter=new PhotoViewPagerAdapter(getContext());
        addView(mViewPager);
    }
    
    private void initPhotoBrowserOverlay(IPhotoBrowserOverlay overlay)
    {
        if(null!=overlay)
        {
            mBrowserLoadingPage=overlay.createBrowserLoadingPage();
            mBrowserLoadFailedPage=overlay.createBrowserLoadFailPage();
            mBrowserNetworkErrorPage=overlay.createBrowserNetworkErrorPage();
            mBrowserOfflinePage=overlay.createBrowserOfflinePage();
            ViewGroup mTopBar=overlay.createTopLayout();
            mBottomBar=overlay.createBottomLayout();
            ViewGroup mToolbar=overlay.createToolbarLayout();
            // 顶部布局
            if(null!=mTopBar)
            {
                LayoutParams topParams=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
                topParams.gravity=Gravity.TOP;
                addView(mTopBar,topParams);
            }
            // 底部布局
            if(null!=mBottomBar)
            {
                LayoutParams bottomParams=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
                bottomParams.gravity=Gravity.BOTTOM;
                // 如果存在工具栏则将底部栏放到工具栏上面
                if(null!=mToolbar)
                {
                    bottomParams.bottomMargin=(int)getResources().getDimension(R.dimen.browserxcorexdpx53);
                }
                addView(mBottomBar,bottomParams);
                mBottomBar.setOnTouchListener(mInfoTouchListener);
            }
            // 工具布局(评论收藏分享)
            if(null!=mToolbar)
            {
                LayoutParams toolbarParams=new LayoutParams(LayoutParams.MATCH_PARENT,(int)getResources().getDimension(R.dimen.browserxcorexdpx53));
                toolbarParams.gravity=Gravity.BOTTOM;
                addView(mToolbar,toolbarParams);
            }
            // 图集加载失败页面
            if(null!=mBrowserLoadFailedPage)
            {
                addView(mBrowserLoadFailedPage);
                mBrowserLoadFailedPage.setVisibility(GONE);
            }
            // 图集下线页面
            if(null!=mBrowserOfflinePage)
            {
                addView(mBrowserOfflinePage);
                mBrowserOfflinePage.setVisibility(GONE);
            }
            // 图集网络错误页面
            if(null!=mBrowserNetworkErrorPage)
            {
                addView(mBrowserNetworkErrorPage);
                mBrowserNetworkErrorPage.setVisibility(GONE);
            }
            // 图集加载页面
            if(null!=mBrowserLoadingPage)
            {
                addView(mBrowserLoadingPage);
            }
            mAdapter.setPhotoBrowserOverlay(overlay);
        }
    }
}
