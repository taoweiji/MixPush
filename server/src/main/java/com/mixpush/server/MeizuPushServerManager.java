package com.mixpush.server;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

/**
 * Created by Wiki on 2017/6/1.
 */

public class MeizuPushServerManager implements MixPushServerManager{
    @Override
    public void sendMessageToAlias(List<String> alias, String messagePayload) throws IOException {

    }

    @Override
    public void sendMessageToTags(List<String> tags, String messagePayload) {

    }

    @Override
    public void sendMessageToAll(String messagePayload) throws IOException, ParseException {

    }

    @Override
    public void sendNotifyToAlias(List<String> alias, String title, String description, String messagePayload) {

    }

    @Override
    public void sendNotifyToTags(List<String> tags, String title, String description, String messagePayload) {

    }

    @Override
    public void sendNotifyToAll(String title, String description, String messagePayload) throws IOException, ParseException {

    }
}
