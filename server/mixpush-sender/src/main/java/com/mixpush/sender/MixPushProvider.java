package com.mixpush.sender;

import java.util.ArrayList;
import java.util.List;

public abstract class MixPushProvider {

    /**
     * 一次推送最多推送的人数
     */
    protected int groupSize() {
        return 1000;
    }

    public final MixPushResult sendMessage(MixPushMessage mixPushMessage, List<String> regIds) {
        List<List<String>> regIdsList = split(regIds, groupSize());
        List<MixPushResult> results = new ArrayList<>();
        for (List<String> item : regIdsList) {
            if (item.size() == 1) {
                results.add(sendMessageToSingle(mixPushMessage, item.get(0)));
            } else {
                results.add(sendMessageToList(mixPushMessage, item));
            }
        }
        return MixPushResult.build(results);
    }

    protected abstract MixPushResult sendMessageToSingle(MixPushMessage mixPushMessage, String regId);

    protected abstract MixPushResult sendMessageToList(MixPushMessage mixPushMessage, List<String> regIds);

    /**
     * 华为/apns 不支持该接口
     */
    public abstract MixPushResult broadcastMessageToAll(MixPushMessage mixPushMessage);

    protected abstract String platformName();

    public abstract boolean isSupportBroadcastAll(boolean isPassThrough);

    public abstract boolean isSupportPassThrough();


    private List<List<String>> split(List<String> list, int count) {
        List<List<String>> result = new ArrayList<>();
        int start = 0;
        while (start < list.size()) {
            int end = start + count;
            if (end > list.size()) {
                end = list.size();
            }
            result.add(list.subList(start, end));
            start = end + 1;
        }
        return result;
    }
}
