package org.zywx.wbpalmstar.widgetone.pushlibrary;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.support.v4.content.LocalBroadcastManager;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;


/**
 * Created by 84309 on 2018/7/27.
 */


public class MiUiMessageReceiver extends PushMessageReceiver {
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        Intent msgIntent = new Intent("com.example.jpush.MESSAGE_RECEIVED_ACTION");
        msgIntent.putExtra("message", message.toString());
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        Intent msgIntent = new Intent("com.example.jpush.MESSAGE_RECEIVED_ACTION");
        msgIntent.putExtra("message", message.toString());
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {

        Intent msgIntent = new Intent("com.example.jpush.MESSAGE_RECEIVED_ACTION");
        msgIntent.putExtra("message", message.toString());
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        Intent msgIntent = new Intent("com.example.jpush.MESSAGE_RECEIVED_ACTION");
        msgIntent.putExtra("message", message.toString());
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        Intent msgIntent = new Intent("com.example.jpush.MESSAGE_RECEIVED_ACTION");
        msgIntent.putExtra("message", message.toString());
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
    }

    @Override
    public void onRequirePermissions(Context context, String[] permissions) {
        super.onRequirePermissions(context, permissions);

    }

}
