package com.mixpush.sender;

public class MixPushPlatform {
    public final String platformName;
    public final String regId;

    public MixPushPlatform(String platformName, String regId) {
        this.platformName = platformName;
        this.regId = regId;
    }

    @Override
    public String toString() {
        return "PushPlatform{" +
                "platformName='" + platformName + '\'' +
                ", regId='" + regId + '\'' +
                '}';
    }
}
