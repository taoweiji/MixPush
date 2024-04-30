package com.mixpush.core;

public interface MixPushLogger {
    void log(String tag, String content, Throwable throwable);

    void log(String tag, String content);
}
