package com.mixpush.sender;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MixPushServerTest2 {

    MixPushSender sender = new MixPushSender.Builder()
            .packageName("")
            // 如果使用小米推送作为全局透传通道，小米推送将不支持全局推送,请使用分组推送给所有用户
            .mi("", false)
            .meizu("", "")
            .huawei("", "")
            .oppo("", "")
            .vivo("", "", "")
            .miAPNs("")
            // 默认开启,如果开启拦截测试数据,测试数据的消息推送发送给超过10人,
            // 就算是测试数据也建议使用正式一点的文案,比如"欢迎使用XXX",避免真的一不小心发送了
            .interceptTestData(true)
            // 如果开启测试环境,数据的消息推送发送给超过10人,建议测试环境不允许使用推送,避免出现严重错误
            .test(false)
            // 支持自定义
            .addProvider(null)
            .build();

    MixPushMessageConfig activitiesMessageConfig = new MixPushMessageConfig.Builder()
            // OPPO 必须在“通道配置 → 新建通道”模块中登记通道，再在发送消息时选择
            .oppoPushChannelId("activities")
            // 推广/运营活动等推广必须设置成false
            .vivoSystemMessage(true)
            // 小于72小时,单位毫秒
            .timeToLive(72 * 3600000)
            .build();
    MixPushMessageConfig imMessageConfig = new MixPushMessageConfig.Builder()
            // TODO mixpush初始化的时候创建
            .channelId("im")
            // 如果是订单变化,物流信息,im等请设置为true
            .vivoSystemMessage(true)
            .timeToLive(72 * 3600000)
            // 非必填,需要向小米申请特殊的渠道,如果没有可以不用填写 https://dev.mi.com/console/doc/detail?pId=2086#faq-permission
            .miPushChannelId("im")
            // 必填,OPPO 必须在“通道配置 → 新建通道”模块中登记通道，再在发送消息时选择
            .oppoPushChannelId("im")
            .huaweiPushChannelId("im")
            .build();


    MixPushMessage getAdMessage() {
        return new MixPushMessage.Builder()
                .title("订单支付成功通知")
                .description("报名成功！您已成功报名《我的课很贵的》，将在2020-09-11 19:45开课，建议提前5分钟进入课程直播间准备开练。太棒了，又多练了一次！")
                .payload("{\"messageId\":\"b575bdb37b6f4cf48e482be5940510fa\",\"url\":\"https://www.baidu.com\"}")
                .passThrough(false)
                .messageId(UUID.randomUUID().toString())
                .config(activitiesMessageConfig)
                .justOpenApp(false)
                .build();
    }

    private static final MixPushPlatform oppo_a9 = new MixPushPlatform("oppo", "OnePlus_CN_0a931b3d14592951a93a232d4e7996f6");
    private static final MixPushPlatform oneplus_6t = new MixPushPlatform("oppo", "OnePlus_CN_210acd676cd9e720f7c60fa6e70a25c3");
    private static final MixPushPlatform vivo_x27 = new MixPushPlatform("vivo", "15924658300146530281822");
    private static final MixPushPlatform vivo_x20a = new MixPushPlatform("vivo", "15933221621246530243564");
    private static final MixPushPlatform mi_m9_wiki = new MixPushPlatform("mi", "/WZ+BudUyTVO2ZuIWHufxgol2BH7O0fOFzjQ5W26UelRBCBAgsroKjmXSoEg3ScT");
    private static final MixPushPlatform meizu_16th = new MixPushPlatform("meizu", "V5R5b79477f06007e6b4f754d6d625f4f4b507e477c01");
    private static final MixPushPlatform huawei_al00 = new MixPushPlatform("huawei", "AIBafNxB7yHUuAFVReoJ0EwYPAT1FYCMLODSiS0CPjnb2L1tiiTwTv1qZ06aGf7CDaitb9O9EzWKwH_uiZlubzkpURJmrFd3hk7waI-xuWxbJt3MlhodfUIsYXm3D-1Q9w");


    private static final MixPushPlatform pass_oppo_a9 = new MixPushPlatform("mi", "MXsKjStfUqJBXtS2WfjENfuv1gRGlSWmWn5GVKrGxJBoLv9EikHpywE5jJzn4PAs");
    private static final MixPushPlatform pass_oneplus_6t = new MixPushPlatform("mi", "MXsKjStfUqJBXtS2WfjENfuv1gRGlSWmWn5GVKrGxJBoLv9EikHpywE5jJzn4PAs");
    private static final MixPushPlatform pass_vivo_x27 = new MixPushPlatform("mi", "jVELeZtwjS1RNFLyT/k5BR9tOghjqn7J3V/CTxj8WU8Tr2UEHvMPuyjRFboVZi7f");
    private static final MixPushPlatform pass_vivo_x20a = new MixPushPlatform("mi", "UodOLe0nnZKDCzV8hUF0WGoKx7ZbTFO1babgZr8EpVsP8/sLW7g6RKWURIipCrXA");
    private static final MixPushPlatform pass_huawei_al00 = new MixPushPlatform("mi", "0bu1sc1fZuGSQ3/ZmHM09vzEwEKZeb3e8CezH+PYHRrCr0U6E2qHTYbmKXrfSZ6/");

    @Test
    public void sendNotificationToSingle() {
//        MixPushTarget target = MixPushTarget.single("huawei", "0865925049412200300007043100CN01");
//        MixPushTarget target = MixPushTarget.single("oppo", "CN_0ffd6dabe9d8bae7770525817118d24f");
//        MixPushTarget mi = MixPushTarget.single("mi","/NcWw80tkAnWoNCQ9ZxSrt+B2ueraivFkCCAWyAunMb3d5xXn0zSNHzBSTToiux9");
        MixPushResult result = sender.sendMessage(getAdMessage(), MixPushTarget.values(huawei_al00));
        System.out.println(result.toString());
    }


    @Test
    public void sendNotificationToList() {
        // 如果用户3个月没有打开APP,建议不要再发送推送,因为通常regId失效时间是3个月
        List<MixPushPlatform> regIds = new ArrayList<>();
        regIds.add(oppo_a9);
        regIds.add(oneplus_6t);
        regIds.add(vivo_x27);
        regIds.add(vivo_x20a);
        regIds.add(mi_m9_wiki);
        regIds.add(meizu_16th);
        regIds.add(huawei_al00);
        MixPushTarget target = MixPushTarget.list(regIds);
//        MixPushResult result = sender.sendMessage(getAdMessage(), target);
//        System.out.println(result.toString());
    }

    @Test
    public void sendNotificationToAll() {
        // 这个方法不支持华为手机/苹果手机/oppo手机/如果usePassThrough,小米推送也不支持
//        MixPushResult result = sender.sendMessage(getAdMessage(), MixPushTarget.broadcastAll());
//        System.out.println(result.toString());
    }

    @Test
    public void sendPayloadToSingle() {

    }

    @Test
    public void sendPayloadToAll() {

    }


}