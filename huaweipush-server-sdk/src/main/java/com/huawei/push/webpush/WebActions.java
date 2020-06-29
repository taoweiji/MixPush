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

public class WebActions {
    @JSONField(name = "action")
    private String action;

    @JSONField(name = "icon")
    private String icon;

    @JSONField(name = "title")
    private String title;

    public String getAction() {
        return action;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public void check(){

    }

    public WebActions(Builder builder) {
        this.action = builder.action;
        this.icon = builder.icon;
        this.title = builder.title;
    }

    /**
     * builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String action;
        private String icon;
        private String title;

        public Builder setAction(String action) {
            this.action = action;
            return this;
        }

        public Builder setIcon(String icon) {
            this.icon = icon;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public WebActions build() {
            return new WebActions(this);
        }
    }

}
