# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\android\sdk/tools/proguard/proguard-android.txt
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

-keep class android.support.v7.widget.** { *; }
-dontwarn com.rey.material.**
-keep class android.support.v7.widget.SearchView { *; }
-keepattributes Signature
-keep public class com.google.gson.stream.** { *; }
-keep class com.zohaltech.app.corevocabulary.serializables.** { *; }
