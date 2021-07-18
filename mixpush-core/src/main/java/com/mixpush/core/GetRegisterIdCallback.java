package com.mixpush.core;

import androidx.annotation.Nullable;

public abstract class GetRegisterIdCallback {
    public abstract void callback(@Nullable MixPushPlatform platform);
}