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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import org.zywx.wbpalmstar.platform.push.mqttpush.MQTTService;
import org.zywx.wbpalmstar.platform.push.report.PushReportUtility;

/**
 * 为确保推送及时性，PushService 运行在单独进程中（在Manifest文件中配置），而非应用的进程，应注意数据的传递方式。
 */
public class PushService extends Service {

    private int type = 0;
    private Object pushGetData = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent) {
            type = intent.getIntExtra("type", type);
        }
        start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // restart Service when the Service is stopped by user.
        Intent localIntent = new Intent();
        localIntent.setClass(this, PushService.class);
        localIntent.putExtra("type", type);
        this.startService(localIntent);
        super.onDestroy();
    }

    private void start() {
        String softToken = PushReportUtility.getSoftToken(this);
        String url_push = PushReportUtility.getPushHost(this);
        if (TextUtils.isEmpty(url_push)) {
            Log.w("PushService", "push_host is empty");
            return;
        }
        PushReportUtility.log("start--" + type);
        try {
            if (type == 0) {
                if (pushGetData != null) {
                    ((MQTTService) pushGetData).onDestroy();
                    pushGetData = null;
                }
                return;
            }
            if (pushGetData == null) {
                pushGetData = new MQTTService(this, url_push, softToken);
                ((MQTTService) pushGetData).init();
            } else {
                Context ctx = getApplicationContext();
                Intent mQttPingIntent = new Intent(MQTTService.MQTT_PING_ACTION);
                mQttPingIntent.setPackage(ctx.getPackageName());
                ctx.sendBroadcast(mQttPingIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}