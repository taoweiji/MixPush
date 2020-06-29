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
package com.huawei.push.android;

import com.alibaba.fastjson.annotation.JSONField;
import com.huawei.push.util.ValidatorUtils;

public class LightSettings {
    private static final String LIGTH_DURATION_PATTERN = "\\d+|\\d+[sS]|\\d+.\\d{1,9}|\\d+.\\d{1,9}[sS]";

    @JSONField(name = "color")
    private Color color;

    @JSONField(name = "light_on_duration")
    private String lightOnDuration;

    @JSONField(name = "light_off_duration")
    private String lightOffDuration;

    public LightSettings(Builder builder) {
        this.color = builder.color;
        this.lightOnDuration = builder.lightOnDuration;
        this.lightOffDuration = builder.lightOffDuration;
    }

    public Color getColor() {
        return color;
    }

    public String getLightOnDuration() {
        return lightOnDuration;
    }

    public String getLightOffDuration() {
        return lightOffDuration;
    }

    /**
     * 参数校验
     */
    public void check() {
        ValidatorUtils.checkArgument(this.color != null, "color must be selected when light_settings is set");

        if (this.color != null) {
            this.color.check();
        }

        ValidatorUtils.checkArgument(this.lightOnDuration != null, "light_on_duration must be selected when light_settings is set");

        ValidatorUtils.checkArgument(this.lightOffDuration != null, "light_off_duration must be selected when light_settings is set");

        ValidatorUtils.checkArgument(this.lightOnDuration.matches(LIGTH_DURATION_PATTERN), "light_on_duration format is wrong");

        ValidatorUtils.checkArgument(this.lightOffDuration.matches(LIGTH_DURATION_PATTERN), "light_off_duration format is wrong");
    }

    /**
     * builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Color color;
        private String lightOnDuration;
        private String lightOffDuration;

        public Builder setColor(Color color) {
            this.color = color;
            return this;
        }

        public Builder setLightOnDuration(String lightOnDuration) {
            this.lightOnDuration = lightOnDuration;
            return this;
        }

        public Builder setLightOffDuration(String lightOffDuration) {
            this.lightOffDuration = lightOffDuration;
            return this;
        }

        public LightSettings build() {
            return new LightSettings(this);
        }
    }
}
