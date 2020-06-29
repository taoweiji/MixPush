package com.mixpush.sender;

public class MixPushMessageConfig {
    private String channelId;
    private long timeToLive = 72 * 3600 * 1000;
    private boolean systemMessage;
    private String miPushChannelId;
    private String oppoPushChannelId;
    private String huaweiPushChannelId;
    // 声音,震动,渠道,icon,retries,时效,重试次数,定时,回执

    public String getChannelId() {
        return channelId;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public boolean isSystemMessage() {
        return systemMessage;
    }

    public String getMiPushChannelId() {
        return miPushChannelId;
    }

    public String getOppoPushChannelId() {
        return oppoPushChannelId;
    }

    public String getHuaweiPushChannelId() {
        return huaweiPushChannelId;
    }

    public static class Builder {
        private MixPushMessageConfig config = new MixPushMessageConfig();

        public Builder channelId(String channelId) {
            config.channelId = channelId;
            return this;
        }

        public Builder timeToLive(long timeToLive) {
            config.timeToLive = timeToLive;
            return this;
        }

        public Builder vivoSystemMessage(boolean isSystemMessage) {
            config.systemMessage = isSystemMessage;
            return this;
        }


        public MixPushMessageConfig build() {
            return config;
        }

        public Builder miPushChannelId(String miPushChannelId) {
            config.miPushChannelId = miPushChannelId;
            return this;
        }

        public Builder oppoPushChannelId(String oppoPushChannelId) {
            config.oppoPushChannelId = oppoPushChannelId;
            return this;
        }
        public Builder huaweiPushChannelId(String huaweiPushChannelId) {
            config.huaweiPushChannelId = huaweiPushChannelId;
            return this;
        }

    }
}
