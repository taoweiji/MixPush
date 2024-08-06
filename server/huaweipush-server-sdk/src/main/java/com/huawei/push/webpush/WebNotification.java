/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.huawei.push.webpush;

import com.alibaba.fastjson.annotation.JSONField;
import com.huawei.push.util.CollectionUtils;
import com.huawei.push.util.ValidatorUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebNotification {
    private static String[] DIR_VALUE = {"auto", "ltr", "rtl"};

    @JSONField(name = "title")
    private String title;

    @JSONField(name = "body")
    private String body;

    @JSONField(name = "icon")
    private String icon;

    @JSONField(name = "image")
    private String image;

    @JSONField(name = "lang")
    private String lang;

    @JSONField(name = "tag")
    private String tag;

    @JSONField(name = "badge")
    private String badge;

    @JSONField(name = "dir")
    private String dir;

    @JSONField(name = "vibrate")
    private List<Integer> vibrate = new ArrayList<>();

    @JSONField(name = "renotify")
    private boolean renotify;

    @JSONField(name = "require_interaction")
    private boolean requireInteraction;

    @JSONField(name = "silent")
    private boolean silent;

    @JSONField(name = "timestamp")
    private Long timestamp;

    @JSONField(name = "actions")
    private List<WebActions> actions = new ArrayList<WebActions>();

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getIcon() {
        return icon;
    }

    public String getImage() {
        return image;
    }

    public String getLang() {
        return lang;
    }

    public String getTag() {
        return tag;
    }

    public String getBadge() {
        return badge;
    }

    public String getDir() {
        return dir;
    }

    public List<Integer> getVibrate() {
        return vibrate;
    }

    public boolean isRenotify() {
        return renotify;
    }

    public boolean isRequireInteraction() {
        return requireInteraction;
    }

    public boolean isSilent() {
        return silent;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public List<WebActions> getActions() {
        return actions;
    }

    public void check() {
        if (this.dir != null) {
            ValidatorUtils.checkArgument(Arrays.stream(DIR_VALUE).anyMatch(value -> value.equals(dir)), "Invalid dir");
        }
        if (this.vibrate != null) {
            for (Object obj : vibrate) {
                ValidatorUtils.checkArgument((obj instanceof Double || obj instanceof Integer), "Invalid vibrate");
            }
        }
    }

    public WebNotification(Builder builder) {
        this.title = builder.title;
        this.body = builder.body;
        this.icon = builder.icon;
        this.image = builder.image;
        this.lang = builder.lang;
        this.tag = builder.tag;
        this.badge = builder.badge;
        this.dir = builder.dir;
        if (!CollectionUtils.isEmpty(builder.vibrate)) {
            this.vibrate.addAll(builder.vibrate);
        } else {
            this.vibrate = null;
        }

        this.renotify = builder.renotify;
        this.requireInteraction = builder.requireInteraction;
        this.silent = builder.silent;
        this.timestamp = builder.timestamp;
        if (!CollectionUtils.isEmpty(builder.actions)) {
            this.actions.addAll(builder.actions);
        } else {
            this.actions = null;
        }
    }

    /**
     * builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String body;
        private String icon;
        private String image;
        private String lang;
        private String tag;
        private String badge;
        private String dir;
        private List<Integer> vibrate = new ArrayList<>();
        private boolean renotify;
        private boolean requireInteraction;
        private boolean silent;
        private Long timestamp;
        private List<WebActions> actions = new ArrayList<WebActions>();

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public Builder setIcon(String icon) {
            this.icon = icon;
            return this;
        }

        public Builder setImage(String image) {
            this.image = image;
            return this;
        }

        public Builder setLang(String lang) {
            this.lang = lang;
            return this;
        }

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder setBadge(String badge) {
            this.badge = badge;
            return this;
        }

        public Builder setDir(String dir) {
            this.dir = dir;
            return this;
        }

        public Builder addAllVibrate(List<Integer> vibrate) {
            this.vibrate.addAll(vibrate);
            return this;
        }

        public Builder addVibrate(Integer vibrate) {
            this.vibrate.add(vibrate);
            return this;
        }

        public Builder setRenotify(boolean renotify) {
            this.renotify = renotify;
            return this;
        }

        public Builder setRequireInteraction(boolean requireInteraction) {
            this.requireInteraction = requireInteraction;
            return this;
        }

        public Builder setSilent(boolean silent) {
            this.silent = silent;
            return this;
        }

        public Builder setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder addAllActions(List<WebActions> actions) {
            this.actions.addAll(actions);
            return this;
        }

        public Builder addAction(WebActions action) {
            this.actions.add(action);
            return this;
        }

        public WebNotification build() {
            return new WebNotification(this);
        }

    }

}
