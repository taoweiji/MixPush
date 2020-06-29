package com.mixpush.sender;

import java.util.UUID;

public class MixPushMessage {
    /**
     * 通知栏标题,透传该字段为空
     */
    private String title;
    /**
     * 通知栏副标题,透传该字段为空
     */
    private String description;
    /**
     * 推送所属平台,比如mi/huawei
     */
    private String platform;
    /**
     * 推送附属的内容信息
     */
    private String payload;
    /**
     * 是否是透传推送
     */
    private boolean passThrough;
    /**
     * 配置信息,渠道,声音,震动等
     */
    private MixPushMessageConfig config = new MixPushMessageConfig();
    private String messageId;
    private boolean justOpenApp;
    private long timeToSend;
//    private String taskId;
    private String oppoTaskId;
    private String vivoTaskId;

    public String getTitle() {
        return title;
    }

    public String getVivoTaskId() {
        return vivoTaskId;
    }

    public void setVivoTaskId(String vivoTaskId) {
        this.vivoTaskId = vivoTaskId;
    }

    public boolean isJustOpenApp() {
        return justOpenApp;
    }

    public String getDescription() {
        return description;
    }

    public String getPlatform() {
        return platform;
    }

    public String getPayload() {
        return payload;
    }

    public boolean isPassThrough() {
        return passThrough;
    }

    public MixPushMessageConfig getConfig() {
        return config;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getOppoTaskId() {
        return oppoTaskId;
    }

    public void setOppoTaskId(String oppoTaskId) {
        this.oppoTaskId = oppoTaskId;
    }

    @Override
    public String toString() {
        return "UnifiedPushMessage{" +
                "title='" + title + '\'' +
                ", content='" + description + '\'' +
                ", platform='" + platform + '\'' +
                ", payload='" + payload + '\'' +
                ", passThrough=" + passThrough +
                '}';
    }

    public long getTimeToSend() {
        return timeToSend;
    }

//    public void setTaskId(String taskId) {
//        this.taskId = taskId;
//    }
//
//    public String getTaskId() {
//        return taskId;
//    }


    public static class Builder {
        private MixPushMessage message = new MixPushMessage();

        public Builder() {
        }

//        protected UnifiedPushMessage.Builder collapseKey(String value) {
//            this.collapseKey = value;
//            return this;
//        }

        public Builder payload(String value) {
            message.payload = value;
            return this;
        }

        public Builder title(String value) {
            message.title = value;
            return this;
        }

        public Builder description(String value) {
            message.description = value;
            return this;
        }

        public Builder passThrough(boolean passThrough) {
            message.passThrough = passThrough;
            return this;
        }

        public Builder justOpenApp(boolean justOpenApp) {
            message.justOpenApp = justOpenApp;
            return this;
        }


        public MixPushMessage build() {
            if (message.messageId == null) {
                message.messageId = UUID.randomUUID().toString();
            }
            return message;
        }

        public Builder config(MixPushMessageConfig config) {
            message.config = config;
            return this;
        }

        public Builder messageId(String messageId) {
            message.messageId = messageId;
            return this;
        }

        public Builder timeToSend(long milliseconds) {
            message.timeToSend = milliseconds;
            return this;
        }

    }
}
