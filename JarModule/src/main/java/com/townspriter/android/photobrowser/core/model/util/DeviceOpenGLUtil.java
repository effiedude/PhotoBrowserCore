package com.townspriter.android.photobrowser.core.model.util;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import android.opengl.GLES10;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

/******************************************************************************
 * @Path PhotoBrowserCore:DeviceOpenGLUtil
 * @Describe OpengGLES1.0工具.获取不同版本安卓系统底层支持的最大纹理尺寸
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class DeviceOpenGLUtil
{
    private static final int EGLxCONTEXTxCLIENTxVERSION=0x3098;
    
    public static int getGLESLimitTexture()
    {
        if(VERSION.SDK_INT>=VERSION_CODES.LOLLIPOP)
        {
            return getGLESTextureLimitEqualAboveLollipop();
        }
        else
        {
            return getGLESTextureLimitBelowLollipop();
        }
    }
    
    private static int getGLESTextureLimitBelowLollipop()
    {
        int[] maxSize=new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE,maxSize,0);
        return maxSize[0];
    }
    
    private static int getGLESTextureLimitEqualAboveLollipop()
    {
        EGL10 egl=(EGL10)EGLContext.getEGL();
        EGLDisplay dpy=egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int[] vers=new int[2];
        egl.eglInitialize(dpy,vers);
        int[] configAttr={EGL10.EGL_COLOR_BUFFER_TYPE,EGL10.EGL_RGB_BUFFER,EGL10.EGL_LEVEL,0,EGL10.EGL_SURFACE_TYPE,EGL10.EGL_PBUFFER_BIT,EGL10.EGL_NONE};
        EGLConfig[] configs=new EGLConfig[1];
        int[] numConfig=new int[1];
        egl.eglChooseConfig(dpy,configAttr,configs,1,numConfig);
        EGLConfig config=configs[0];
        int[] surfAttr={EGL10.EGL_WIDTH,64,EGL10.EGL_HEIGHT,64,EGL10.EGL_NONE};
        EGLSurface surf=egl.eglCreatePbufferSurface(dpy,config,surfAttr);
        int[] ctxAttrib={EGLxCONTEXTxCLIENTxVERSION,1,EGL10.EGL_NONE};
        EGLContext ctx=egl.eglCreateContext(dpy,config,EGL10.EGL_NO_CONTEXT,ctxAttrib);
        egl.eglMakeCurrent(dpy,surf,surf,ctx);
        int[] maxSize=new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE,maxSize,0);
        egl.eglMakeCurrent(dpy,EGL10.EGL_NO_SURFACE,EGL10.EGL_NO_SURFACE,EGL10.EGL_NO_CONTEXT);
        egl.eglDestroySurface(dpy,surf);
        egl.eglDestroyContext(dpy,ctx);
        egl.eglTerminate(dpy);
        return maxSize[0];
    }
}
