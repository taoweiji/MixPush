package com.mixpush.server;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    static List<String> alias = new ArrayList<>();
    static List<String> tags = new ArrayList<>();

    static {
        MixPushServer.addPushServerManager(new GeTuiPushServerManager(GETUI_APP_ID, GETUI_APP_KEY, GETUI_MASTER_SECRET, GETUI_URL));
        MixPushServer.addPushServerManager(new MeizuPushServerManager(MEIZU_APP_ID, MEIZU_APP_SECRET_KEY));
        MixPushServer.addPushServerManager(new MiPushServerManager(APP_PACKAGE_NAME, MIPUSH_APP_SECRET_KEY));



        alias.add("103");
        tags.add("广东");
    }

    String title = "title";
    String description = "description";
    String json = "{\"name\":\"Wiki\",\"age\":24}";
    private String url = "https://www.thejoyrun.com";


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
        MixPushServer.sendMessageToAlias(alias, json);
    }

    @Test
    public void sendMessageToTags() throws Exception {
        MixPushServer.sendMessageToTags(tags, json);
    }

    @Test
    public void sendNotifyToAlias() throws Exception {
        MixPushServer.sendNotifyToAlias(alias, title, description, json);
    }

    @Test
    public void sendNotifyToTags() throws Exception {
        MixPushServer.sendNotifyToTags(tags, title, description, json);
    }


//    @Test
//    public void sendNotifyOpenWebViewToAlias() throws Exception {
//        MixPushServer.sendNotifyToAlias(alias, title, description, "{\"option\":\"web\",\"url\":\"http://baidu.com\"}");
//    }


    @Test
    public void sendNotifyLinkToAlias() throws Exception {
        MixPushServer.sendLinkNotifyToAlias(alias, title, description, url);
    }

    @Test
    public void sendNotifyLinkToTags() throws Exception {
        MixPushServer.sendLinkNotifyToTags(tags, title, description, url);
    }

    @Test
    public void sendNotifyLinkToAll() throws Exception {
        MixPushServer.sendLinkNotifyToAll(title, description, url);
    }
}