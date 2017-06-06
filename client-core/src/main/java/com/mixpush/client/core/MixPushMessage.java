package com.mixpush.client.core;

import java.io.Serializable;

/**
 * Created by Wiki on 2017/6/1.
 */

public class MixPushMessage implements Serializable{
    private String messageId;
    private String content;
    private String alias;
    private String topic;
    private String userAccount;
    private int passThrough;
    private int notifyType;
    private int notifyId;
    private boolean isNotified;
    private String description;
    private String title;
    private String platform;//
    private int notify;

    public MixPushMessage() {
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setNotify(int notify) {
        this.notify = notify;
    }

    public int getNotify() {
        return notify;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setPassThrough(int passThrough) {
        this.passThrough = passThrough;
    }

    public int getPassThrough() {
        return passThrough;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
