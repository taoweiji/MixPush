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
package com.huawei.push.messaging;

import com.huawei.push.exception.HuaweiMesssagingException;
import com.huawei.push.message.Message;
import com.huawei.push.message.TopicMessage;
import com.huawei.push.reponse.SendResponse;

/**
 * sending messages interface
 */
public interface HuaweiMessageClient {

    /**
     * Sends the given message with HCM.
     *
     * @param message      message {@link Message}
     * @param validateOnly A boolean indicating whether to send message for test. or not.
     * @return {@link SendResponse}.
     * @throws HuaweiMesssagingException
     */
    SendResponse send(Message message, boolean validateOnly, String accessToken) throws HuaweiMesssagingException;

    SendResponse send(TopicMessage message, String operation, String accessToken) throws HuaweiMesssagingException;
}
