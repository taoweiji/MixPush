package com.mixpush.mi;

import android.content.Context;

import com.mixpush.core.MixPushPlatform;
import com.mixpush.core.RegisterType;
import com.mixpush.core.MixPushClient;
import com.mixpush.core.MixPushHandler;
import com.mixpush.core.MixPushMessage;
import com.xiaomi.mipush.sdk.*;

import java.util.Arrays;
import java.util.List;

public class MiPushMessageReceiver extends PushMessageReceiver {
    MixPushHandler handler = MixPushClient.getInstance().getHandler();
//    private String mRegId;

    //    private long mResultCode = -1;
//    private String mReason;
//    private String mCommand;
//    private String mMessage;
//    private String mTopic;
//    private String mAlias;
//    private String mUserAccount;
//    private String mStartTime;
//    private String mEndTime;
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        MixPushMessage pushMessage = new MixPushMessage();
        pushMessage.setPlatform(MiPushProvider.MI);
        pushMessage.setTitle(message.getTitle());
        pushMessage.setPayload(message.getContent());
        pushMessage.setDescription(message.getDescription());
        pushMessage.setPassThrough(true);
        handler.getPassThroughReceiver().onReceiveMessage(context, pushMessage);
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        MixPushMessage pushMessage = new MixPushMessage();
        pushMessage.setPlatform(MiPushProvider.MI);
        pushMessage.setTitle(message.getTitle());
        pushMessage.setDescription(message.getDescription());
        pushMessage.setPayload(message.getContent());
        handler.getPushReceiver().onNotificationMessageClicked(context, pushMessage);
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        MixPushMessage pushMessage = new MixPushMessage();
        pushMessage.setPlatform(MiPushProvider.MI);
        pushMessage.setTitle(message.getTitle());
        pushMessage.setDescription(message.getDescription());
        pushMessage.setPayload(message.getContent());
        handler.getPushReceiver().onNotificationMessageArrived(context, pushMessage);
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
//        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
//            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                PushPlatform pushPlatform = new PushPlatform(MiPushProvider.MI, cmdArg1);
//                handler.getPushReceiver().onRegisterNotificationSucceed(context, pushPlatform);
//            }
//        }
//        else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
//            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                mAlias = cmdArg1;
//                Log.e("mAlias",mAlias);
//            }
//        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
//            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                mAlias = cmdArg1;
//                Log.e("mAlias",mAlias);
//            }
//        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
//            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                mTopic = cmdArg1;
//            }
//        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
//            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                mTopic = cmdArg1;
//            }
//        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
//            if (message.getResultCode() == ErrorCode.SUCCESS) {
//                mStartTime = cmdArg1;
//                mEndTime = cmdArg2;
//            }
//        }
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                MixPushPlatform mixPushPlatform = new MixPushPlatform(MiPushProvider.MI, cmdArg1);

                if (MiPushProvider.registerType == RegisterType.all) {
                    handler.getPushReceiver().onRegisterSucceed(context, mixPushPlatform);
                    handler.getPassThroughReceiver().onRegisterSucceed(context, mixPushPlatform);
                } else if (MiPushProvider.registerType == RegisterType.notification) {
                    handler.getPushReceiver().onRegisterSucceed(context, mixPushPlatform);
                } else if (MiPushProvider.registerType == RegisterType.passThrough) {
                    handler.getPassThroughReceiver().onRegisterSucceed(context, mixPushPlatform);
                }
            }
        }
    }

    @Override
    public void onRequirePermissions(Context context, String[] strings) {
        super.onRequirePermissions(context, strings);
        handler.getLogger().log(MiPushProvider.MI, "onRequirePermissions 缺少权限: " + Arrays.toString(strings));
    }
}