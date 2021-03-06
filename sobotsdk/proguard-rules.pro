# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\tools\Android\AndroidStudio\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class com.bumptech.glide.integration.okhttp.OkHttpGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# 客户系统--------------
-keepattributes Annotation

-keepattributes Signature

-keep public class * extends android.app.Fragment

-keep public class * extends android.app.Activity

-keep public class * extends android.app.Application

-keep public class * extends android.app.Service

-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.content.ContentProvider

-keep public class * extends android.app.backup.BackupAgentHelper

-keep public class * extends android.preference.Preference

-keep public class * extends android.support.v4.**

-keep public class com.android.vending.licensing.ILicensingService

-keep class com.android.vending.licensing.ILicensingService

-keep class android.support.v4.** { *; }


-dontwarn android.support.v4.**

-dontwarn android.webkit.WebView

-keepclasseswithmembernames class * {

    native <methods>;

}


-keepclasseswithmembernames class * {

    public <init>(android.content.Context, android.util.AttributeSet);

}


-keepclasseswithmembernames class * {

    public <init>(android.content.Context, android.util.AttributeSet, int);

}


-keepclassmembers enum * {

    public static **[] values();

    public static ** valueOf(java.lang.String);

}


-keep class * implements android.os.Parcelable {

  public static final android.os.Parcelable$Creator *;

}


-keepclasseswithmembers class * {

    public <init>(android.content.Context);

}


-keepclassmembers class * {

   public <init> (org.json.JSONObject);

}

## ----------------------------------

##      sobot

## ----------------------------------

-keep class com.sobot.** {*;}


## ----------------------------------

##      OkHttp

## ----------------------------------

-keepattributes Signature

-keepattributes *Annotation*

-keep class com.squareup.okhttp3.** { *; }

-keep interface com.squareup.okhttp3.** { *; }

-dontwarn com.squareup.okhttp3.**


## ----------------------------------

##      Okio

## ----------------------------------

-keep class sun.misc.Unsafe { *; }

-dontwarn java.nio.file.*