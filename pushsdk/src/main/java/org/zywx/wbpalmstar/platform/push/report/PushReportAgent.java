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

package org.zywx.wbpalmstar.platform.push.report;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;
import org.zywx.wbpalmstar.platform.push.PushService;
import org.zywx.wbpalmstar.platform.push.vo.NameValuePairVO;
import org.zywx.wbpalmstar.platform.push.vo.PushDeviceBindUserVO;
import org.zywx.wbpalmstar.platform.push.vo.PushDeviceBindVO;
import org.zywx.wbpalmstar.platform.push.vo.PushHostVO;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class PushReportAgent implements PushReportConstants {
    private static PushReportAgent sAgent = null;

    public static PushReportAgent getInstance() {
        if (sAgent == null) {
            sAgent = new PushReportAgent();
        }
        return sAgent;
    }

    /**
     * 主应用启动时初始化推送
     */
    public void initPush(String appId, String appKey, Context context) {
        PushReportUtility.saveAppIDAndKey(context, appId, appKey);
        PushReportUtility.getSoftToken(context);// 初始化将softToken保存在sp中
        PushReportThread.getPushThread(context, this, TYPE_INIT_PUSH).start();
    }

    /**
     * 推送消息相关上报
     *
     * @param pushInfo  消息内容
     * @param eventType 时间类型，open 和 arrived
     */
    public static void reportPush(Context context, String pushInfo, String eventType) {
        PushReportUtility.log("reportPush===" + pushInfo + " eventType===" + eventType);
        String softToken = PushReportUtility.getSoftToken(context);
        SharedPreferences sp = context.getSharedPreferences(
                PushReportConstants.PUSH_DATA_SHAREPRE, Context.MODE_PRIVATE);
        String taskId = sp.getString(
                PushReportConstants.PUSH_DATA_SHAREPRE_TASKID, "");
        PushReportUtility.log("reportPush===taskId " + taskId);
        Editor editor = sp.edit();
        editor.putString(PushReportConstants.PUSH_DATA_SHAREPRE_DATA, "");
        editor.putString(PushReportConstants.PUSH_DATA_SHAREPRE_MESSAGE, "");
        editor.commit();
        if (TextUtils.isEmpty(taskId)) {
            String msgId = parsePushInfo2MsgId(pushInfo);
            if (msgId != null) {
                List<NameValuePairVO> nameValuePairs = new ArrayList<NameValuePairVO>();
                nameValuePairs
                        .add(new NameValuePairVO(KEY_PUSH_REPORT_MSGID, msgId));
                nameValuePairs.add(new NameValuePairVO(KEY_PUSH_REPORT_SOFTTOKEN,
                        softToken));
                nameValuePairs.add(new NameValuePairVO(KEY_PUSH_REPORT_EVENTTYPE,
                        eventType));
                nameValuePairs.add(new NameValuePairVO(KEY_PUSH_REPORT_OCCUREDAT,
                        System.currentTimeMillis() + ""));
                if (eventType.equals(PushReportConstants.EVENT_TYPE_OPEN)) {
                    PushReportThread.getPushReportThread(context, sAgent,
                            TYPE_PUSH_REPORT_OPEN, nameValuePairs).start();
                    Log.i("push", "EVENT_TYPE_OPEN");
                } else if (eventType.equals(PushReportConstants.EVENT_TYPE_ARRIVED)) {
                    PushReportThread.getPushReportThread(context, sAgent,
                            TYPE_PUSH_REPORT_ARRIVED, nameValuePairs).start();
                    Log.i("push", "EVENT_TYPE_ARRIVED");
                }
            }
        } else {
            String tenantId = sp.getString(
                    PushReportConstants.PUSH_DATA_SHAREPRE_TENANTID, "");
            PushReportUtility.log("reportPush===tenantId " + tenantId);
            editor.putString(PushReportConstants.PUSH_DATA_SHAREPRE_TASKID, "");
            editor.putString(PushReportConstants.PUSH_DATA_SHAREPRE_TENANTID, "");
            editor.commit();
            PushReportThread.getNewPushReportOpen(context,
                    TYPE_NEW_PUSH_REPORT_OPEN, taskId, tenantId, softToken).start();
            Log.i("push", "TYPE_NEW_PUSH_REPORT_OPEN");
        }
    }

    /**
     * 解析推送消息中的msgId
     *
     * @param pushInfo
     * @return
     */
    private static String parsePushInfo2MsgId(String pushInfo) {
        String msgId = null;
        try {
            JSONObject json = new JSONObject(pushInfo);
            msgId = json.getString(KEY_PUSH_REPORT_MSGID);
        } catch (Exception e) {
            PushReportUtility.oe("parsePushInfo2MsgId", e);
        }
        return msgId;
    }

    /**
     * 推送开关
     *
     * @param type 1 开 0 关
     */
    public static void setPushState(Context context, int type) {
        SharedPreferences sp = context.getSharedPreferences(SP_PUSHSTATE,
                Context.MODE_MULTI_PROCESS);
        Editor editor = sp.edit();
        editor.putString(SP_PUSHSTATE_KEY_LOCALSTATE, String.valueOf(type)).commit();
        PushReportUtility.log("setPushState type " + type);
        if (type == 1) {
            Intent myIntent = new Intent(context, PushService.class);
            myIntent.putExtra("type", type);
            context.startService(myIntent);
        } else {
            Intent myIntent = new Intent(context, PushService.class);
            myIntent.putExtra("type", type);
            context.startService(myIntent);
        }
    }

    /**
     * 获取推送开关
     */
    public static String getPushState(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_PUSHSTATE,
                Context.MODE_MULTI_PROCESS);
        String localPushMes = sp.getString(SP_PUSHSTATE_KEY_LOCALSTATE, "0");
        return localPushMes;
    }

    /**
     * 推送绑定用户接口
     */
    @SuppressWarnings("rawtypes")
    public static void bindUser(Context context, String userId, String userName) {
        String appId = PushReportUtility.getAppID(context);
        String softToken = PushReportUtility.getSoftToken(context);
        List<NameValuePairVO> nameValuePairs = new ArrayList<NameValuePairVO>();
        nameValuePairs.add(new NameValuePairVO("userId", userId));
        nameValuePairs.add(new NameValuePairVO("userNick", userName));
        nameValuePairs.add(new NameValuePairVO("appId", appId));
        nameValuePairs.add(new NameValuePairVO("platform", "1"));
        nameValuePairs.add(new NameValuePairVO("pushType", "mqtt"));
        nameValuePairs.add(new NameValuePairVO("softToken", softToken));
        nameValuePairs.add(new NameValuePairVO("deviceToken", softToken));
        PushReportThread.getPushBindUserThread(context, sAgent,
                TYPE_PUSH_BINDUSER, nameValuePairs).start();
    }

    public static void unBindUser(Context context) {
        List<NameValuePairVO> nameValuePairs = new ArrayList<NameValuePairVO>();
        PushReportThread.getPushBindUserThread(context, sAgent,
                TYPE_PUSH_UNBINDUSER, nameValuePairs).start();
    }

    public static void deviceBind(Context context, String userId, String userName) {
        try {


            String softToken = PushReportUtility.getSoftToken(context);
            PushDeviceBindVO pushDeviceBind = new PushDeviceBindVO();
            pushDeviceBind.setDeviceName(Build.MODEL);
            pushDeviceBind.setDeviceVersion(Build.VERSION.RELEASE);
            pushDeviceBind.setDeviceType("android");
            pushDeviceBind.setValid(true);
            pushDeviceBind.setTimeZone(TimeZone.getDefault().getDisplayName());
            pushDeviceBind.setDeviceToken(softToken);
            pushDeviceBind.setSoftToken(softToken);
            pushDeviceBind.setDeviceOwner("");
            pushDeviceBind.setTags("");
            pushDeviceBind.setBrand(Build.BRAND);
            pushDeviceBind.setFeacture(Build.MANUFACTURER);
            String mAppId = PushReportUtility.getAppID(context);
            String macAddress = PushReportUtility.getMacAddress(context).replaceAll(":", "");
            String clientId = macAddress + mAppId;
            if (clientId.length()>22){
                clientId=clientId.substring(0,22);
            }
            pushDeviceBind.setClientId(clientId);
            pushDeviceBind.setChannelId("");
            pushDeviceBind.setVersionId("");
            pushDeviceBind.setOrg("");
            pushDeviceBind.setTenantMark(PushReportUtility.getTenantAccount(context));
            pushDeviceBind.setFrom("Android-Engine");
            if (TextUtils.isEmpty(userId) && TextUtils.isEmpty(userName)) {
                pushDeviceBind.setRequestType("startUp");
            } else {
                pushDeviceBind.setRequestType("bindUser");
            }

            PushDeviceBindUserVO pushDeviceBindUser = new PushDeviceBindUserVO();
            pushDeviceBindUser.setUserId(userId);
            pushDeviceBindUser.setUsername(userName);
            pushDeviceBindUser.setTags("");
            pushDeviceBindUser.setSessionStatus("");
            pushDeviceBind.setUser(pushDeviceBindUser);
            PushReportThread.getDeviceBindThread(context, TYPE_PUSH_DEVICEBIND, pushDeviceBind).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deviceUnBind(Context context) {
        try {
            String softToken = PushReportUtility.getSoftToken(context);
            PushDeviceBindVO pushDeviceBind = new PushDeviceBindVO();
            pushDeviceBind.setSoftToken(softToken);
            pushDeviceBind.setDeviceToken(softToken);
            pushDeviceBind.setFrom("Android-Engine");
            pushDeviceBind.setRequestType("unbind");
            pushDeviceBind.setTenantMark(PushReportUtility.getTenantAccount(context));
            PushDeviceBindUserVO pushDeviceBindUser = new PushDeviceBindUserVO();
            pushDeviceBind.setUser(pushDeviceBindUser);
            PushReportThread.getDeviceBindThread(context, TYPE_PUSH_DEVICEUNBIND, pushDeviceBind).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PushHostVO setPushHost(Context context, String host) {
        PushHostVO pushHostVO = DataHelper.gson.fromJson(host, PushHostVO.class);
        String bindUserHost = pushHostVO.getBindUserHost();
        String pushHost = pushHostVO.getPushHost();
        if (TextUtils.isEmpty(bindUserHost) && TextUtils.isEmpty(pushHost)) {
            pushHostVO.setStatus(1);
        } else {
            PushReportUtility.setPushAndBindUserHost(context, bindUserHost, pushHost);
            pushHostVO.setStatus(0);
        }
        return pushHostVO;
    }

    public static PushHostVO getPushHost(Context context) {
        PushHostVO pushHostVO = new PushHostVO();
        pushHostVO.setBindUserHost(PushReportUtility.getBindUserHost(context));
        pushHostVO.setPushHost(PushReportUtility.getPushHost(context));
        return pushHostVO;
    }
}
