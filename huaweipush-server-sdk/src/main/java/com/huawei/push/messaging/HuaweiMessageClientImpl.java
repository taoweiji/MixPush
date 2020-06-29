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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huawei.push.exception.HuaweiMesssagingException;
import com.huawei.push.message.Message;
import com.huawei.push.message.TopicMessage;
import com.huawei.push.model.TopicOperation;
import com.huawei.push.reponse.SendResponse;
import com.huawei.push.reponse.TopicListResponse;
import com.huawei.push.reponse.TopicSendResponse;
import com.huawei.push.util.ResponceCodeProcesser;
import com.huawei.push.util.ValidatorUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class HuaweiMessageClientImpl implements HuaweiMessageClient {
    private static final String PUSH_URL = "https://push-api.cloud.huawei.com";

    private final String HcmPushUrl;
    private String hcmTopicUrl;
    private String hcmGroupUrl;
    private String hcmTokenUrl;
    private final CloseableHttpClient httpClient;

    private HuaweiMessageClientImpl(Builder builder) {
        this.HcmPushUrl = MessageFormat.format(PUSH_URL + "/v1/{0}/messages:send", builder.appId);
        this.hcmTopicUrl = MessageFormat.format(PUSH_URL + "/v1/{0}/topic:{1}", builder.appId);

        ValidatorUtils.checkArgument(builder.httpClient != null, "requestFactory must not be null");
        this.httpClient = builder.httpClient;
    }

    /**
     * getter
     */
    public String getHcmSendUrl() {
        return HcmPushUrl;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public SendResponse send(Message message, boolean validateOnly, String accessToken) throws HuaweiMesssagingException {
        try {
            return sendRequest(message, validateOnly, accessToken);
        } catch (IOException e) {
            throw new HuaweiMesssagingException(HuaweiMessaging.INTERNAL_ERROR, "Error while calling HCM backend service", e);
        }
    }

    @Override
    public SendResponse send(TopicMessage message, String operation, String accessToken) throws HuaweiMesssagingException {
        try {
            return sendRequest(message, operation, accessToken);
        } catch (IOException e) {
            throw new HuaweiMesssagingException(HuaweiMessaging.INTERNAL_ERROR, "Error while calling HCM backend service", e);
        }
    }

    private SendResponse sendRequest(TopicMessage message, String operation, String accessToken) throws IOException, HuaweiMesssagingException {
        this.hcmTopicUrl = MessageFormat.format(hcmTopicUrl, "", operation);
        HttpPost httpPost = new HttpPost(this.hcmTopicUrl);
        StringEntity entity = new StringEntity(JSON.toJSONString(message), "UTF-8");
        httpPost.setHeader("Authorization", "Bearer " + accessToken);
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String rpsContent = EntityUtils.toString(response.getEntity());
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            JSONObject jsonObject = JSONObject.parseObject(rpsContent);
            String code = jsonObject.getString("code");
            String msg = jsonObject.getString("msg");
            String requestId = jsonObject.getString("requestId");
            if (StringUtils.equals(code, "80000000")) {
                SendResponse sendResponse;
                if (StringUtils.equals(operation, TopicOperation.LIST.getValue())) {
                    JSONArray topics = jsonObject.getJSONArray("topics");
                    sendResponse = TopicListResponse.fromCode(code, ResponceCodeProcesser.process(Integer.valueOf(code)), requestId, topics);
                } else {
                    Integer failureCount = jsonObject.getInteger("failureCount");
                    Integer successCount = jsonObject.getInteger("successCount");
                    JSONArray errors = jsonObject.getJSONArray("errors");
                    sendResponse = TopicSendResponse.fromCode(code, ResponceCodeProcesser.process(Integer.valueOf(code)), requestId, failureCount, successCount, errors);
                }
                return sendResponse;
            } else {
                String errorMsg = MessageFormat.format("error code : {0}, error message : {1}", String.valueOf(code), ResponceCodeProcesser.process(Integer.valueOf(code)));
                throw new HuaweiMesssagingException(HuaweiMessaging.KNOWN_ERROR, errorMsg);
            }
        }
        HttpResponseException exception = new HttpResponseException(statusCode, rpsContent);
        throw createExceptionFromResponse(exception);
    }

    /**
     * send request
     *
     * @param message     message {@link Message}
     * @param validateOnly A boolean indicating whether to send message for test or not.
     * @param accessToken  A String for oauth
     * @return {@link SendResponse}
     * @throws IOException If a error occurs when sending request
     */
    private SendResponse sendRequest(Message message, boolean validateOnly, String accessToken) throws IOException, HuaweiMesssagingException {
        Map<String, Object> map = createRequestMap(message, validateOnly);
        HttpPost httpPost = new HttpPost(this.HcmPushUrl);
        StringEntity entity = new StringEntity(JSON.toJSONString(map), "UTF-8");
//        String aqa = JSON.toJSONString(map);
        httpPost.setHeader("Authorization", "Bearer " + accessToken);
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String rpsContent = EntityUtils.toString(response.getEntity());
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            JSONObject jsonObject = JSONObject.parseObject(rpsContent);
            String code = jsonObject.getString("code");
            String msg = jsonObject.getString("msg");
            String requestId = jsonObject.getString("requestId");
            if (StringUtils.equals(code, "80000000")) {
                return SendResponse.fromCode(code, ResponceCodeProcesser.process(Integer.valueOf(code)), requestId);
            } else {
                String errorMsg = MessageFormat.format("error code : {0}, error message : {1}", String.valueOf(code), ResponceCodeProcesser.process(Integer.valueOf(code)));
                throw new HuaweiMesssagingException(HuaweiMessaging.KNOWN_ERROR, errorMsg);
            }
        }
        HttpResponseException exception = new HttpResponseException(statusCode, rpsContent);
        throw createExceptionFromResponse(exception);
    }

    /**
     * create the map of the request body, mostly for wrapping the message with validate_only
     *
     * @param message      A non-null {@link Message} to be sent.
     * @param validateOnly A boolean indicating whether to send message for test or not.
     * @return a map of request
     */
    private Map<String, Object> createRequestMap(Message message, boolean validateOnly) {
        return new HashMap<String, Object>() {
            {
                put("validate_only", validateOnly);
                put("message", message);
            }
        };
    }

    private HuaweiMesssagingException createExceptionFromResponse(HttpResponseException e) {
        String msg = MessageFormat.format("Unexpected HTTP response with status : {0}, body : {1}", e.getStatusCode(), e.getMessage());
        return new HuaweiMesssagingException(HuaweiMessaging.UNKNOWN_ERROR, msg, e);
    }

    static HuaweiMessageClientImpl fromApp(HuaweiApp app) {
        String appId = ImplHuaweiTrampolines.getAppId(app);
        return HuaweiMessageClientImpl.builder()
                .setAppId(appId)
                .setHttpClient(app.getOption().getHttpClient())
                .build();
    }

    static Builder builder() {
        return new Builder();
    }

    static final class Builder {

        private String appId;
        private CloseableHttpClient httpClient;

        private Builder() {
        }

        public Builder setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        public Builder setHttpClient(CloseableHttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public HuaweiMessageClientImpl build() {
            return new HuaweiMessageClientImpl(this);
        }
    }
}
