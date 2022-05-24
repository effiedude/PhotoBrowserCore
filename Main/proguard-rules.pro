#************************************基础参数************************************#
-dontwarn **
-dontnote **

#屏蔽警告
-ignorewarnings

#混淆所有的方法.无论任何关键字
-dontskipnonpubliclibraryclasses

#安卓不需要预校验.去掉预校验能够加快混淆速度.预校验是对嘎哇平台做的优化
-dontpreverify

#产生实际类和混淆类的映射文件
-verbose

#指定混淆是采用的算法.后面的参数是一个过滤器(这个过滤器是谷歌推荐的算法一般不做更改)
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#抛出异常时.保留异常类名称和异常行数
-keepattributes SourceFile,LineNumberTable

#抛出异常时混淆异常日志.将异常类名称显示为指定的类名称
#-renamesourcefileattribute SourceFile

#保留注解参数
-keepattributes *Annotation*


#************************************公共配置************************************#
-keep class android.app.*$*{*;}
-keep class * extends android.app.Application{*;}
-keep class * extends android.app.Activity
-keep class * extends android.app.Service
-keep class * extends android.os.IInterface
-keep class * extends android.content.BroadcastReceiver
-keep class * extends android.content.ContentProvider
-keep class * extends android.app.backup.BackupAgentHelper
-keep class * extends android.preference.Preference
-keep class * extends android.appwidget.AppWidgetProvider
-keep class * extends androidx.**{*;}
-keep class * extends androidx.fragment.app.Fragment{*;}
-keep class * extends androidx.fragment.app.FragmentStatePagerAdapter{*;}
-keep class * extends androidx.fragment.app.FragmentPagerAdapter{*;}
-keep class * extends android.webkit.**{*;}
-keep public class android.webkit.*
-keep class * extends android.widget.**{*;}
-keep class * extends android.view.View{*;}
-keep class * implements android.os.Handler.Callback{*;}
-keep class * implements android.os.IBinder{*;}
-keep class * implements java.io.Serializable
-keep class * implements Handler.Callback{*;}
-keep interface * extends android.os.IInterface{*;}
-keep interface **{*;}
-keep class org.json.**{*;}


#************************************网页必配************************************#
#WebView处理(项目中没有使用到webView和JS的忽略即可)
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#    public *;
#}

#-keepclassmembers class * extends android.webkit.webViewClient {
#    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
#    public boolean *(android.webkit.WebView, java.lang.String);
#}

#-keepclassmembers class * extends android.webkit.webViewClient {
#    public void *(android.webkit.webView, jav.lang.String);
#}


#************************************可选定制************************************#
#保留资源标示
#-keep class **.R$* {*;}

#保留底层方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保留在Activity中的方法参数是View的方法.确保布局中设置的onClick不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

#保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


#对于带有回调函数的on**Event或**On*Listener的接口不被混淆
#-keepclassmembers class * {
#    void *(**On*Event);
#    void *(**On*Listener);
#}

#保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

#保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}


#************************************对外接口************************************#


#************************************三方接口************************************#