package com.zywork.zymobi;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.mipush.sdk.MiPushClient;

import org.zywx.wbpalmstar.platform.push.PushSDK;
import org.zywx.wbpalmstar.platform.push.ReceiveDataInterface;
import org.zywx.wbpalmstar.widgetone.pushlibrary.AppCanPush;
import org.zywx.wbpalmstar.widgetone.pushlibrary.reciveInterface.AppCanReciveInterface;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 1、本 demo 可以直接运行，设置 topic 和 alias。
 * 服务器端使用 appsecret 即可以向demo发送广播和单点的消息。<br/>
 * 2、为了修改本 demo 为使用你自己的 appid，你需要修改几个地方：DemoApplication.java 中的 APP_ID,
 * APP_KEY，AndroidManifest.xml 中的 packagename，和权限 permission.MIPUSH_RECEIVE 的前缀为你的 packagename。
 *
 * @author wangkuiwei
 */
public class MainActivity extends Activity  {
    public static List<String> logList = new CopyOnWriteArrayList<String>();
    // user your appid the key.
    private static final String APP_ID = "2882303761517837543";
    // user your appid the key.
    private static final String APP_KEY = "5121783771543";

    // 此TAG在adb logcat中检索自己所需要的信息， 只需在命令行终端输入 adb logcat | grep
    // com.xiaomi.mipushdemo
    public static final String TAG = "com.zywork.zymobi";

    private static DemoHandler sHandler = null;

    private TextView mLogView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (shouldInit()) {
            AppCanPush.registerPush(this,APP_ID, APP_KEY);
        }


        AppCanPush.setAppCanonReciver(new AppCanReciveInterface() {
            @Override
            public void onRecive(Context context, String message, String type) {
             String m=message;
             String t=type;
            }
        });


        String sn = MacUtils.getMobileMAC(MainActivity.this);
        final String newSn = sn.replace(":", "");
        TextView textView=findViewById(R.id.tv_text);
        textView.setText(newSn);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PushSDK.setPushState(MainActivity.this,1);
            }
        });

        final EditText ed_text_account=findViewById(R.id.ed_text_account);
        findViewById(R.id.btn_ed_text_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Toast.makeText(MainActivity.this,"hah",Toast.LENGTH_SHORT).show();

                PushSDK.deviceBind(MainActivity.this,"guiyanbing","桂雁彬");
            }
        });

        final EditText ed_text_subscribe=findViewById(R.id.ed_text_subscribe);
        findViewById(R.id.btn_ed_text_subscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PushSDK.initPush("1846:MOBILEOA","www","http://192.168.90.82:27012/access/","192.168.90.82:1883",MainActivity.this);

            }
        });

    }

    private void setTitle(String title,String data){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //创建通知建设类
        Notification.Builder builder = new Notification.Builder(MainActivity.this);
        //设置跳转的页面
        PendingIntent intent = PendingIntent.getActivity(MainActivity.this,
                100, new Intent(),
                PendingIntent.FLAG_CANCEL_CURRENT);

        //设置通知栏标题
        builder.setContentTitle(title);
        //设置通知栏内容
        builder.setContentText(data);
        //设置跳转
        builder.setContentIntent(intent);
        //设置图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //设置
        builder.setDefaults(Notification.DEFAULT_ALL);
        //创建通知类
        Notification notification = builder.build();
        //显示在通知栏
        manager.notify(0, notification);



    }
















    @Override
    protected void onResume() {
        super.onResume();
        refreshLogInfo();
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static void reInitPush(Context ctx) {
        AppCanPush.registerPush(ctx,APP_ID, APP_KEY);
    }
    public static DemoHandler getHandler() {
        return sHandler;
    }


    public  class DemoHandler extends Handler {

        private Context context;

        public DemoHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            String s = (String) msg.obj;
            refreshLogInfo();
            if (!TextUtils.isEmpty(s)) {
                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        DemoApplication.setMainActivity(null);
    }

    public void refreshLogInfo() {
        String AllLog = "";
        for (String log : logList) {
            AllLog = AllLog + log + "\n\n";
        }
    }
}
