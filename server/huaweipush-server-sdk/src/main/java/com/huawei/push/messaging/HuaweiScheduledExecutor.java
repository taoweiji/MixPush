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

 *  2019.12.15-Changed constructor HuaweiScheduledExecutor
 *                  Huawei Technologies Co., Ltd.
 *
 */
package com.huawei.push.messaging;

import com.google.common.base.Strings;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A single-threaded scheduled executor implementation.
 */
public class HuaweiScheduledExecutor extends ScheduledThreadPoolExecutor {

    public HuaweiScheduledExecutor(ThreadFactory threadFactory, String name) {
        this(threadFactory, name, null);
    }

    public HuaweiScheduledExecutor(ThreadFactory threadFactory, String name, Thread.UncaughtExceptionHandler handler) {
        super(1, decorateThreadFactory(threadFactory, name, handler));
        setRemoveOnCancelPolicy(true);
    }

    static ThreadFactory getThreadFactoryWithName(ThreadFactory threadFactory, String name) {
        return decorateThreadFactory(threadFactory, name, null);
    }

    private static ThreadFactory decorateThreadFactory(
            ThreadFactory threadFactory, String name, Thread.UncaughtExceptionHandler handler) {
        checkArgument(!Strings.isNullOrEmpty(name));
        ThreadFactoryBuilder builder = new ThreadFactoryBuilder()
                .setThreadFactory(threadFactory)
                .setNameFormat(name)
                .setDaemon(true);
        if (handler != null) {
            builder.setUncaughtExceptionHandler(handler);
        }
        return builder.build();
    }
}
