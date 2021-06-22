package com.townspriter.android.template;

import com.townspriter.android.foundation.Foundation;
import com.townspriter.android.foundation.utils.log.LogImpl;
import com.townspriter.android.foundation.utils.log.Logger;
import android.app.Application;

/******************************************************************************
 * @path AndroidTemplate:APP
 * @version 1.0.0.0
 * @describe 程序入口
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-06-07 14:59:43
 * CopyRight(C)2020 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class APP extends Application
{
    private static final String APPxTAG="APP";
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        initData();
    }
    
    private void initData()
    {
        Foundation.init(this);
        Logger.setLoggerImpl(new LogImpl(APPxTAG));
    }
}
