/* Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.

 *  2019.12.15-changed constructor TokenRefresher
 *                  Huawei Technologies Co., Ltd.
 *
 */
package com.huawei.push.messaging;

import com.huawei.push.util.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

/**
 * Utility class for scheduling proactive token refresh events. Each HuaweiApp should have
 * its own instance of this class. TokenRefresher schedules token refresh events when token is expired.
 *
 * <p>This class is thread safe. It will handle only one token change event at a time. It also
 * cancels any pending token refresh events, before scheduling a new one.
 */
public class TokenRefresher {
    private static final Logger logger = LoggerFactory.getLogger(TokenRefresher.class);

    private HuaweiApp app;
    private HuaweiCredential credential;

    private Future future;

    public TokenRefresher(HuaweiApp app) {
        ValidatorUtils.checkArgument(app != null, "app must not be null");
        this.app = app;
        this.credential = app.getOption().getCredential();
    }

    protected void scheduleNext(Runnable task, long initialDelay, long period) {
        logger.debug("Scheduling next token refresh in {} milliseconds", period);
        try {
            future = app.schedule(task, initialDelay, period);
        } catch (UnsupportedOperationException e) {
            logger.debug("Failed to schedule token refresh event", e);
        }
    }

    /**
     * Schedule a forced token refresh to be executed after a specified period.
     *
     * @param period in milliseconds, after which the token should be forcibly refreshed.
     */
    public void scheduleRefresh(final long period) {
        cancelPrevious();
        scheduleNext(() -> credential.refreshToken(), period, period);
    }

    private void cancelPrevious() {
        if (future != null) {
            future.cancel(true);
        }
    }

    /**
     * Starts the TokenRefresher if not already started. Starts refreshing when token is expired. If no active
     * token is present, or if the available token is expired soon, this will also refresh immediately.
     */
    final synchronized void start() {
        logger.debug("Starting the proactive token refresher");
        String accessToken = credential.getAccessToken();
        long refreshDelay;
        if (accessToken == null || StringUtils.isEmpty(accessToken)) {
            credential.refreshToken();
        }

        refreshDelay = credential.getExpireIn();

        scheduleRefresh(refreshDelay);
    }

    final synchronized void stop() {
        cancelPrevious();
        logger.debug("Stopped the proactive token refresher");
    }

}
