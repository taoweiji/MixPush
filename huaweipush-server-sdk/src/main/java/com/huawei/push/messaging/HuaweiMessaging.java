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

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.huawei.push.exception.HuaweiMesssagingException;
import com.huawei.push.message.Message;
import com.huawei.push.message.TopicMessage;
import com.huawei.push.model.TopicOperation;
import com.huawei.push.reponse.SendResponse;
import com.huawei.push.util.ValidatorUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the entrance for all server-side HCM actions.
 *
 * <p>You can get a instance of {@link HuaweiMessaging}
 * by a instance of {@link HuaweiApp}, and then use it to send a message
 */
public class HuaweiMessaging {
    private static final Logger logger = LoggerFactory.getLogger(HuaweiMessaging.class);

    static final String INTERNAL_ERROR = "internal error";

    static final String UNKNOWN_ERROR = "unknown error";

    static final String KNOWN_ERROR = "known error";

    private final HuaweiApp app;
    private final Supplier<? extends HuaweiMessageClient> messagingClient;

    private HuaweiMessaging(Builder builder) {
        this.app = builder.app;
        this.messagingClient = Suppliers.memoize(builder.messagingClient);
    }

    /**
     * Gets the {@link HuaweiMessaging} instance for the specified {@link HuaweiApp}.
     *
     * @return The {@link HuaweiMessaging} instance for the specified {@link HuaweiApp}.
     */
    public static synchronized HuaweiMessaging getInstance(HuaweiApp app) {
        HuaweiMessagingService service = ImplHuaweiTrampolines.getService(app, SERVICE_ID, HuaweiMessagingService.class);
        if (service == null) {
            service = ImplHuaweiTrampolines.addService(app, new HuaweiMessagingService(app));
        }
        return service.getInstance();
    }

    private static HuaweiMessaging fromApp(final HuaweiApp app) {
        return HuaweiMessaging.builder()
                .setApp(app)
                .setMessagingClient(() -> HuaweiMessageClientImpl.fromApp(app))
                .build();
    }

    HuaweiMessageClient getMessagingClient() {
        return messagingClient.get();
    }

    /**
     * Sends the given {@link Message} via HCM.
     *
     * @param message A non-null {@link Message} to be sent.
     * @return {@link SendResponse}.
     * @throws HuaweiMesssagingException If an error occurs while handing the message off to HCM for
     *                                   delivery.
     */
    public SendResponse sendMessage(Message message) throws HuaweiMesssagingException {
        return sendMessage(message, false);
    }

    /**
     * @param topicMessage topicmessage
     * @return topic subscribe response
     * @throws HuaweiMesssagingException
     */
    public SendResponse subscribeTopic(TopicMessage topicMessage) throws HuaweiMesssagingException {
        final HuaweiMessageClient messagingClient = getMessagingClient();
        return messagingClient.send(topicMessage, TopicOperation.SUBSCRIBE.getValue(), ImplHuaweiTrampolines.getAccessToken(app));
    }

    /**
     * @param topicMessage topic Message
     * @return topic unsubscribe response
     * @throws HuaweiMesssagingException
     */
    public SendResponse unsubscribeTopic(TopicMessage topicMessage) throws HuaweiMesssagingException {
        final HuaweiMessageClient messagingClient = getMessagingClient();
        return messagingClient.send(topicMessage, TopicOperation.UNSUBSCRIBE.getValue(), ImplHuaweiTrampolines.getAccessToken(app));
    }

    /**
     * @param topicMessage topic Message
     * @return topic list
     * @throws HuaweiMesssagingException
     */
    public SendResponse listTopic(TopicMessage topicMessage) throws HuaweiMesssagingException {
        final HuaweiMessageClient messagingClient = getMessagingClient();
        return messagingClient.send(topicMessage, TopicOperation.LIST.getValue(), ImplHuaweiTrampolines.getAccessToken(app));
    }


    /**
     * Sends message {@link Message}
     *
     * <p>If the {@code validateOnly} option is set to true, the message will not be actually sent. Instead
     * HCM performs all the necessary validations, and emulates the send operation.
     *
     * @param message      message {@link Message} to be sent.
     * @param validateOnly a boolean indicating whether to send message for test or not.
     * @return {@link SendResponse}.
     * @throws HuaweiMesssagingException exception.
     */
    public SendResponse sendMessage(Message message, boolean validateOnly) throws HuaweiMesssagingException {
        ValidatorUtils.checkArgument(message != null, "message must not be null");
        final HuaweiMessageClient messagingClient = getMessagingClient();
        return messagingClient.send(message, validateOnly, ImplHuaweiTrampolines.getAccessToken(app));
    }

    /**
     * HuaweiMessagingService
     */
    private static final String SERVICE_ID = HuaweiMessaging.class.getName();

    private static class HuaweiMessagingService extends HuaweiService<HuaweiMessaging> {

        HuaweiMessagingService(HuaweiApp app) {
            super(SERVICE_ID, HuaweiMessaging.fromApp(app));
        }

        @Override
        public void destroy() {

        }
    }

    /**
     * Builder for constructing {@link HuaweiMessaging}.
     */
    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private HuaweiApp app;
        private Supplier<? extends HuaweiMessageClient> messagingClient;

        private Builder() {
        }

        public Builder setApp(HuaweiApp app) {
            this.app = app;
            return this;
        }

        public Builder setMessagingClient(Supplier<? extends HuaweiMessageClient> messagingClient) {
            this.messagingClient = messagingClient;
            return this;
        }

        public HuaweiMessaging build() {
            return new HuaweiMessaging(this);
        }
    }
}
