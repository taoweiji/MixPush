package com.mixpush.server;

/**
 * Created by Wiki on 2017/6/1.
 */

public class MixMessage {
    private String content;

    public MixMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
