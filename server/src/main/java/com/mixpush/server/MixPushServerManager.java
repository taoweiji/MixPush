package com.mixpush.server;

import java.util.List;

public interface MixPushServerManager {

    void sendMessageToAlias(List<String> alias, String messagePayload) throws Exception;

    void sendMessageToTags(List<String> tags, String messagePayload) throws Exception;

    void sendMessageToAll(String messagePayload) throws Exception;

    void sendNotifyToAlias(List<String> alias, String title, String description, String messagePayload) throws Exception;

    void sendNotifyToTags(List<String> tags, String title, String description, String messagePayload) throws Exception;

    void sendNotifyToAll(String title, String description, String messagePayload) throws Exception;


    /**
     * 该通知栏消息是推送一个超链接，打开外部浏览器，不建议使用，建议使用普通通知栏消息实现，打开内容的浏览器，从而提高日活
     */
    @Deprecated
    void sendLinkNotifyToAlias(List<String> alias, String title, String description, String url) throws Exception;
    /**
     * 该通知栏消息是推送一个超链接，打开外部浏览器，不建议使用，建议使用普通通知栏消息实现，打开内容的浏览器，从而提高日活
     */
    @Deprecated
    void sendLinkNotifyToTags(List<String> tags, String title, String description, String url) throws Exception;
    /**
     * 该通知栏消息是推送一个超链接，打开外部浏览器，不建议使用，建议使用普通通知栏消息实现，打开内容的浏览器，从而提高日活
     */
    @Deprecated
    void sendLinkNotifyToAll(String title, String description, String url) throws Exception;

}
