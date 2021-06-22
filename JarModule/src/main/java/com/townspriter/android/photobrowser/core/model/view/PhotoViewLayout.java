package com.townspriter.android.photobrowser.core.model.view;

import com.bumptech.glide.Glide;
import com.townspriter.android.foundation.utils.concurrent.ThreadManager;
import com.townspriter.android.foundation.utils.log.Logger;
import com.townspriter.android.foundation.utils.net.NetworkUtil;
import com.townspriter.android.foundation.utils.ui.ResHelper;
import com.townspriter.android.photobrowser.core.R;
import com.townspriter.android.photobrowser.core.api.view.IPhotoBrowserOverlay;
import com.townspriter.android.photobrowser.core.model.listener.OnPhotoLoadListener;
import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/******************************************************************************
 * @Path PhotoBrowserCore:PhotoViewLayout
 * @Describe 容器类
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class PhotoViewLayout extends RelativeLayout implements OnPhotoLoadListener
{
    private final String TAG="PhotoViewLayout";
    private final IPhotoBrowserOverlay mBrowserOverlay;
    private PhotoViewCompat mPhotoView;
    private ViewGroup mFailView;
    private ViewGroup mPhotoNetworkErrorPage;
    private ViewGroup mLoadingView;
    private ViewGroup mBrowserLoadingPage;
    
    public PhotoViewLayout(Context context,IPhotoBrowserOverlay browserOverlay)
    {
        super(context);
        mBrowserOverlay=browserOverlay;
        initView();
    }
    
    public void bindPhotoView()
    {
        removeAllViews();
        setBackgroundColor(Color.TRANSPARENT);
        LayoutParams layoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
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
            else
            {
                Logger.w(TAG,"bindPhotoView-mFailView:NULL");
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
            else
            {
                Logger.w(TAG,"bindPhotoView-mPhotoNetworkErrorPage:NULL");
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
            else
            {
                Logger.w(TAG,"bindPhotoView-mLoadingView:NULL");
            }
            // 图集加载页面
            mBrowserLoadingPage=mBrowserOverlay.createBrowserLoadingPage();
        }
        else
        {
            Logger.w(TAG,"bindPhotoView-mBrowserOverlay:NULL");
        }
    }
    
    public PhotoViewCompat getPhotoView()
    {
        return mPhotoView;
    }
    
    public void clearPhotoView()
    {
        Logger.i(TAG,"clearPhotoView");
        if(mPhotoView!=null)
        {
            removeView(mPhotoView);
            // 停止加载此控件的图片
            Glide.with(getContext()).clear(mPhotoView);
        }
    }
    
    /**************************************** OnPhotoLoadListener ****************************************/
    @Override
    public void onPhotoLoadFailed()
    {
        Logger.i(TAG,"onPhotoLoadFailed");
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
        Logger.i(TAG,"onPhotoLoadSucceed");
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
        Logger.i(TAG,"onPhotoLoading");
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