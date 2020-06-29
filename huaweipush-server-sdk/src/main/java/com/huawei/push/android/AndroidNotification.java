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
package com.huawei.push.android;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.huawei.push.message.Notification;
import com.huawei.push.model.Importance;
import com.huawei.push.model.Visibility;
import com.huawei.push.util.CollectionUtils;
import com.huawei.push.util.ValidatorUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class AndroidNotification {

    private static final String COLOR_PATTERN = "^#[0-9a-fA-F]{6}$";

    private static final String URL_PATTERN = "^https.*";

    private static final String VIBRATE_PATTERN = "[0-9]+|[0-9]+[sS]|[0-9]+[.][0-9]{1,9}|[0-9]+[.][0-9]{1,9}[sS]";

    @JSONField(name = "title")
    private String title;

    @JSONField(name = "body")
    private String body;

    @JSONField(name = "icon")
    private String icon;

    @JSONField(name = "color")
    private String color;

    @JSONField(name = "sound")
    private String sound;

    @JSONField(name = "default_sound")
    private boolean defaultSound;

    @JSONField(name = "tag")
    private String tag;

    @JSONField(name = "click_action")
    private ClickAction clickAction;

    @JSONField(name = "body_loc_key")
    private String bodyLocKey;

    @JSONField(name = "body_loc_args")
    private List<String> bodyLocArgs = new ArrayList<>();

    @JSONField(name = "title_loc_key")
    private String titleLocKey;

    @JSONField(name = "title_loc_args")
    private List<String> titleLocArgs = new ArrayList<>();

    @JSONField(name = "multi_lang_key")
    private JSONObject multiLangKey;

    @JSONField(name = "channel_id")
    private String channelId;

    @JSONField(name = "notify_summary")
    private String notifySummary;

    @JSONField(name = "image")
    private String image;

    @JSONField(name = "style")
    private Integer style;

    @JSONField(name = "big_title")
    private String bigTitle;

    @JSONField(name = "big_body")
    private String bigBody;

    @JSONField(name = "auto_clear")
    private Integer autoClear;

    @JSONField(name = "notify_id")
    private Integer notifyId;

    @JSONField(name = "group")
    private String group;

    @JSONField(name = "badge")
    private BadgeNotification badge;

    @JSONField(name = "ticker")
    private String ticker;

    @JSONField(name = "auto_cancel")
    private boolean autoCancel;

    @JSONField(name = "when")
    private String when;

    @JSONField(name = "local_only")
    private Boolean localOnly;

    @JSONField(name = "importance")
    private String importance;

    @JSONField(name = "use_default_vibrate")
    private boolean useDefaultVibrate;

    @JSONField(name = "use_default_light")
    private boolean useDefaultLight;

    @JSONField(name = "vibrate_config")
    private List<String> vibrateConfig = new ArrayList<>();

    @JSONField(name = "visibility")
    private String visibility;

    @JSONField(name = "light_settings")
    private LightSettings lightSettings;

    @JSONField(name = "foreground_show")
    private boolean foregroundShow;

    private AndroidNotification(Builder builder) {
        this.title = builder.title;
        this.body = builder.body;
        this.icon = builder.icon;
        this.color = builder.color;
        this.sound = builder.sound;
        this.defaultSound = builder.defaultSound;

        this.tag = builder.tag;
        this.clickAction = builder.clickAction;

        this.bodyLocKey = builder.bodyLocKey;
        if (!CollectionUtils.isEmpty(builder.bodyLocArgs)) {
            this.bodyLocArgs.addAll(builder.bodyLocArgs);
        } else {
            this.bodyLocArgs = null;
        }

        this.titleLocKey = builder.titleLocKey;
        if (!CollectionUtils.isEmpty(builder.titleLocArgs)) {
            this.titleLocArgs.addAll(builder.titleLocArgs);
        } else {
            this.titleLocArgs = null;
        }

        if (builder.multiLangkey != null) {
            this.multiLangKey = builder.multiLangkey;
        } else {
            this.multiLangKey = null;
        }

        this.channelId = builder.channelId;

        this.notifySummary = builder.notifySummary;
        this.image = builder.image;
        this.style = builder.style;
        this.bigTitle = builder.bigTitle;
        this.bigBody = builder.bigBody;
        this.autoClear = builder.autoClear;
        this.notifyId = builder.notifyId;
        this.group = builder.group;

        if (null != builder.badge) {
            this.badge = builder.badge;
        } else {
            this.badge = null;
        }

        this.ticker = builder.ticker;
        this.autoCancel = builder.autoCancel;
        this.when = builder.when;
        this.importance = builder.importance;
        this.useDefaultVibrate = builder.useDefaultVibrate;
        this.useDefaultLight = builder.useDefaultLight;
        if (!CollectionUtils.isEmpty(builder.vibrateConfig)) {
            this.vibrateConfig = builder.vibrateConfig;
        } else {
            this.vibrateConfig = null;
        }

        this.visibility = builder.visibility;
        this.lightSettings = builder.lightSettings;
        this.foregroundShow = builder.foregroundShow;
    }

    /**
     * check androidNotification's parameters
     *
     * @param notification which is in message
     */
    public void check(Notification notification) {
        if (null != notification) {
            ValidatorUtils.checkArgument(StringUtils.isNotEmpty(notification.getTitle()) || StringUtils.isNotEmpty(this.title), "title should be set");
            ValidatorUtils.checkArgument(StringUtils.isNotEmpty(notification.getBody()) || StringUtils.isNotEmpty(this.body), "body should be set");
        }

        if (StringUtils.isNotEmpty(this.color)) {
            ValidatorUtils.checkArgument(this.color.matches(AndroidNotification.COLOR_PATTERN), "Wrong color format, color must be in the form #RRGGBB");
        }

        if (this.clickAction != null) {
            this.clickAction.check();
        }

        if (!CollectionUtils.isEmpty(this.bodyLocArgs)) {
            ValidatorUtils.checkArgument(StringUtils.isNotEmpty(this.bodyLocKey), "bodyLocKey is required when specifying bodyLocArgs");
        }

        if (!CollectionUtils.isEmpty(this.titleLocArgs)) {
            ValidatorUtils.checkArgument(StringUtils.isNotEmpty(this.titleLocKey), "titleLocKey is required when specifying titleLocArgs");
        }

        if (StringUtils.isNotEmpty(this.image)) {
            ValidatorUtils.checkArgument(this.image.matches(URL_PATTERN), "notifyIcon must start with https");
        }

        //Style 0,1,2
        if (this.style != null) {
            boolean isTrue = this.style == 0 ||
                    this.style == 1 ||
                    this.style == 2;
            ValidatorUtils.checkArgument(isTrue, "style should be one of 0:default, 1: big text, 2: big picture");

            if (this.style == 1) {
                ValidatorUtils.checkArgument(StringUtils.isNotEmpty(this.bigTitle) && StringUtils.isNotEmpty(this.bigBody), "title and body are required when style = 1");
            }
        }

        if (this.autoClear != null) {
            ValidatorUtils.checkArgument(this.autoClear.intValue() > 0, "auto clear should positive value");
        }

        if (badge != null) {
            this.badge.check();
        }

        if (this.importance != null) {
            ValidatorUtils.checkArgument(StringUtils.equals(this.importance, Importance.LOW.getValue())
                            || StringUtils.equals(this.importance, Importance.NORMAL.getValue())
                            || StringUtils.equals(this.importance, Importance.HIGH.getValue()),
                    "importance shouid be [HIGH, NORMAL, LOW]");
        }

        if (!CollectionUtils.isEmpty(this.vibrateConfig)) {
            ValidatorUtils.checkArgument(this.vibrateConfig.size() <= 10, "vibrate_config array size cannot be more than 10");
            for (String vibrateTiming : this.vibrateConfig) {
                ValidatorUtils.checkArgument(vibrateTiming.matches(AndroidNotification.VIBRATE_PATTERN), "Wrong vibrate timing format");
                long vibrateTimingValue = (long) (1000 * Double
                        .valueOf(StringUtils.substringBefore(vibrateTiming.toLowerCase(Locale.getDefault()), "s")));
                ValidatorUtils.checkArgument(vibrateTimingValue > 0 && vibrateTimingValue < 60, "Vibrate timing duration must be greater than 0 and less than 60s");
            }
        }

        if (this.visibility != null) {
            ValidatorUtils.checkArgument(StringUtils.equals(this.visibility, Visibility.VISIBILITY_UNSPECIFIED.getValue())
                            || StringUtils.equals(this.visibility, Visibility.PRIVATE.getValue())
                            || StringUtils.equals(this.visibility, Visibility.PUBLIC.getValue())
                            || StringUtils.equals(this.visibility, Visibility.SECRET.getValue()),
                    "visibility shouid be [VISIBILITY_UNSPECIFIED, PRIVATE, PUBLIC, SECRET]");
        }

        // multiLangKey
        if (null != this.multiLangKey) {
            Iterator<String> keyIterator = this.multiLangKey.keySet().iterator();
            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                ValidatorUtils.checkArgument((this.multiLangKey.get(key) instanceof JSONObject), "multiLangKey value should be JSONObject");
                JSONObject contentsObj = multiLangKey.getJSONObject(key);
                if (contentsObj != null) {
                    ValidatorUtils.checkArgument(contentsObj.keySet().size() <= 3, "Only three lang property can carry");
                }
            }
        }

        if (this.lightSettings != null) {
            this.lightSettings.check();
        }
    }

    /**
     * getter
     */
    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getIcon() {
        return icon;
    }

    public String getColor() {
        return color;
    }

    public String getSound() {
        return sound;
    }

    public Boolean isDefaultSound() {
        return defaultSound;
    }

    public String getTag() {
        return tag;
    }

    public ClickAction getClickAction() {
        return clickAction;
    }

    public String getBodyLocKey() {
        return bodyLocKey;
    }

    public List<String> getBodyLocArgs() {
        return bodyLocArgs;
    }

    public String getTitleLocKey() {
        return titleLocKey;
    }

    public List<String> getTitleLocArgs() {
        return titleLocArgs;
    }

    public JSONObject getMultiLangKey() {
        return multiLangKey;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getNotifySummary() {
        return notifySummary;
    }

    public String getImage() {
        return image;
    }

    public Integer getStyle() {
        return style;
    }

    public String getBigTitle() {
        return bigTitle;
    }

    public String getBigBody() {
        return bigBody;
    }

    public Integer getAutoClear() {
        return autoClear;
    }

    public Integer getNotifyId() {
        return notifyId;
    }

    public String getGroup() {
        return group;
    }

    public BadgeNotification getBadge() {
        return badge;
    }

    public String getTicker() {
        return ticker;
    }

    public String getWhen() {
        return when;
    }

    public String getImportance() {
        return importance;
    }

    public List<String> getVibrateConfig() {
        return vibrateConfig;
    }

    public String getVisibility() {
        return visibility;
    }

    public LightSettings getLightSettings() {
        return lightSettings;
    }

    public boolean isAutoCancel() {
        return autoCancel;
    }

    public Boolean getLocalOnly() {
        return localOnly;
    }

    public boolean isUseDefaultVibrate() {
        return useDefaultVibrate;
    }

    public boolean isUseDefaultLight() {
        return useDefaultLight;
    }

    public boolean isForegroundShow() {
        return foregroundShow;
    }

    /**
     * builder
     *
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String title;
        private String body;
        private String icon;
        private String color;
        private String sound;
        private boolean defaultSound;
        private String tag;
        private ClickAction clickAction;
        private String bodyLocKey;
        private List<String> bodyLocArgs = new ArrayList<>();
        private String titleLocKey;
        private List<String> titleLocArgs = new ArrayList<>();
        private JSONObject multiLangkey;
        private String channelId;
        private String notifySummary;
        private String image;
        private Integer style;
        private String bigTitle;
        private String bigBody;
        private Integer autoClear;
        private Integer notifyId;
        private String group;

        private BadgeNotification badge;

        private String ticker;
        private boolean autoCancel = true;
        private String when;
        private String importance;
        private boolean useDefaultVibrate;
        private boolean useDefaultLight;
        private List<String> vibrateConfig = new ArrayList<>();
        private String visibility;
        private LightSettings lightSettings;
        private boolean foregroundShow;

        private Builder() {
        }

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

        public Builder setColor(String color) {
            this.color = color;
            return this;
        }

        public Builder setSound(String sound) {
            this.sound = sound;
            return this;
        }

        public Builder setDefaultSound(boolean defaultSound) {
            this.defaultSound = defaultSound;
            return this;
        }

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder setClickAction(ClickAction clickAction) {
            this.clickAction = clickAction;
            return this;
        }

        public Builder setBodyLocKey(String bodyLocKey) {
            this.bodyLocKey = bodyLocKey;
            return this;
        }

        public Builder addBodyLocArgs(String arg) {
            this.bodyLocArgs.add(arg);
            return this;
        }

        public Builder addAllBodyLocArgs(List<String> args) {
            this.bodyLocArgs.addAll(args);
            return this;
        }

        public Builder setTitleLocKey(String titleLocKey) {
            this.titleLocKey = titleLocKey;
            return this;
        }

        public Builder addTitleLocArgs(String arg) {
            this.titleLocArgs.add(arg);
            return this;
        }

        public Builder addAllTitleLocArgs(List<String> args) {
            this.titleLocArgs.addAll(args);
            return this;
        }

        public Builder setMultiLangkey(JSONObject multiLangkey) {
            this.multiLangkey = multiLangkey;
            return this;
        }

        public Builder setChannelId(String channelId) {
            this.channelId = channelId;
            return this;
        }

        public Builder setNotifySummary(String notifySummary) {
            this.notifySummary = notifySummary;
            return this;
        }

        public Builder setImage(String image) {
            this.image = image;
            return this;
        }

        public Builder setStyle(Integer style) {
            this.style = style;
            return this;
        }

        public Builder setBigTitle(String bigTitle) {
            this.bigTitle = bigTitle;
            return this;
        }

        public Builder setBigBody(String bigBody) {
            this.bigBody = bigBody;
            return this;
        }

        public Builder setAutoClear(Integer autoClear) {
            this.autoClear = autoClear;
            return this;
        }

        public Builder setNotifyId(Integer notifyId) {
            this.notifyId = notifyId;
            return this;
        }

        public Builder setGroup(String group) {
            this.group = group;
            return this;
        }

        public Builder setBadge(BadgeNotification badge) {
            this.badge = badge;
            return this;
        }

        public Builder setTicker(String ticker) {
            this.ticker = ticker;
            return this;
        }

        public Builder setAutoCancel(boolean autoCancel) {
            this.autoCancel = autoCancel;
            return this;
        }

        public Builder setWhen(String when) {
            this.when = when;
            return this;
        }

        public Builder setImportance(String importance) {
            this.importance = importance;
            return this;
        }

        public Builder setUseDefaultVibrate(boolean useDefaultVibrate) {
            this.useDefaultVibrate = useDefaultVibrate;
            return this;
        }

        public Builder setUseDefaultLight(boolean useDefaultLight) {
            this.useDefaultLight = useDefaultLight;
            return this;
        }

        public Builder addVibrateConfig(String vibrateTiming) {
            this.vibrateConfig.add(vibrateTiming);
            return this;
        }


        public Builder addAllVibrateConfig(List<String> vibrateTimings) {
            this.vibrateConfig.addAll(vibrateTimings);
            return this;
        }

        public Builder setVisibility(String visibility) {
            this.visibility = visibility;
            return this;
        }

        public Builder setLightSettings(LightSettings lightSettings) {
            this.lightSettings = lightSettings;
            return this;
        }

        public Builder setForegroundShow(boolean foregroundShow) {
            this.foregroundShow = foregroundShow;
            return this;
        }

        public AndroidNotification build() {
            return new AndroidNotification(this);
        }
    }
}
