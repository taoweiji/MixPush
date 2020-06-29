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
import com.huawei.push.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class TopicMessage {
    @JSONField(name = "topic")
    private String topic;

    @JSONField(name = "tokenArray")
    private List<String> tokenArray = new ArrayList<String>();

    @JSONField(name = "token")
    private String token;

    public String getTopic() {
        return topic;
    }

    public List<String> getTokenArray() {
        return tokenArray;
    }

    public String getToken() {
        return token;
    }

    private TopicMessage(Builder builder) {
        this.topic = builder.topic;
        if (!CollectionUtils.isEmpty(builder.tokenArray)) {
            this.tokenArray.addAll(builder.tokenArray);
        } else {
            this.tokenArray = null;
        }
        this.token = builder.token;
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
        private String topic;
        private List<String> tokenArray = new ArrayList<String>();
        private String token;

        public Builder setTopic(String topic) {
            this.topic = topic;
            return this;
        }

        public Builder addToken(String token) {
            this.tokenArray.add(token);
            return this;
        }

        public Builder addAllToken(List<String> tokenArray) {
            this.tokenArray.addAll(tokenArray);
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public TopicMessage build() {
            return new TopicMessage(this);
        }

    }

}
