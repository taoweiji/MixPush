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
package com.huawei.push.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.huawei.push.android.AndroidNotification;
import com.huawei.push.model.Urgency;
import com.huawei.push.util.ValidatorUtils;

import org.apache.commons.lang3.StringUtils;

public class AndroidConfig {
    private static final String TTL_PATTERN = "\\d+|\\d+[sS]|\\d+.\\d{1,9}|\\d+.\\d{1,9}[sS]";

    @JSONField(name = "collapse_key")
    private Integer collapseKey;

    @JSONField(name = "urgency")
    private String urgency;

    @JSONField(name = "category")
    private String category;

    @JSONField(name = "ttl")
    private String ttl;

    @JSONField(name = "bi_tag")
    private String biTag;

    @JSONField(name = "fast_app_target")
    private Integer fastAppTargetType;

    @JSONField(name = "data")
    private String data;

    @JSONField(name = "notification")
    private AndroidNotification notification;

    @JSONField(name = "receipt_id")
    private String receiptId;

    public AndroidConfig(Builder builder) {
        this.collapseKey = builder.collapseKey;
        this.urgency = builder.urgency;
        this.category = builder.category;

        if (null != builder.ttl) {
            this.ttl = builder.ttl;
        } else {
            this.ttl = null;
        }

        this.biTag = builder.biTag;
        this.fastAppTargetType = builder.fastAppTargetType;
        this.data = builder.data;
        this.notification = builder.notification;
        this.receiptId = builder.receiptId;
    }

    /**
     * check androidConfig's parameters
     *
     * @param notification whcic is in message
     */
    public void check(Notification notification) {
        if (this.collapseKey != null) {
            ValidatorUtils.checkArgument((this.collapseKey >= -1 && this.collapseKey <= 100), "Collapse Key should be [-1, 100]");
        }

        if (this.urgency != null) {
            ValidatorUtils.checkArgument(StringUtils.equals(this.urgency, Urgency.HIGH.getValue())
                            || StringUtils.equals(this.urgency, Urgency.NORMAL.getValue()),
                    "urgency shouid be [HIGH, NORMAL]");
        }

        if (StringUtils.isNotEmpty(this.ttl)) {
            ValidatorUtils.checkArgument(this.ttl.matches(AndroidConfig.TTL_PATTERN), "The TTL's format is wrong");
        }

        if (this.fastAppTargetType != null) {
            ValidatorUtils.checkArgument(this.fastAppTargetType == 1 || this.fastAppTargetType == 2, "Invalid fast app target type[one of 1 or 2]");
        }

        if (null != this.notification) {
            this.notification.check(notification);
        }
    }

    /**
     * getter
     */
    public Integer getCollapseKey() {
        return collapseKey;
    }

    public String getTtl() {
        return ttl;
    }

    public String getCategory() {
        return category;
    }

    public String getBiTag() {
        return biTag;
    }

    public Integer getFastAppTargetType() {
        return fastAppTargetType;
    }

    public AndroidNotification getNotification() {
        return notification;
    }

    public String getUrgency() {
        return urgency;
    }

    public String getData() {
        return data;
    }

    public String getReceiptId() { return receiptId; }

    /**
     * builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer collapseKey;
        private String urgency;
        private String category;
        private String ttl;
        private String biTag;
        private Integer fastAppTargetType;
        private String data;

        private AndroidNotification notification;
        private String receiptId;

        private Builder() {
        }

        public Builder setCollapseKey(Integer collapseKey) {
            this.collapseKey = collapseKey;
            return this;
        }

        public Builder setUrgency(String urgency) {
            this.urgency = urgency;
            return this;
        }

        public Builder setCategory(String category) {
            this.category = category;
            return this;
        }

        /**
         * time-to-live
         */
        public Builder setTtl(String ttl) {
            this.ttl = ttl;
            return this;
        }

        public Builder setBiTag(String biTag) {
            this.biTag = biTag;
            return this;
        }

        public Builder setFastAppTargetType(Integer fastAppTargetType) {
            this.fastAppTargetType = fastAppTargetType;
            return this;
        }

        public Builder setData(String data) {
            this.data = data;
            return this;
        }

        public Builder setNotification(AndroidNotification notification) {
            this.notification = notification;
            return this;
        }

        public AndroidConfig build() {
            return new AndroidConfig(this);
        }

        public Builder setReceiptId(String receiptId) {
            this.receiptId = receiptId;
            return this;
        }
    }
}
