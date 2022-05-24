package com.townspriter.android.photobrowser.core.model.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/******************************************************************************
 * @Path PhotoBrowserCore:ToastUtil
 * @Describe 封装吐司控制显示
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class ToastUtil
{
    private static volatile ToastUtil toastUtil;
    private Toast toast;
    
    private ToastUtil()
    {}
    
    public static ToastUtil getInstance()
    {
        if(toastUtil==null)
        {
            synchronized(ToastUtil.class)
            {
                if(toastUtil==null)
                {
                    toastUtil=new ToastUtil();
                }
            }
        }
        return toastUtil;
    }
    
    public synchronized void showToast(Context context,String message)
    {
        showToastInner(context,message,0);
    }
    
    public synchronized void showToast(Context context,int messageId)
    {
        showToastInner(context,"",messageId);
    }
    
    private void showToastInner(final Context context,String message,int messageId)
    {
        if(context==null)
        {
            throw new IllegalArgumentException("getInstance:NULL");
        }
        if(((Activity)context).isFinishing())
        {
            // return;
        }
        final String realMessage=TextUtils.isEmpty(message)?context.getString(messageId):message;
        ((Activity)context).runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if(toast==null)
                {
                    toast=Toast.makeText(context.getApplicationContext(),realMessage,Toast.LENGTH_SHORT);
                }
                else
                {
                    toast.setText(realMessage);
                }
                toast.show();
            }
        });
    }
}
