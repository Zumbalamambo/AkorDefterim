# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\android-sdk/tools/proguard/proguard-android.txt
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

-printmapping build/outputs/mapping/release/mapping.txt

-renamesourcefileattribute MyApplication
-keepattributes InnerClasses,EnclosingMethod,SourceFile,LineNumberTable
-dontoptimize

-dontwarn com.google.**
-dontwarn com.mysql.**
-dontwarn org.apache.**
-dontwarn com.jeremyfeinstein.**
-dontwarn rx.**
-dontwarn com.squareup.okhttp.*
-dontwarn okio.**
-dontwarn com.github.**
-dontwarn com.nostra13.**
-dontwarn com.hbb20.**

-ignorewarnings

-keep class * {
    public private *;
}

#-keep public class org.jsoup.** {
#public *;
#}

#-keep public class org.apache.** {
#public *;
#}

#-keep public class com.theartofdev.** {
#public *;
#}

#-keep public class org.eclipse.** {
#public *;
#}

#-keep public class com.github.clans.** {
#public *;
#}

#-keep public class com.jpardogo.** {
#public *;
#}

#-keep public class com.squareup.** {
#public *;
#}

#-keep public class com.github.iammehedi.** {
#public *;
#}

#-keep public class com.github.ybq.** {
#public *;
#}

#-keep public class com.github.iammehedi.** {
#public *;
#}

#-keep public class com.github.bumptech.** {
#public *;
#}

#-keep public class com.nostra13.** {
#public *;
#}

#-keep public class com.hbb20.** {
#public *;
#}

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions