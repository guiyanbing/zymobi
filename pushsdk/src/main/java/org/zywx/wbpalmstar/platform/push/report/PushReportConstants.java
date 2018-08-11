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

public interface PushReportConstants {
    public static final String url_push_report = "report";
    public static final String url_push_bindUser = "msg/bindUser";
    public static final String url_push_bindDevice = "4.0/installations";

    public static final String KEY_BINDUSER_HOST = "bindUser_host";
    public static final String KEY_PUSH_HOST = "push_host";
    public static final String SP_PUSH_HOST = "spPushHost";
    public static final String SP_APP = "app";
    public static final String SP_APP_KEY_APPID = "appid";
    public static final String SP_APP_KEY_APPKEY = "appKey";
    public static final String SP_APP_KEY_SOFTTOKEN = "softToken";
    public static final String SP_APP_KEY_TENANTACCOUNT = "tenantAccount";
    public static final String SP_PUSHSTATE = "pushState";
    public static final String SP_PUSHSTATE_KEY_LOCALSTATE = "localState";

    public static final String EVENT_TYPE_OPEN = "open";
    public static final String EVENT_TYPE_ARRIVED = "arrived";

    public static final String KEY_PUSH_REPORT_MSGID = "msgId";
    public static final String KEY_PUSH_REPORT_SOFTTOKEN = "softToken";
    public static final String KEY_PUSH_REPORT_EVENTTYPE = "eventType";
    public static final String KEY_PUSH_REPORT_OCCUREDAT = "occuredAt";
    public static final String PUSH_DATA_SHAREPRE = "pushData_sharePre";
    public static final String PUSH_DATA_SHAREPRE_DATA = "pushData_data";
    public static final String PUSH_DATA_SHAREPRE_MESSAGE = "pushData_message";
    public static final String PUSH_DATA_SHAREPRE_TASKID = "pushData_taskId";
    public static final String PUSH_DATA_SHAREPRE_TENANTID = "pushData_tenantId";

    public static final String PUSH_DATA_INFO_KEY = "pushDataInfo";
    public static final String PUSH_DATA_JSON_KEY_APPID = "appId";
    public static final String PUSH_DATA_JSON_KEY_TASKID = "taskId";
    public static final String PUSH_DATA_JSON_KEY_TITLE = "title";
    public static final String PUSH_DATA_JSON_KEY_ALERT = "alert";
    public static final String PUSH_DATA_JSON_KEY_BADGE = "badge";
    public static final String PUSH_DATA_JSON_KEY_CONTENT_AVAILABLE = "content-available";
    public static final String PUSH_DATA_JSON_KEY_REMINDTYPE = "remindType";
    public static final String PUSH_DATA_JSON_KEY_STYLE = "style";
    public static final String PUSH_DATA_JSON_KEY_ICON = "icon";
    public static final String PUSH_DATA_JSON_KEY_RGB = "rgb";
    public static final String PUSH_DATA_JSON_KEY_BEHAVIOR = "behavior";
    public static final String PUSH_DATA_JSON_KEY_TENANTID = "tenantId";

    public final static int TYPE_INIT_PUSH = 0;
    public final static int TYPE_PUSH_BINDUSER = 1;
    public final static int TYPE_PUSH_REPORT_OPEN = 2;
    public final static int TYPE_PUSH_REPORT_ARRIVED = 3;
    public final static int TYPE_PUSH_UNBINDUSER = 4;
    public final static int TYPE_NEW_PUSH_REPORT_OPEN = 5;
    public final static int TYPE_PUSH_DEVICEBIND = 6;
    public final static int TYPE_PUSH_DEVICEUNBIND = 7;
}
