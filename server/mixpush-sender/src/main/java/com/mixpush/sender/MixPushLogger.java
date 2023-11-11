package com.mixpush.sender;

public class MixPushLogger {
    private static MixPushLogger logger = new MixPushLogger();

    public static MixPushLogger getLogger() {
        return logger;
    }

    public void log(String message) {
        System.out.println(message);
    }
}
