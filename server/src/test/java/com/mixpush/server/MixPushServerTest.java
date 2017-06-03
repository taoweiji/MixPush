package com.mixpush.server;

import org.junit.Test;

/**
 * Created by Wiki on 2017/6/1.
 */
public class MixPushServerTest {
    static {
        MixPushServer.addPushServerManager(new MeizuPushServerManager(110697L,"ef7778880d264ec28a47399509974659"));
        MixPushServer.addPushServerManager(new GeTuiPushServerManager("51xb25cmJx9I28wet1Rtd5","Wq0MtiYBdO7YwpTLbR8iI3","W0EHO18Yk77sSLJxCvBlf4","http://sdk.open.api.igexin.com/apiex.htm"));
        MixPushServer.addPushServerManager(new MiPushServerManager("com.mixpush.demo","0Evhdw93wlSfAiZ3JEkCMA=="));
    }

    @Test
    public void sendNotifyToAll() throws Exception {
        MixPushServer.sendNotifyToAll("title", "description", "{\"name\":\"Wiki\",\"age\":24}");
    }

    @Test
    public void sendMessageToAll() throws Exception {
        MixPushServer.sendMessageToAll("{\"name\":\"Wiki\",\"age\":24}");
    }

    @Test
    public void sendMessageToAlias() throws Exception {
        MixPushServer.sendMessageToAlias("100", "{\"name\":\"Wiki\",\"age\":24}");
    }

    @Test
    public void sendMessageToTags() throws Exception {
        MixPushServer.sendMessageToTags("广东", "{\"name\":\"Wiki\",\"age\":24}");
    }

    @Test
    public void sendNotifyToAlias() throws Exception {
        MixPushServer.sendNotifyToAlias("100", "title", "description", "{\"name\":\"Wiki\",\"age\":24}");
    }

    @Test
    public void sendNotifyToTags() throws Exception {
        MixPushServer.sendNotifyToTags("广东", "title", "description", "{\"name\":\"Wiki\",\"age\":24}");
    }

}