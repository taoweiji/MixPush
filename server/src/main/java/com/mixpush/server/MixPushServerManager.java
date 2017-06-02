package com.mixpush.server;

import java.util.List;

public interface MixPushServerManager {

    void sendMessageToAlias(List<String> alias, String messagePayload) throws Exception;

    void sendMessageToTags(List<String> tags, String messagePayload) throws Exception;

    void sendMessageToAll(String messagePayload) throws Exception;

    void sendNotifyToAlias(List<String> alias, String title, String description, String messagePayload) throws Exception;

    void sendNotifyToTags(List<String> tags, String title, String description, String messagePayload) throws Exception;

    void sendNotifyToAll(String title, String description, String messagePayload) throws Exception;
}
