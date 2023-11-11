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
import com.huawei.push.util.ValidatorUtils;

import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class WebHmsOptions {
    @JSONField(name = "link")
    private String link;

    public String getLink() {
        return link;
    }

    public WebHmsOptions(Builder builder) {
        this.link = builder.link;
    }

    public void check() {
        if (!StringUtils.isEmpty(this.link)) {
            try {
                new URL(link);
            } catch (MalformedURLException e) {
                ValidatorUtils.checkArgument(false, "Invalid link");
            }
        }
    }

    /**
     * builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String link;

        public Builder setLink(String link) {
            this.link = link;
            return this;
        }

        public WebHmsOptions build() {
            return new WebHmsOptions(this);
        }
    }
}
