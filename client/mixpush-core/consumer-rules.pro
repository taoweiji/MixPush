# MixPush
-keep class com.mixpush.mi.MiPushProvider {*;}
-keep class com.mixpush.meizu.MeizuPushProvider {*;}
-keep class com.mixpush.huawei.HuaweiPushProvider {*;}
-keep class com.mixpush.oppo.OppoPushProvider {*;}
-keep class com.mixpush.vivo.VivoPushProvider {*;}
 
# 华为推送
-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.huawei.hianalytics.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}

# 小米推送
-keep class com.xiaomi.**{*;}

# OPPO
-keep public class * extends android.app.Service
-keep class com.heytap.msp.** { *;}

# VIVO
-dontwarn com.vivo.push.** 
-keep class com.vivo.push.**{*; } 
-keep class com.vivo.vms.**{*; }

# 魅族
-keep class com.meizu.**{*;}

