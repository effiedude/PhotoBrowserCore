package com.townspriter.android.photobrowser.core.api.view;

import android.view.ViewGroup;

/******************************************************************************
 * @Path PhotoBrowserCore:IPhotoBrowserOverlay
 * @Describe 看图器浮层界面定制
 * @Describe 提供界面定制:顶部布局/加载中布局/加载失败布局/底部布局/内容提示界面定制
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface IPhotoBrowserOverlay
{
    /**
     * createBrowserLoadingPage
     * 创建图集加载中页面
     *
     * @return ViewGroup
     */
    ViewGroup createBrowserLoadingPage();
    
    /**
     * createNetworkErrorPage
     * 构造图集网络错误页面
     *
     * @return
     */
    ViewGroup createBrowserNetworkErrorPage();
    
    /**
     * createBrowserLoadFailPage
     * 构造图集加载失败页面(图集数据加载失败会显示此布局)
     *
     * @return
     */
    ViewGroup createBrowserLoadFailPage();
    
    /**
     * createBrowserOfflinePage
     * 创建图集下线页面
     *
     * @return ViewGroup
     */
    ViewGroup createBrowserOfflinePage();
    
    /**
     * createPhotoLoadingLayout
     * 构造图片加载中页面
     *
     * @return
     */
    ViewGroup createPhotoLoadingLayout();
    
    /**
     * createPhotoNetworkErrorPage
     * 构造图片加载网络错误页面
     *
     * @return
     */
    ViewGroup createPhotoNetworkErrorPage();
    
    /**
     * createPhotoLoadFailLayout
     * 构造图片加载失败页面(图片展示失败会显示此布局)
     *
     * @return
     */
    ViewGroup createPhotoLoadFailLayout();
    
    /**
     * createTopLayout
     * 构造顶部工具栏
     *
     * @return
     */
    ViewGroup createTopLayout();
    
    /**
     * createBottomLayout
     * 构造底部工具栏(索引/下载)
     *
     * @return
     */
    ViewGroup createBottomLayout();
    
    /**
     * createToolbarLayout
     * 构造底部工具栏(评论/收藏/分享)
     *
     * @return
     */
    ViewGroup createToolbarLayout();
}
