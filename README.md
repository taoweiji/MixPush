
[![](https://jitpack.io/v/joyrun/MixPush.svg)](https://jitpack.io/#joyrun/MixPush)

MixPush SDK融合多家推送平台，小米推送、魅族推送、个推等，在MIUI和Flyme OS共享系统级推送，杀死APP也能收到推送消息。

由于国产手机厂商的“优化”，APP基本都无法在后台正常运行，所以大多数的推送服务在一键杀死APP后都无法正常收到推送。小米和魅族也由此推出了系统级别的推送服务，让APP在被杀死的情况下正常收到推送消息。

为了降低开发者融合多家推送的开发成本，所以就开源了MixPush SDK，让开发者更加简单地集成多家推送平台，提高推送的到达率。

##### 原理图
 ![image](logic_chart.jpg)



#### Android客户端配置
添加仓库地址，修改根目录的build.gradle文件：
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
选择推送平台，如果没有可以参考源码自行实现，添加依赖：
```

dependencies {
    compile 'com.github.joyrun.MixPush:client-core:0.1' //必填
    compile 'com.github.joyrun.MixPush:client-mipush:0.1' // 小米推送
    compile 'com.github.joyrun.MixPush:client-getui:0.1' // 个推
    compile 'com.github.joyrun.MixPush:client-meizu:0.1' // 魅族推送
}
```
创建一个继承MixPushIntentService的服务类，用于接收事件：

```

public class PushIntentService extends MixPushIntentService {
    @Override
    public void onReceivePassThroughMessage(MixPushMessage message) {
        Log.e(TAG, "收到透传消息 -> " + message.getContent());
    }

    @Override
    public void onNotificationMessageClicked(MixPushMessage message) {
        Log.e(TAG, "通知栏消息点击 -> " + message.getContent());
    }
}
```
配置AndroidManifest.xml，注册服务类
```
<manifest>
    <application ...>
        ...
        <service android:name=".PushIntentService"/>
    </application>
</manifest>
```



在Application类进行初始化，如果没有请自行创建：
```
public class DemoApplication extends Application {
    public static final String MEIZU_APP_ID = "<MEIZU_APP_ID>";
    public static final String MEIZU_APP_KEY = "<MEIZU_APP_KEY>";
    public static final String MIPUSH_APP_ID = "<MIPUSH_APP_ID>";
    public static final String MIPUSH_APP_KEY = "<MIPUSH_APP_KEY>";
    
    @Override
    public void onCreate() {
        super.onCreate();
        initPush();
    }
    private void initPush() {
        MixPushClient.addPushManager(new MeizuPushManager(MEIZU_APP_KEY, MEIZU_APP_ID));
        MixPushClient.addPushManager(new MiPushManager(MIPUSH_APP_ID, MIPUSH_APP_KEY));
        MixPushClient.addPushManager(new GeTuiManager());
        MixPushClient.setPushIntentService(PushIntentService.class);
        MixPushClient.setSelector(new MixPushClient.Selector() {
            @Override
            public String select(Map<String, MixPushManager> pushAdapterMap, String brand) {
                // return GeTuiManager.NAME;
                //底层已经做了小米推送、魅族推送、个推判断，也可以按照自己的需求来选择推送
                return super.select(pushAdapterMap, brand);
            }
        });
        // 配置接收推送消息的服务类
        MixPushClient.setPushIntentService(PushIntentService.class);
        MixPushClient.registerPush(this);
        // 绑定别名，一般是填写用户的ID，便于定向推送
        String userId = "100";
        MixPushClient.bindAlias(this, userId);
    }
}
```
修改APP的build.gradle文件，配置个推的APP ID等信息

```
android {
    defaultConfig {
        ...
        manifestPlaceholders = [
                GETUI_APP_ID : "<GETUI_APP_ID>",
                GETUI_APP_KEY : "<GETUI_APP_KEY>",
                GETUI_APP_SECRET : "<GETUI_APP_SECRET>",
                PACKAGE_NAME: "<APP的包名>"
        ]
    }
}
```
Android客户端的配置就只有这么多。
### 服务端配置测试
目前只有Java的服务端代码，如果服务端使用其它语言，请参考设计思路自己开发。

服务端的代码可以通过Maven方式引入，但还是建议复制代码到项目中来实现。这样子能够有更大的可控性，比如消息的有效期、消息提醒的时间、重试的次数等，我的代码已经编写了较优的配置，如果没有其它要求可以通过Maven引入。

增加Maven仓库
```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
添加依赖
```
	<dependency>
	    <groupId>com.github.joyrun.MixPush</groupId>
	    <artifactId>server</artifactId>
	    <version>0.1</version>
	</dependency>
```

or

复制代码和jar包到项目。

##### 服务端推送测试

```
public class MixPushServerTest {
    public static final String APP_PACKAGE_NAME = "com.mixpush.demo";
    public static final String MIPUSH_APP_SECRET_KEY = "0Evhdw93wlSfAiZ3JEkCMA==";

    public static final Long MEIZU_APP_ID = 110697L;
    public static final String MEIZU_APP_SECRET_KEY = "ef7778880d264ec28a47399509974659";

    public static final String GETUI_APP_ID = "51xb25cmJx9I28wet1Rtd5";
    public static final String GETUI_APP_KEY = "Wq0MtiYBdO7YwpTLbR8iI3";
    public static final String GETUI_MASTER_SECRET = "W0EHO18Yk77sSLJxCvBlf4";
    public static final String GETUI_URL = "http://sdk.open.api.igexin.com/apiex.htm";
    static {
        MixPushServer.addPushServerManager(new MiPushServerManager(APP_PACKAGE_NAME, MIPUSH_APP_SECRET_KEY));
        MixPushServer.addPushServerManager(new MeizuPushServerManager(MEIZU_APP_ID, MEIZU_APP_SECRET_KEY));
        MixPushServer.addPushServerManager(new GeTuiPushServerManager(GETUI_APP_ID, GETUI_APP_KEY, GETUI_MASTER_SECRET, GETUI_URL));
    }
    String title = "title";
    String description = "description";
    String json = "{\"name\":\"Wiki\",\"age\":24}";
    @Test
    public void sendNotifyToAll() throws Exception {
        MixPushServer.sendNotifyToAll(title, description, json);
    }
    @Test
    public void sendMessageToAll() throws Exception {
        MixPushServer.sendMessageToAll(json);
    }
    @Test
    public void sendMessageToAlias() throws Exception {
        MixPushServer.sendMessageToAlias("100", json);
    }
    @Test
    public void sendMessageToTags() throws Exception {
        MixPushServer.sendMessageToTags("广东", json);
    }
    @Test
    public void sendNotifyToAlias() throws Exception {
        MixPushServer.sendNotifyToAlias("100", title, description, json);
    }
    @Test
    public void sendNotifyToTags() throws Exception {
        MixPushServer.sendNotifyToTags("广东", title, description, json);
    }
}
```
如果你不是使用以上三个推送，也可以根据接口自己来实现。



