package org.zywx.wbpalmstar.widgetone.pushlibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.xiaomi.mipush.sdk.MiPushClient;
import org.zywx.wbpalmstar.widgetone.pushlibrary.reciveInterface.AppCanReciveInterface;
import org.zywx.wbpalmstar.widgetone.pushlibrary.utils.MIUIUtils;


/**
 * Created by 84309 on 2018/7/27.
 */

public class AppCanPush {
    private static AppCanReciveInterface mAppCanReciveInterface;
    private static final String APP_CAN_XIAOMI = "xiami";
    private static final String APP_CAN_HUAWEI = "huawei";
    private static final String APP_CAN_MEIZU = "meizu";
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpush.MESSAGE_RECEIVED_ACTION";
    private static MessageReceiver mMessageReceiver;

    //初始化推送
    public static void registerPush(Context context, String appId, String appKey) {
        //如果是小米推送
        if (MIUIUtils.isMIUI()) {
            MiPushClient.registerPush(context, appId, appKey);
            registerMessageReceiver(context);

        }
        //TODO 去封装对应的sdk 初始化
    }
    //设置别名
    public static void setAlias(Context context, String alias) {
        //如果是小米推送
        if (MIUIUtils.isMIUI()) {
            MiPushClient.setAlias(context, alias, null);

        }



        //TODO 去封装对应的sdk 设置别名
    }



    /**
     * 注册广播
     */
    public static void registerMessageReceiver(Context context) {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver, filter);
    }



    //根据类型去回调使用者信息
    public static void setAppCanonReciver(AppCanReciveInterface appCanReciveInterface) {
        mAppCanReciveInterface = appCanReciveInterface;

    }


    /**
     * 广播回调 处理返回的内容
     */
    public static class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra("message");
                    mAppCanReciveInterface.onRecive(context,messge,APP_CAN_XIAOMI);
                }
            } catch (Exception e) {
            }
        }
    }

}
