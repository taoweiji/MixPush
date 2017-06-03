# MixPush 融合推送

融合推送平台，根据不同的手机使用不同的推送平台（小米推送、魅族推送、个推等），共享系统级推送，提高推送到达率。

[![](https://jitpack.io/v/joyrun/MixPush.svg)](https://jitpack.io/#joyrun/MixPush)



开始时间：2017.06.01 12:00:00

Step 1. Add the JitPack repository to your build file
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Step 2. Add the dependency
```
	dependencies {
	  compile 'com.github.joyrun.MixPush:client-core:0.1'
    compile 'com.github.joyrun.MixPush:client-mipush:0.1'
    compile 'com.github.joyrun.MixPush:client-getui:0.1'
    compile 'com.github.joyrun.MixPush:client-meizu:0.1'
	}
  
```
