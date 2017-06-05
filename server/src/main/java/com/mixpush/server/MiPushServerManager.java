package com.mixpush.server;

import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

/**
 * Created by Wiki on 2017/6/1.
 */

public class MiPushServerManager implements MixPushServerManager {
    private final String appPackageName;
    private final String appSecretKey;

    public MiPushServerManager(String appPackageName, String appSecretKey) {
        this.appPackageName = appPackageName;
        this.appSecretKey = appSecretKey;
    }
    private Message getLinkMessage(String title, String description, String url){
        return new Message.Builder()
                .title(title)
                .description(description)
                .restrictedPackageName(appPackageName)
                .passThrough(0)  //消息使用通知栏方式
                .notifyType(1)
                .extra(Constants.EXTRA_PARAM_NOTIFY_EFFECT, Constants.NOTIFY_WEB)
                .extra(Constants.EXTRA_PARAM_WEB_URI, url)
                .build();
    }

    private Message getNotifyMessage(String title, String description, String messagePayload){
        return new Message.Builder()
                .title(title)
                .description(description).payload(messagePayload)
                .restrictedPackageName(appPackageName)
                .passThrough(0)
                .notifyType(1)
                .build();
    }


    private Message getMessage(String messagePayload){
        return new Message.Builder()
                .passThrough(1)
                .payload(messagePayload)
                .restrictedPackageName(appPackageName)
                .notifyType(0)
                .build();
    }



    @Override
    public void sendMessageToAlias(List<String> alias, String messagePayload) throws Exception {
        Constants.useOfficial();
        Sender sender = new Sender(appSecretKey);
        Message message = getMessage(messagePayload);
        for (String item : alias) {
            Result result = sender.sendToAlias(message, item, 10);
            System.out.println(item + ":" + result.toString());
        }
    }

    @Override
    public void sendMessageToTags(List<String> tags, String messagePayload) throws IOException, ParseException {
        Constants.useOfficial();
        Sender sender = new Sender(appSecretKey);
        Message message = getMessage(messagePayload);
        for (String item : tags) {
            Result result = sender.broadcast(message, item, 10);
            System.out.println(item + ":" + result.toString());
        }
    }

    @Override
    public void sendMessageToAll(String messagePayload) throws IOException, ParseException {
        Constants.useOfficial();
        Sender sender = new Sender(appSecretKey);
        Message message = getMessage(messagePayload);
        Result result = sender.broadcastAll(message, 10);
        System.out.println("all:" + result.toString());
    }


    @Override
    public void sendNotifyToAlias(List<String> alias, String title, String description, String messagePayload) throws IOException, ParseException {
        Constants.useOfficial();
        Sender sender = new Sender(appSecretKey);
        Message message = getNotifyMessage(title, description, messagePayload);

        for (String item : alias) {
            Result result = sender.sendToAlias(message, item, 10);
            System.out.println(item + ":" + result.toString());
        }
    }

    @Override
    public void sendNotifyToTags(List<String> tags, String title, String description, String messagePayload) throws IOException, ParseException {
        Constants.useOfficial();
        Sender sender = new Sender(appSecretKey);
        Message message = getNotifyMessage(title, description, messagePayload);

        for (String item : tags) {
            Result result = sender.broadcast(message, item, 10);
            System.out.println(item + ":" + result.toString());
        }
    }

    @Override
    public void sendNotifyToAll(String title, String description, String messagePayload) throws IOException, ParseException {
        Constants.useOfficial();
        Sender sender = new Sender(appSecretKey);
        Message message = getNotifyMessage(title, description, messagePayload);

        Result result = sender.broadcastAll(message, 10);
        System.out.println("all:" + result.toString());
    }

    @Override
    public void sendLinkNotifyToAlias(List<String> alias, String title, String description, String url) throws Exception {
        Constants.useOfficial();
        Sender sender = new Sender(appSecretKey);
        Message message = getLinkMessage(title, description, url);
        for (String item : alias) {
            Result result = sender.sendToAlias(message, item, 10);
            System.out.println(item + ":" + result.toString());
        }
    }

    @Override
    public void sendLinkNotifyToTags(List<String> tags, String title, String description, String url) throws Exception {
        Constants.useOfficial();
        Sender sender = new Sender(appSecretKey);
        Message message = getLinkMessage(title, description, url);

        for (String item : tags) {
            Result result = sender.broadcast(message, item, 10);
            System.out.println(item + ":" + result.toString());
        }
    }

    @Override
    public void sendLinkNotifyToAll(String title, String description, String url) throws Exception {
        Constants.useOfficial();
        Sender sender = new Sender(appSecretKey);
        Message message = getLinkMessage(title, description, url);

        Result result = sender.broadcastAll(message, 10);
        System.out.println("all:" + result.toString());
    }
}
