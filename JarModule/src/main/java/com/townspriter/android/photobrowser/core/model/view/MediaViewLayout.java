package com.townspriter.android.photobrowser.core.model.view;

import com.bumptech.glide.Glide;
import com.townspriter.android.photobrowser.core.R;
import com.townspriter.android.photobrowser.core.api.bean.BrowserImageBean;
import com.townspriter.android.photobrowser.core.api.view.IPhotoBrowserOverlay;
import com.townspriter.android.photobrowser.core.model.listener.IVideoPlayer;
import com.townspriter.android.photobrowser.core.model.listener.OnPhotoLoadListener;
import com.townspriter.base.foundation.utils.concurrent.ThreadManager;
import com.townspriter.base.foundation.utils.net.NetworkUtil;
import com.townspriter.base.foundation.utils.ui.ResHelper;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @path MediaViewLayout
 * @describe 容器类
 * @author 张飞
 * @email zhangfei@personedu.com
 * @date 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
@SuppressLint("ViewConstructor")
public class MediaViewLayout extends RelativeLayout implements OnPhotoLoadListener
{
    private final IPhotoBrowserOverlay mBrowserOverlay;
    private PhotoViewCompat mPhotoView;
    private @Nullable VideoView videoView;
    private ViewGroup mFailView;
    private ViewGroup mPhotoNetworkErrorPage;
    private ViewGroup mLoadingView;
    private ViewGroup mBrowserLoadingPage;
    private final BrowserImageBean bean;
    
    public MediaViewLayout(Context context,IPhotoBrowserOverlay browserOverlay,BrowserImageBean bean)
    {
        super(context);
        mBrowserOverlay=browserOverlay;
        this.bean=bean;
        initView();
    }
    
    public BrowserImageBean getData()
    {
        return bean;
    }
    
    public void addVideoView(@NonNull IVideoPlayer videoPlayer)
    {
        setBackgroundColor(Color.BLACK);
        videoView=new VideoView(getContext());
        videoView.setVideoPlayer(videoPlayer);
        LayoutParams layoutParams=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        addView(videoView,layoutParams);
    }
    
    public void bindPhotoView()
    {
        removeAllViews();
        setBackgroundColor(Color.BLACK);
        LayoutParams layoutParams=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        mPhotoView=new PhotoViewCompat(getContext());
        mPhotoView.setBackgroundColor(Color.TRANSPARENT);
        mPhotoView.setPhotoLoadListener(this);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        mPhotoView.setLayoutParams(layoutParams);
        addView(mPhotoView);
        if(null!=mBrowserOverlay)
        {
            // 图片失败界面
            mFailView=mBrowserOverlay.createPhotoLoadFailLayout();
            if(null!=mFailView)
            {
                LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
                params.setMargins(0,ResHelper.getDimenInt(R.dimen.browserxcorexdpx100),0,ResHelper.getDimenInt(R.dimen.browserxcorexdpx100));
                addView(mFailView,params);
                mFailView.setVisibility(GONE);
            }
            // 图片无网页面
            mPhotoNetworkErrorPage=mBrowserOverlay.createPhotoNetworkErrorPage();
            if(null!=mPhotoNetworkErrorPage)
            {
                LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
                params.setMargins(0,ResHelper.getDimenInt(R.dimen.browserxcorexdpx100),0,ResHelper.getDimenInt(R.dimen.browserxcorexdpx100));
                addView(mPhotoNetworkErrorPage,params);
                mPhotoNetworkErrorPage.setVisibility(GONE);
            }
            // 图片加载界面
            mLoadingView=mBrowserOverlay.createPhotoLoadingLayout();
            if(null!=mLoadingView)
            {
                LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
                params.setMargins(0,ResHelper.getDimenInt(R.dimen.browserxcorexdpx100),0,ResHelper.getDimenInt(R.dimen.browserxcorexdpx100));
                addView(mLoadingView,params);
                mLoadingView.setVisibility(GONE);
            }
            // 图集加载页面
            mBrowserLoadingPage=mBrowserOverlay.createBrowserLoadingPage();
        }
    }
    
    public PhotoViewCompat getPhotoView()
    {
        return mPhotoView;
    }
    
    public void clearPhotoView()
    {
        if(mPhotoView!=null)
        {
            removeView(mPhotoView);
            // 停止加载此控件的图片
            Glide.with(getContext()).clear(mPhotoView);
        }
    }
    
    public @Nullable VideoView getVideoView()
    {
        return videoView;
    }
    
    public void clearVideoView()
    {
        if(videoView!=null)
        {
            removeView(videoView);
        }
    }
    
    /**************************************** OnPhotoLoadListener ****************************************/
    @Override
    public void onPhotoLoadFailed()
    {
        ThreadManager.post(ThreadManager.THREADxUI,new Runnable()
        {
            @Override
            public void run()
            {
                if(null!=mLoadingView)
                {
                    mLoadingView.setVisibility(GONE);
                }
                if(null!=mBrowserLoadingPage)
                {
                    mBrowserLoadingPage.setVisibility(GONE);
                }
                if(NetworkUtil.isNetworkConnected())
                {
                    if(null!=mFailView)
                    {
                        mFailView.setVisibility(VISIBLE);
                    }
                }
                else
                {
                    if(null!=mPhotoNetworkErrorPage)
                    {
                        mPhotoNetworkErrorPage.setVisibility(VISIBLE);
                    }
                }
            }
        });
    }
    
    @Override
    public void onPhotoLoadSucceed()
    {
        ThreadManager.post(ThreadManager.THREADxUI,new Runnable()
        {
            @Override
            public void run()
            {
                if(null!=mLoadingView)
                {
                    mLoadingView.setVisibility(GONE);
                }
                if(null!=mBrowserLoadingPage)
                {
                    mBrowserLoadingPage.setVisibility(GONE);
                }
                if(null!=mFailView)
                {
                    mFailView.setVisibility(GONE);
                }
                if(null!=mPhotoNetworkErrorPage)
                {
                    mPhotoNetworkErrorPage.setVisibility(GONE);
                }
            }
        });
    }
    
    @Override
    public void onPhotoLoading(int progress)
    {
        ThreadManager.post(ThreadManager.THREADxUI,new Runnable()
        {
            @Override
            public void run()
            {
                if(null!=mFailView)
                {
                    mFailView.setVisibility(GONE);
                }
                if(null!=mPhotoNetworkErrorPage)
                {
                    mPhotoNetworkErrorPage.setVisibility(GONE);
                }
                // 如果图集加载页面存在则不需要再展示图片加载页面
                if(null!=mBrowserLoadingPage&&mBrowserLoadingPage.isShown())
                {
                    return;
                }
                if(null!=mLoadingView)
                {
                    mLoadingView.setVisibility(VISIBLE);
                }
            }
        });
    }
    
    /**************************************** 私有方法 ****************************************/
    private void initView()
    {
        bindPhotoView();
    }
}