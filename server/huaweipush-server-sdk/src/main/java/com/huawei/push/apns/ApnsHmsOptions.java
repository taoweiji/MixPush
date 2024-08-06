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
package com.huawei.push.apns;

import com.alibaba.fastjson.annotation.JSONField;
import com.huawei.push.util.ValidatorUtils;

public class ApnsHmsOptions {
    private static final int TEST_USER = 1;
    private static final int FORMAL_USER = 2;
    private static final int VOIP_USER = 3;

    @JSONField(name = "target_user_type")
    private Integer targetUserType;

    public Integer getTargetUserType() {
        return targetUserType;
    }

    private ApnsHmsOptions(Builder builder){
        this.targetUserType = builder.targetUserType;
    }

    public void check(){
        if (targetUserType != null) {
            ValidatorUtils.checkArgument(this.targetUserType.intValue() == TEST_USER
                            || this.targetUserType.intValue() == FORMAL_USER
                            || this.targetUserType.intValue() == VOIP_USER,
                    "targetUserType should be [TEST_USER: 1, FORMAL_USER: 2, VOIP_USER: 3]");
        }
    }

    /**
     * builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer targetUserType;

        public Builder setTargetUserType(Integer targetUserType) {
            this.targetUserType = targetUserType;
            return this;
        }

        public ApnsHmsOptions build(){
            return new ApnsHmsOptions(this);
        }
    }
}
