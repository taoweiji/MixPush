package com.mixpush.sender;

import java.util.ArrayList;
import java.util.List;

public class MixPushResult {
    public static final String TEST_ENV_CAN_NOT_BROADCAST = "100";
    public static final String TEST_DATA_CAN_NOT_BROADCAST = "101";
    public static final String NOT_SUPPORT_BROADCAST = "102";
    public static final String NOT_SUPPORT = "103";
    public static final String NOT_SUPPORT_PASS_THROUGH = "104";

    private String statusCode;
    private String reason;
    private List<MixPushResult> results = new ArrayList<>();
    private Object extra;
    private String platformName;
    private String messageId;
    private Throwable error;
    private boolean succeed;

    private MixPushResult() {

    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getReason() {
        return reason;
    }

    public List<MixPushResult> getResults() {
        return results;
    }

    public Object getExtra() {
        return extra;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getMessageId() {
        return messageId;
    }

    public Throwable getError() {
        return error;
    }

    public boolean isSucceed() {
        return succeed;
    }

    private static List<MixPushResult> getList(List<MixPushResult> list) {
        List<MixPushResult> newList = new ArrayList<>();
        for (MixPushResult it : list) {
            if (it.results.isEmpty()) {
                newList.add(it);
            } else {
                newList.addAll(it.results);
                it.results = new ArrayList<>();
            }
        }
        return newList;
    }

    public static MixPushResult build(List<MixPushResult> results) {
        List<MixPushResult> newList = getList(results);
        MixPushResult first = newList.get(0);
        if (newList.size() == 1) {
            return first;
        }
        MixPushResult result = new MixPushResult();
        result.messageId = first.messageId;
        result.succeed = true;
        for (MixPushResult it : newList) {
            if (!it.isSucceed()) {
                result.succeed = false;
                result.reason = it.reason;
                result.statusCode = it.statusCode;
                result.error = it.error;
                result.extra = it.extra;
                break;
            }
        }
        result.results = results;
        return result;
    }

    @Override
    public String toString() {

        return "MixPushResult{" +
                "succeed='" + succeed + '\'' +
                ", platformName='" + platformName + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", reason='" + reason + '\'' +
                ", messageId='" + messageId + '\'' +
                ", results=" + results +
                ", extra=" + extra +
                ", error=" + error +
                '}';
    }

    public static class Builder {
        MixPushResult result = new MixPushResult();

        public MixPushResult build() {
            return result;
        }

        public Builder reason(String reason) {
            result.reason = reason;
            return this;
        }

        public Builder statusCode(String statusCode) {
            result.statusCode = statusCode;
            return this;
        }

        public Builder extra(Object extra) {
            result.extra = extra;
            return this;
        }

        public Builder succeed(boolean success) {
            result.succeed = success;
            return this;
        }

        public Builder provider(MixPushProvider provider) {
            result.platformName = provider.platformName();
            return this;
        }

        public Builder error(Throwable throwable) {
            result.statusCode = "-1";
            result.reason = throwable.toString();
            result.error = throwable;
            return this;
        }

        public Builder message(MixPushMessage message) {
            result.messageId = message.getMessageId();
            return this;
        }

        public Builder platformName(String platformName) {
            result.platformName = platformName;
            return this;
        }
    }
}
