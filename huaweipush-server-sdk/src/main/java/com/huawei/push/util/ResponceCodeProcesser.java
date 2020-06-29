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
package com.huawei.push.util;

import java.util.HashMap;
import java.util.Map;

/**
 * A tool for processing the responce code
 */
public class ResponceCodeProcesser {
    private static final Map<Integer, String> codeMap = new HashMap<Integer, String>() {
        {
            put(80000000, "Success");
            put(80100000, "Some tokens are right, the others are illegal");
            put(80100001, "Parameters check error");
            put(80100002, "Token number should be one when send sys message");
            put(80100003, "Message structure error");
            put(80100004, "TTL is less than current time, please check");
            put(80100013, "Collapse_key is illegal, please check");
            put(80100016, "Message contians sensitive information, please check");
            put(80200001, "Oauth authentication error");
            put(80200003, "Oauth Token expired");
            put(80300002, "APP is forbidden to send");
            put(80300007, "Invalid Token");
            put(80300008, "The message body size exceeds the default value set by the system (4K)");
            put(80300010, "Tokens exceed the default value");
            put(81000001, "System inner error");
            put(82000001, "GroupKey or groupName error");
            put(82000002, "GroupKey and groupName do not match");
            put(82000003, "Token array is null");
            put(82000004, "Group do not exist");
            put(82000005, "Group do not belond to this app");
            put(82000006, "Token array or group number is transfinited");
            put(82000007, "Invalid topic");
            put(82000008, "Token array null or transfinited");
            put(82000009, "Topic num transfinited, at most 2000");
            put(82000010, "Some token is wrong");
            put(82000011, "Token is null");
            put(82000012, "Data storage location is not selected");
            put(82000013, "Data storage location does not match the actual data");
        }
    };

    public static String process(Integer code) {
        return codeMap.getOrDefault(code, "Unknown code");
    }
}
