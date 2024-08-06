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
package com.huawei.push.reponse;

import com.alibaba.fastjson.JSONArray;

public final class TopicSendResponse extends SendResponse {
    private final Integer failureCount;
    private final Integer successCount;
    private final JSONArray errors;

    public Integer getFailureCount() {
        return failureCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public JSONArray getErrors() {
        return errors;
    }

    private TopicSendResponse(String code, String msg, String requestId, Integer failureCount, Integer successCount, JSONArray errors) {
        super(code,msg,requestId);
        this.failureCount = failureCount;
        this.successCount = successCount;
        this.errors = errors;
    }

    public static TopicSendResponse fromCode(String code, String msg, String requestId,Integer failureCount,Integer successCount,JSONArray errors ) {
        return new TopicSendResponse(code, msg, requestId,failureCount,successCount,errors);
    }
}
