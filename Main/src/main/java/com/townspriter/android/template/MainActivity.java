package com.townspriter.android.template;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/******************************************************************************
 * @path AndroidTemplate:MainActivity
 * @version 1.0.0.0
 * @describe 主类
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-06-07 14:59:43
 * CopyRight(C)2020 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class MainActivity extends AppCompatActivity
{
    private static final String TAG="MainActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutxmain);
        initView();
        initData();
    }
    
    private void initData()
    {}
    
    private void initView()
    {}
}
