# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 去除行号和源文件信息
# -dontusemixedcaseclassnames
# -dontskipnonpubliclibraryclasses
# 丢弃调试信息
# -dontoptimize
# -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
# 彻底去除行号和源文件调试信息
# -renamesourcefileattribute ''
# -keepattributes !SourceFile,!LineNumberTable

# 保留 native 方法，防止 JNI 失败
# -keepclasseswithmembernames class * {
#     native <methods>;
# }
