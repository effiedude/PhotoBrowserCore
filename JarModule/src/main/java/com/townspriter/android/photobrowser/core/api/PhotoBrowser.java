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
import com.townspriter.android.photobrowser.core.model.listener.OnScrollListener;
import com.townspriter.android.photobrowser.core.model.util.LogUtil;
import com.townspriter.android.photobrowser.core.model.view.PhotoViewCompat;
import com.townspriter.android.photobrowser.core.model.view.PhotoViewLayout;
import com.townspriter.android.photobrowser.core.model.view.PhotoViewPager;
import com.townspriter.base.foundation.utils.collection.CollectionUtil;
import com.townspriter.base.foundation.utils.log.Logger;
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
 * @Path PhotoBrowserCore:PhotoBrowser
 * @Describe 图片浏览器入口类
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class PhotoBrowser extends FrameLayout
{
    public static ViewUtils.Orientation mCurrentOrientation;
    private final String TAG="PhotoBrowser";
    private @Nullable List<BrowserImageBean> mPhotoViewBeans;
    private PhotoViewPager mViewPager;
    private PhotoViewPagerAdapter mAdapter;
    private UICallback mCallback;
    private @Nullable ViewGroup mBrowserLoadFailedPage;
    private @Nullable ViewGroup mBrowserOfflinePage;
    private @Nullable ViewGroup mBrowserLoadingPage;
    private @Nullable ViewGroup mBrowserNetworkErrorPage;
    private @Nullable ViewGroup mTopBar;
    private @Nullable ViewGroup mBottomBar;
    private @Nullable ViewGroup mToolbar;
    private int mCurrentPageIndex;
    private final ViewPager.OnPageChangeListener mPageChangeListener=new ViewPager.OnPageChangeListener()
    {
        @Override
        public void onPageScrolled(final int position,final float positionOffset,final int positionOffsetPixels)
        {
            LogUtil.logD(TAG, "onPageScrolled-position:"+position);
        }
        
        @Override
        public void onPageSelected(final int index)
        {
            LogUtil.logD(TAG, "onPageSelected-index:"+index);
            if(mPhotoViewBeans==null||mPhotoViewBeans.get(index)==null)
            {
                LogUtil.logW(TAG,"onPageSelected-mPhotoViewBeans:NULL");
                return;
            }
            PhotoViewLayout photoViewLayout=null;
            if(mAdapter!=null)
            {
                photoViewLayout=(PhotoViewLayout)mAdapter.getPrimaryItem();
            }
            if(photoViewLayout==null)
            {
                LogUtil.logW(TAG,"onPageSelected-photoViewLayout:NULL");
                return;
            }
            // Glide.with(getContext()).load(mPhotoViewBeans.get(index).url).apply(bitmapTransform(new BlurTransformation(getContext()))).into(mBlurView);
            // 重新进入页面之后恢复到原始缩放矩阵
            final PhotoViewCompat photoViewCompat=photoViewLayout.getPhotoView();
            if(photoViewCompat!=null)
            {
                photoViewCompat.resetMatrix();
            }
            else
            {
                LogUtil.logW(TAG,"onPageSelected-photoViewCompat:NULL");
            }
            mCurrentPageIndex=index;
            if(null!=mCallback)
            {
                mCallback.onPageChanged(mAdapter.getCount(),mCurrentPageIndex+1);
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
    }
    
    public void bindData(@Nullable BrowserArticleItem photoViewData)
    {
        if(photoViewData!=null)
        {
            dealWithData(photoViewData);
        }
        else
        {
            LogUtil.logW(TAG,"bindData:NULL");
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
        else
        {
            LogUtil.logW(TAG,"reloadPhoto-mAdapter:NULL");
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
            if(mCurrentPageIndex < mAdapter.getCount())
            {
                if(null!=mCallback)
                {
                    mCallback.onPageChanged(mAdapter.getCount()-1,mCurrentPageIndex+1);
                }
            }
            mAdapter.deleteItem(position);
        }
    }
    
    /**
     * showLoadingView
     * 所有页面最上层的加载中页面
     *
     * @param visibility
     * View.GONE View.VISIBLE
     */
    public void showLoadingView(int visibility)
    {
        if(null!=mBrowserLoadingPage)
        {
            mBrowserLoadingPage.setVisibility(visibility);
        }
    }
    
    /**
     * showLoadFailedView
     * 所有页面最上层的加载失败页面
     *
     * @param visibility
     * View.GONE View.VISIBLE
     */
    public void showLoadFailedView(int visibility)
    {
        if(null!=mBrowserLoadFailedPage)
        {
            mBrowserLoadFailedPage.setVisibility(visibility);
        }
        else
        {
            LogUtil.logW(TAG,"showLoadFailedView-mBrowserLoadFailedPage:NULL");
        }
    }
    
    /**
     * showOfflinePage
     * 显示图集下线页面
     *
     * @param visibility
     * GONE VISIBLE
     */
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
        /** 图片的索引从0开始.检查索引值.限制在合理的范围内 */
        mCurrentPageIndex=browserArticleItem.imageIndex;
        Logger.d(TAG,"dealWithData-mCurrentPageIndex:"+mCurrentPageIndex);
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
            mTopBar=overlay.createTopLayout();
            mBottomBar=overlay.createBottomLayout();
            mToolbar=overlay.createToolbarLayout();
            // 顶部布局
            if(null!=mTopBar)
            {
                LayoutParams topParams=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
                topParams.gravity=Gravity.TOP;
                addView(mTopBar,topParams);
            }
            else
            {
                LogUtil.logD(TAG,"initPhotoBrowserOverlay-mTopBar:NULL");
            }
            // 底部布局
            if(null!=mBottomBar)
            {
                LayoutParams bottomParams=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
                bottomParams.gravity=Gravity.BOTTOM;
                // 如果存在工具栏.则将底部栏放到工具栏上面
                if(null!=mToolbar)
                {
                    bottomParams.bottomMargin=(int)getResources().getDimension(R.dimen.browserxcorexdpx53);
                }
                addView(mBottomBar,bottomParams);
                mBottomBar.setOnTouchListener(mInfoTouchListener);
            }
            else
            {
                LogUtil.logD(TAG,"initPhotoBrowserOverlay-mBottomBar:NULL");
            }
            // 工具布局(评论/收藏/分享)
            if(null!=mToolbar)
            {
                LayoutParams toolbarParams=new LayoutParams(LayoutParams.MATCH_PARENT,(int)getResources().getDimension(R.dimen.browserxcorexdpx53));
                toolbarParams.gravity=Gravity.BOTTOM;
                addView(mToolbar,toolbarParams);
            }
            else
            {
                LogUtil.logD(TAG,"initPhotoBrowserOverlay-mToolbar:NULL");
            }
            // 图集加载失败页面
            if(null!=mBrowserLoadFailedPage)
            {
                addView(mBrowserLoadFailedPage);
                mBrowserLoadFailedPage.setVisibility(GONE);
            }
            else
            {
                LogUtil.logD(TAG,"initPhotoBrowserOverlay-mBrowserLoadFailedPage:NULL");
            }
            // 图集下线页面
            if(null!=mBrowserOfflinePage)
            {
                addView(mBrowserOfflinePage);
                mBrowserOfflinePage.setVisibility(GONE);
            }
            else
            {
                LogUtil.logD(TAG,"initPhotoBrowserOverlay-mBrowserOfflinePage:NULL");
            }
            // 图集网络错误页面
            if(null!=mBrowserNetworkErrorPage)
            {
                addView(mBrowserNetworkErrorPage);
                mBrowserNetworkErrorPage.setVisibility(GONE);
            }
            else
            {
                LogUtil.logD(TAG,"initPhotoBrowserOverlay-mBrowserNetworkErrorPage:NULL");
            }
            // 图集加载页面
            if(null!=mBrowserLoadingPage)
            {
                addView(mBrowserLoadingPage);
            }
            else
            {
                LogUtil.logD(TAG,"initPhotoBrowserOverlay-mBrowserLoadingPage:NULL");
            }
            mAdapter.setPhotoBrowserOverlay(overlay);
        }
        else
        {
            LogUtil.logW(TAG,"initPhotoBrowserOverlay-overlay:NULL");
        }
    }
}
