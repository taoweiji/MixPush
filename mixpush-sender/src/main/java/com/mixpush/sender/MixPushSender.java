package com.mixpush.sender;

import com.mixpush.sender.provider.HuaweiPushProvider;
import com.mixpush.sender.provider.MeizuPushProvider;
import com.mixpush.sender.provider.MiAPNsPushProvider;
import com.mixpush.sender.provider.MiPushProvider;
import com.mixpush.sender.provider.OppoPushProvider;
import com.mixpush.sender.provider.VivoPushProvider;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 混合推送
 */
public class MixPushSender {
    // setNetworkType
    // setOffLine
    // setShowStartTime


    public static final String MI = "mi";

    public static final String VIVO = "vivo";

    private String miAppSecretKey = "";
    private String packageName = "";
    private String meizuAppId;
    private String meizuAppSecretKey;
    private String huaweiAppId;
    private String huaweiAppSecret;
    private String oppoAppKey;
    private String oppoMasterSecret;
    private String vivoAppId;
    private String vivoAppKey;
    private String vivoAppSecret;

    private boolean usePassThrough = false;
    private boolean interceptTestData = true;

    private Map<String, MixPushProvider> providerMap = new HashMap<>();
    private FileOutputStream apnsCertificate;
    private String apnsPassword;
    private boolean test;
    private String miAPNsAppSecretKey;

    private MixPushSender() {

    }

    private void register() {
        if (miAppSecretKey != null) {
            addProvider(new MiPushProvider(packageName, miAppSecretKey, usePassThrough,test));
        }
        if (miAPNsAppSecretKey != null) {
            addProvider(new MiAPNsPushProvider(miAPNsAppSecretKey, test));
        }
        if (meizuAppId != null) {
            addProvider(new MeizuPushProvider(meizuAppId, meizuAppSecretKey));
        }
        if (huaweiAppId != null) {
            addProvider(new HuaweiPushProvider(huaweiAppId, huaweiAppSecret));
        }
        if (oppoAppKey != null) {
            addProvider(new OppoPushProvider(oppoAppKey, oppoMasterSecret));
        }
        if (vivoAppId != null) {
            addProvider(new VivoPushProvider(vivoAppId, vivoAppKey, vivoAppSecret, test));
        }
//        if (apnsCertificate != null) {
//            addProvider(new ApnsPushProvider(apnsCertificate, apnsPassword));
//        }
    }

    private void addProvider(MixPushProvider provider) {
        providerMap.put(provider.platformName(), provider);
    }

    private MixPushProvider getProvider(String platformName) {
        return providerMap.get(platformName);
    }

    /**
     * 检查是否可能是测试消息,测试消息无法广播给所有用户
     *
     * @return 是否可能是测试
     */
    private boolean checkMaybeTest(MixPushMessage message) {
        if (test) {
            return true;
        }
        if (!interceptTestData) {
            return false;
        }
        // TODO 标题纯数字,带有test,无空格英文连续
        String title = message.getTitle();
        if (title.matches("[a-zA-Z]+")) {
            return true;
        }
        if (title.matches("[0-9]+")) {
            return true;
        }
        return title.contains("test");
    }

    public MixPushResult sendMessage(MixPushMessage message, MixPushTarget target) {
        if (target.broadcastAll) {
            if (test) {
                return new MixPushResult.Builder()
                        .message(message)
                        .platformName(null)
                        .statusCode(MixPushResult.TEST_ENV_CAN_NOT_BROADCAST)
                        .reason("测试环境,无法执行全局推送")
                        .build();
            }
            if (checkMaybeTest(message)) {
                return new MixPushResult.Builder()
                        .message(message)
                        .platformName(null)
                        .statusCode(MixPushResult.TEST_DATA_CAN_NOT_BROADCAST)
                        .reason("请检查是否是测试数据,测试数据无法推送给超过10人")
                        .build();
            }
            List<MixPushResult> results = new ArrayList<>();
            Collection<MixPushProvider> providers = providerMap.values();
            for (MixPushProvider provider : providers) {
                // 通知栏推送需要检查是否支持全局推送
                if (message.isPassThrough() && !provider.isSupportPassThrough()) {
                    continue;
                }
                if (provider.isSupportBroadcastAll(message.isPassThrough())) {
                    results.add(provider.broadcastMessageToAll(message));
                } else {
                    results.add(new MixPushResult.Builder()
                            .message(message)
                            .platformName(provider.platformName())
                            .statusCode(MixPushResult.NOT_SUPPORT_BROADCAST)
                            .reason(provider.platformName() + " 不支持全局推送")
                            .succeed(true)
                            .build());
                }
            }
            if (results.isEmpty()) {
                return new MixPushResult.Builder()
                        .message(message)
                        .statusCode(MixPushResult.NOT_SUPPORT_BROADCAST)
                        .reason("不支持全局推送")
                        .succeed(true)
                        .build();
            }
            return MixPushResult.build(results);
        }
        if (target.platforms.isEmpty()) {
            return new MixPushResult.Builder().reason("没有填写regId").build();
        }
        if (target.platforms.size() >= 10 && checkMaybeTest(message)) {
            return new MixPushResult.Builder()
                    .message(message)
                    .platformName(null)
                    .statusCode(MixPushResult.TEST_DATA_CAN_NOT_BROADCAST)
                    .reason("请检查是否是测试数据,测试数据无法推送给超过10人")
                    .build();
        }
        Map<String, List<String>> map = new HashMap<>();
        for (MixPushPlatform item : target.platforms) {
            List<String> regIds;
            if (map.containsKey(item.platformName)) {
                regIds = map.get(item.platformName);
            } else {
                regIds = new ArrayList<>();
                map.put(item.platformName, regIds);
            }
            regIds.add(item.regId);
        }
        List<MixPushResult> results = new ArrayList<>();
        Set<String> keys = map.keySet();
        for (String platformName : keys) {
            MixPushProvider provider = getProvider(platformName);
            if (provider == null) {
                results.add(new MixPushResult.Builder()
                        .message(message)
                        .statusCode(MixPushResult.NOT_SUPPORT)
                        .platformName(platformName)
                        .reason("没有找到对应的注册平台 " + platformName)
                        .build());
            } else if (message.isPassThrough() && !provider.isSupportPassThrough()) {
                results.add(new MixPushResult.Builder()
                        .message(message)
                        .statusCode(MixPushResult.NOT_SUPPORT_PASS_THROUGH)
                        .platformName(platformName)
                        .reason("不支持透传 " + platformName)
                        .build());
            } else {
                results.add(provider.sendMessage(message, map.get(platformName)));
            }
        }
        return MixPushResult.build(results);
    }

    public static class Builder {
        private MixPushSender sender = new MixPushSender();

        /**
         * 如果使用了小米作为默认的透传,那么就不能使用 broadcastNotificationMessageToAll,
         * 因为如果把小米推送作为默认的透传,那么所有的平台都必须注册小米推送,一旦使用broadcastAll,会导致非小米手机重复收到推送
         */
        public Builder mi(String miAppSecretKey, boolean usePassThrough) {
            sender.miAppSecretKey = miAppSecretKey;
            sender.usePassThrough = usePassThrough;
            return this;
        }

        public Builder mi(String miAppSecretKey) {
            sender.miAppSecretKey = miAppSecretKey;
            sender.usePassThrough = false;
            return this;
        }

        public Builder oppo(String appKey, String masterSecret) {
            sender.oppoAppKey = appKey;
            sender.oppoMasterSecret = masterSecret;
            return this;
        }

        public Builder vivo(String appId, String appKey, String appSecret) {
            sender.vivoAppId = appId;
            sender.vivoAppKey = appKey;
            sender.vivoAppSecret = appSecret;
            return this;
        }

        public Builder huawei(String appId, String appSecret) {
            sender.huaweiAppId = appId;
            sender.huaweiAppSecret = appSecret;
            return this;
        }

        public Builder meizu(String appId, String appSecretKey) {
            sender.meizuAppId = appId;
            sender.meizuAppSecretKey = appSecretKey;
            return this;
        }

//        public Builder apns(FileOutputStream apnsCertificate, String password) {
//            sender.apnsCertificate = apnsCertificate;
//            sender.apnsPassword = password;
//            return this;
//        }


        public Builder packageName(String packageName) {
            sender.packageName = packageName;
            return this;
        }

        public MixPushSender build() {
            sender.register();
            return sender;
        }

        /**
         * 包含test/纯英文无空格/纯数字无空格
         *
         * @param interceptTestData 是否检查全局推送的头部是否是测试推送,如果是测试推荐就禁止发送
         */
        public Builder interceptTestData(boolean interceptTestData) {
            sender.interceptTestData = interceptTestData;
            return this;
        }

        public Builder test(boolean isTest) {
            sender.test = isTest;
            return this;
        }

        public Builder miAPNs(String miAPNsAppSecretKey) {
            sender.miAPNsAppSecretKey = miAPNsAppSecretKey;
            return this;
        }
    }
}
