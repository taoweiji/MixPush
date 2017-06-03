package com.mixpush.server;

import org.junit.Test;

/**
 * Created by Wiki on 2017/6/1.
 */
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


    @Test
    public void sendNotifyOpenWebViewToAlias() throws Exception {
        MixPushServer.sendNotifyToAlias("100", title, description, "{\"option\":\"web\",\"url\":\"http://baidu.com\"}");
    }

}