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
package com.huawei.push.exception;

import com.google.common.base.Strings;
import com.huawei.push.util.ValidatorUtils;

/**
 * exceptions
 */
public class HuaweiException extends Exception {

    public HuaweiException(String detailMessage) {
        super(detailMessage);
        ValidatorUtils.checkArgument(!Strings.isNullOrEmpty(detailMessage), "Detail message must not be empty");
    }

    public HuaweiException(String detailMessage, Throwable cause) {
        super(detailMessage, cause);
        ValidatorUtils.checkArgument(!Strings.isNullOrEmpty(detailMessage), "Detail message must not be empty");
    }
}
