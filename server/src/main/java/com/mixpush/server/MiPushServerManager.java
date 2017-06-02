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

    @Override
    public void sendMessageToAlias(List<String> alias, String messagePayload) throws Exception {
        Constants.useOfficial();
        Sender sender = new Sender(appSecretKey);
        Message message = new Message.Builder()
                .passThrough(1)
                .payload(messagePayload)
                .restrictedPackageName(appPackageName)
                .notifyType(0)
                .build();
        for (String item : alias) {
            Result result = sender.sendToAlias(message, item, 10);
            System.out.println(item + ":" + result.toString());
        }
    }

    @Override
    public void sendMessageToTags(List<String> tags, String messagePayload) throws IOException, ParseException {
        Constants.useOfficial();
        Sender sender = new Sender(appSecretKey);
        Message message = new Message.Builder()
                .passThrough(1)
                .payload(messagePayload)
                .restrictedPackageName(appPackageName)
                .notifyType(0)
                .build();
        for (String item : tags) {
            Result result = sender.broadcast(message, item, 10);
            System.out.println(item + ":" + result.toString());
        }
    }

    @Override
    public void sendMessageToAll(String messagePayload) throws IOException, ParseException {
        Constants.useOfficial();
        Sender sender = new Sender(appSecretKey);
        Message message = new Message.Builder()
                .passThrough(1)
                .payload(messagePayload)
                .restrictedPackageName(appPackageName)
                .notifyType(0)
                .build();
        Result result = sender.broadcastAll(message, 10);
        System.out.println("all:" + result.toString());
    }


    @Override
    public void sendNotifyToAlias(List<String> alias, String title, String description, String messagePayload) throws IOException, ParseException {
        Constants.useOfficial();
        Sender sender = new Sender(appSecretKey);
        Message message = new Message.Builder()
                .title(title)
                .description(description).payload(messagePayload)
                .restrictedPackageName(appPackageName)
                .passThrough(0)
                .notifyType(1)
                .build();

        for (String item : alias) {
            Result result = sender.sendToAlias(message, item, 10);
            System.out.println(item + ":" + result.toString());
        }
    }

    @Override
    public void sendNotifyToTags(List<String> tags, String title, String description, String messagePayload) throws IOException, ParseException {
        Constants.useOfficial();
        Sender sender = new Sender(appSecretKey);
        Message message = new Message.Builder()
                .title(title)
                .description(description).payload(messagePayload)
                .restrictedPackageName(appPackageName)
                .passThrough(0)
                .notifyType(1)
                .build();

        for (String item : tags) {
            Result result = sender.broadcast(message, item, 10);
            System.out.println(item + ":" + result.toString());
        }
    }

    @Override
    public void sendNotifyToAll(String title, String description, String messagePayload) throws IOException, ParseException {
        Constants.useOfficial();
        Sender sender = new Sender(appSecretKey);
        Message message = new Message.Builder()
                .title(title)
                .description(description).payload(messagePayload)
                .restrictedPackageName(appPackageName)
                .passThrough(0)
                .notifyType(1)
                .build();

        Result result = sender.broadcastAll(message, 10);
        System.out.println("all:" + result.toString());
    }
}
