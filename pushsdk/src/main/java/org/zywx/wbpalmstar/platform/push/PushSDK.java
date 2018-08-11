package org.zywx.wbpalmstar.platform.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import org.zywx.wbpalmstar.platform.push.report.PushReportAgent;
import org.zywx.wbpalmstar.platform.push.report.PushReportConstants;
import org.zywx.wbpalmstar.platform.push.report.PushReportUtility;
import org.zywx.wbpalmstar.platform.push.vo.PushHostVO;

import static android.content.Context.NOTIFICATION_SERVICE;

public class PushSDK {
    private static ReceiveDataInterface mReceiveDataInterface;
    private  static MessageReceiver messageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpush.MESSAGE_RECEIVED_ACTION";
    /**
     * 初始化推送
     * @param appId         config.xml中配置的appId
     * @param appKey        应用的唯一标示
     * @param bindUserHost  绑定解绑的host地址
     * @param pushHost      链接推送服务器的host地址
     * @param context
     */
    public static void initPush(String appId, String appKey, String bindUserHost, String pushHost, Context context) {
        PushReportUtility.setPushAndBindUserHost(context, bindUserHost, pushHost);
        PushReportAgent pushReportAgent = PushReportAgent.getInstance();
        pushReportAgent.initPush(appId, appKey, context);
        registerMessageReceiver(context);
    }

    /**
     * 设置绑定解绑的host地址和链接推送服务器的host地址
     */
    public static PushHostVO setPushHost(Context context, String host) {
        PushHostVO pushHostVO = PushReportAgent.setPushHost(context, host);
        return pushHostVO;
    }

    /**
     * 获取绑定解绑的host地址和链接推送服务器的host地址
     */
    public static PushHostVO getPushHost(Context context) {
        PushHostVO pushHostVO = PushReportAgent.getPushHost(context);
        return pushHostVO;
    }

    /**
     * 推送3.0版本绑定用户
     * @param userId     用户ID
     * @param userName   用户昵称
     */
    public static void bindUser(Context context, String userId, String userName) {
        PushReportAgent.bindUser(context, userId, userName);
    }

    /**
     * 推送3.0版本解绑用户
     */
    public static void unBindUser(Context context) {
        PushReportAgent.unBindUser(context);
    }

    /**
     * 推送4.0版本设备绑定
     * @param userId     用户ID
     * @param userName   用户昵称
     */
    public static void deviceBind(Context context, String userId, String userName) {
        PushReportAgent.deviceBind(context, userId, userName);
    }

    /**
     * 推送4.0版本设备解绑
     */
    public static void deviceUnBind(Context context) {
        PushReportAgent.deviceUnBind(context);
    }

    /**
     * 设置推送开关状态
     * @param type   1 开 0 关
     */
    public static void setPushState(Context context, int type) {
        PushReportAgent.setPushState(context, type);
    }

    /**
     * 获取推送开关状态
     * @return type: 1 开 0 关
     */
    public static String getPushState(Context context) {
        return PushReportAgent.getPushState(context);
    }

    /**
     * 推送消息打开上报
     * @param pushInfo       消息内容
     * @param tenantAccount  租户标示
     */
    public static void reportPush(Context context, String pushInfo, String tenantAccount) {
        PushReportUtility.setTenantAccount(context, tenantAccount);
        PushReportAgent.reportPush(context, pushInfo, PushReportConstants.EVENT_TYPE_OPEN);
    }


    public static void setPushInterface(ReceiveDataInterface receiveDataInterface){
        mReceiveDataInterface=receiveDataInterface;
    }

    public  void setStringData(Context context){

        NotificationManager manager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        //创建通知建设类
        Notification.Builder builder = new Notification.Builder(context);
        //设置跳转的页面
        PendingIntent intent = PendingIntent.getActivity(context,
                100, new Intent(),
                PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setSmallIcon(R.drawable.mipush_logo);
        //设置通知栏标题
        builder.setContentTitle("haha");
        //设置通知栏内容
        builder.setContentText("hahah");
        //设置跳转
        builder.setContentIntent(intent);
        //设置图标
        //设置
        builder.setDefaults(Notification.DEFAULT_ALL);
        //创建通知类
        Notification notification = builder.build();
        //显示在通知栏
        manager.notify(0, notification);

    }

    /**
     * 注册广播
     */
    public static void registerMessageReceiver(Context context) {
        messageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        context.registerReceiver(messageReceiver, filter);
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
                     mReceiveDataInterface.getMessage(messge,"");
                }
            } catch (Exception e) {
            }
        }
    }





}