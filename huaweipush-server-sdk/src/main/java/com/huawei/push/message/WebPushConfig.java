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
import com.huawei.push.webpush.WebHmsOptions;
import com.huawei.push.webpush.WebNotification;
import com.huawei.push.webpush.WebpushHeaders;

public class WebPushConfig {
    @JSONField(name = "headers")
    private WebpushHeaders headers;

    @JSONField(name = "data")
    private String data;

    @JSONField(name = "notification")
    private WebNotification notification;

    @JSONField(name = "hms_options")
    private WebHmsOptions webHmsOptions;

    public WebpushHeaders getHeaders() {
        return headers;
    }

    public String getData() {
        return data;
    }

    public WebNotification getNotification() {
        return notification;
    }

    public WebHmsOptions getWebHmsOptions() {
        return webHmsOptions;
    }

    public WebPushConfig(Builder builder) {
        this.headers = builder.headers;
        this.data = builder.data;
        this.notification = builder.notification;
        this.webHmsOptions = builder.webHmsOptions;
    }

    public void check() {
        if (this.headers != null) {
            this.headers.check();
        }
        if (this.notification != null) {
            this.notification.check();
        }
        if (this.webHmsOptions != null) {
            this.webHmsOptions.check();
        }
    }

    /**
     * builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private WebpushHeaders headers;

        private String data;

        private WebNotification notification;

        private WebHmsOptions webHmsOptions;

        public Builder setHeaders(WebpushHeaders headers) {
            this.headers = headers;
            return this;
        }

        public Builder setData(String data) {
            this.data = data;
            return this;
        }

        public Builder setNotification(WebNotification notification) {
            this.notification = notification;
            return this;
        }

        public Builder setWebHmsOptions(WebHmsOptions webHmsOptions) {
            this.webHmsOptions = webHmsOptions;
            return this;
        }

        public WebPushConfig build() {
            return new WebPushConfig(this);
        }
    }
}
