/*
 *  Copyright (C) 2014 The AppCan Open Source Project.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.

 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.zywx.wbpalmstar.platform.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.zywx.wbpalmstar.platform.push.report.PushReportAgent;

import static android.content.Context.NOTIFICATION_SERVICE;

public class PushReceiver extends BroadcastReceiver {
    public static final String ACTION = "org.zywx.push.receive";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION.equals(intent.getAction())) {
            String s="aaa";
            Bundle bundle=intent.getExtras();
            PushDataInfo dataInfo = (PushDataInfo) bundle.getSerializable("pushDataInfo");
            Toast.makeText(context,"TaskId:"+dataInfo.getTaskId()+"AppId:"+dataInfo.getAppId()+"pushDataString:"+dataInfo.getPushDataString(),Toast.LENGTH_SHORT).show();
            Log.e("GYB",s);
            setTitle(dataInfo.getTitle(),dataInfo.getPushDataString(),context);
        }
    }

    private void setTitle(String title,String data,Context context){
        NotificationManager manager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        //创建通知建设类
        Notification.Builder builder = new Notification.Builder(context);
        //设置跳转的页面
        PendingIntent intent = PendingIntent.getActivity(context,
                100, new Intent(),
                PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setSmallIcon(R.drawable.mipush_logo);
        //设置通知栏标题
        builder.setContentTitle(title);
        //设置通知栏内容
        builder.setContentText(data);
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









}