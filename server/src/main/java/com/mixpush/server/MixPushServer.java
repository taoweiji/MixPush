package com.mixpush.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wiki on 2017/6/1.
 */

public class MixPushServer {
    private static List<MixPushServerManager> managers = new ArrayList<>();

    static {
        managers.add(new MeizuPushServerManager(110697L,"ef7778880d264ec28a47399509974659"));
        managers.add(new GeTuiPushServerManager());
        managers.add(new MiPushServerManager("com.mixpush.demo","0Evhdw93wlSfAiZ3JEkCMA=="));

    }


    public static void sendMessageToAlias(String alias, String messagePayload) throws Exception {
        List<String> aliases = new ArrayList<>();
        aliases.add(alias);
        sendMessageToAlias(aliases, messagePayload);
    }

    public static void sendMessageToTags(String tag, String messagePayload) throws Exception {
        List<String> tags = new ArrayList<>();
        tags.add(tag);
        sendMessageToTags(tags, messagePayload);
    }

    public static void sendNotifyToAlias(String alias, String title, String description, String messagePayload) throws Exception {
        List<String> aliases = new ArrayList<>();
        aliases.add(alias);
        sendNotifyToAlias(aliases, title, description, messagePayload);
    }

    public static void sendNotifyToTags(String tag, String title, String description, String messagePayload) throws Exception {
        List<String> tags = new ArrayList<>();
        tags.add(tag);
        sendNotifyToTags(tags, title, description, messagePayload);
    }

    public static void sendMessageToAlias(List<String> alias, String messagePayload) throws Exception {
        for (MixPushServerManager item : managers) {
            item.sendMessageToAlias(alias, messagePayload);
        }
    }


    public static void sendMessageToTags(List<String> tags, String messagePayload) throws Exception {
        for (MixPushServerManager item : managers) {
            item.sendMessageToTags(tags, messagePayload);
        }
    }


    public static void sendNotifyToAlias(List<String> alias, String title, String description, String messagePayload) throws Exception {
        for (MixPushServerManager item : managers) {
            item.sendNotifyToAlias(alias, title, description, messagePayload);
        }
    }


    public static void sendNotifyToTags(List<String> tags, String title, String description, String messagePayload) throws Exception {
        for (MixPushServerManager item : managers) {
            item.sendNotifyToTags(tags, title, description, messagePayload);
        }
    }

    public static void sendNotifyToAll(String title, String description, String messagePayload) throws Exception {
        for (MixPushServerManager item : managers) {
            item.sendNotifyToAll(title, description, messagePayload);
        }
    }


    public static void sendMessageToAll(String messagePayload) throws Exception {
        for (MixPushServerManager item : managers) {
            item.sendMessageToAll(messagePayload);
        }
    }

}
