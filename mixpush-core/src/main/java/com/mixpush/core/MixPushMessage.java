package com.mixpush.core;

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
//    private String msgId;

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPassThrough() {
        return passThrough;
    }

    public void setPassThrough(boolean passThrough) {
        this.passThrough = passThrough;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
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

//    public void setMsgId(String msgId) {
//        this.msgId = msgId;
//    }

//    public String getMsgId() {
//        return msgId;
//    }
}
