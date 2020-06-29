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

public class HuaweiMesssagingException extends HuaweiException {
    private final String errorCode;

    public HuaweiMesssagingException(String errorCode, String message) {
        super(message);
        ValidatorUtils.checkArgument(!Strings.isNullOrEmpty(errorCode));
        this.errorCode = errorCode;
    }

    public HuaweiMesssagingException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        ValidatorUtils.checkArgument(!Strings.isNullOrEmpty(errorCode));
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
