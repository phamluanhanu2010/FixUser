package com.strategy.intecom.vtc.fixuser.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gms.gcm.GcmListenerService;
import com.strategy.intecom.vtc.fixuser.MainScreen;
import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.dialog.DlRatingRepairer;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNoti;
import com.strategy.intecom.vtc.fixuser.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixuser.utils.ParserJson;
import com.strategy.intecom.vtc.fixuser.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;
import com.strategy.intecom.vtc.fixuser.view.base.AppCoreBase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr. Ha on 5/17/16.
 */
public class GcmListenerServices extends GcmListenerService {


    public final static int NOTI_AGENCY_ACCEPT = 2001;               // Thông báo khi có Agency đã nhận việc
    public final static int NOTI_AGENCY_CANCEL_JOB = 3003;           // Nhận được khi Agency hủy 1 công việc đã nhận
    public final static int NOTI_CHANGE_STATUS_COMING = 3004;       // Khi Agency thay đổi trạng thái Comming
    public final static int NOTI_CHANGE_STATUS_START_JOB = 3005;    // Khi Agency thay đổi trạng thái start job
    public final static int NOTI_CHANGE_STATUS_FINISH = 3006;       // Khi Agency thay đổi trạng thái hoàn thành
    public final static int NOTI_REMIND_AGENCY_NOT_COME = 2003;       // Khi Agency đã nhận việc nhưng còn 30 phút mà vẫn chưa đến
    public final static int NOTI_REMIND_ORDER_EXPIRED = 2004;       // Khi việc đã quá giờ mà thợ chưa đến
    public final static int NOTI_NO_AGENCY_ACCEPTED = 2005;       // Khi không có thợ nhận việc


    public GcmListenerServices(){

    }

    @Override
    public void onMessageReceived(String from, Bundle data) {

        String notification_type = data.getString("notification_type");
        String title = data.getString("gcm.notification.title");
        String body = data.getString("gcm.notification.body");
        String responseData = data.getString("responseData");
        String badge = data.getString("gcm.notification.badge");

        AppCoreBase.showLog(" onMessageReceived type -------- : " + "" + " -----from--- : " + from + ":--body---:" + body + ":--title---:" + title + " --------- : " + responseData);


        wakeUpScreenDevice();

        String sID = "";
        int type = 0;

        if (notification_type != null && !notification_type.isEmpty()) {

            try {

                type = Integer.parseInt(notification_type);

            } catch (NumberFormatException e) {
                type = 0;
            }

            AppCoreBase.showLog(" onMessageReceived type -------- : " + type + " -----from--- : " + from + ":--body---:" + body + ":--title---:" + title + " --------- : " + responseData);

            try {
                JSONObject jsonObject = new JSONObject(responseData);

                JSONObject orderObj = jsonObject.optJSONObject(ParserJson.API_PARAMETER_RESSPONSE_DATA);

                if (orderObj == null) {
                    orderObj = jsonObject;
                }

                sID = orderObj.optString(ParserJson.API_PARAMETER_ID_);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            switch (type) {
                case NOTI_CHANGE_STATUS_FINISH:
                    SharedPreferences IShare = getApplicationContext().getSharedPreferences(getApplicationContext().getApplicationInfo().packageName, Context.MODE_PRIVATE);
                    IShare.edit().putString(PreferenceUtil.IS_KEEP_WORKING, responseData).commit();
                case NOTI_AGENCY_ACCEPT:
                case NOTI_AGENCY_CANCEL_JOB:
                case NOTI_CHANGE_STATUS_COMING:
                case NOTI_CHANGE_STATUS_START_JOB:
                case NOTI_REMIND_AGENCY_NOT_COME:
                case NOTI_REMIND_ORDER_EXPIRED:
                case NOTI_NO_AGENCY_ACCEPTED:
                    try {

                        VtcModelNoti vtcModelNoti = new VtcModelNoti();
                        vtcModelNoti.setType(type);
                        vtcModelNoti.setMessage(body);
                        vtcModelNoti.setTitle(title);
                        vtcModelNoti.setId_order(sID);
                        vtcModelNoti.setResponseData(responseData);
                        vtcModelNoti.setTypeGoto(1);

                        AppCoreBase.showLog("send noti");
                        AppCore.initReceived(1000001, vtcModelNoti);

                    } catch (ExceptionInInitializerError error) {
                        error.printStackTrace();
                    } catch (NoClassDefFoundError e) {
                        e.printStackTrace();
                    }
                    break;
            }
            initShowNotification(type, title, body, sID, responseData);
        }
    }

    /**
     *
     *  Show Big content Notification
     * @param msg -->> Message
     *
     **/
    public void initShowNotification(int type, String title, String msg, String idOrder, String responseData){

        Bitmap icon1 = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getApplicationContext()).setAutoCancel(true)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon1).setContentText(msg);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(msg);
        bigText.setBigContentTitle(title);
        bigText.setSummaryText("Por: " + msg);
        mBuilder.setStyle(bigText);
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(getApplicationContext(),
                MainScreen.class);
        resultIntent.putExtra("type", type);
        resultIntent.putExtra("message", msg);
        resultIntent.putExtra("title", title);
        resultIntent.putExtra("id_order", idOrder);
        resultIntent.putExtra("order", responseData);

        // The stack builder object will contain an artificial back
        // stack for
        // the
        // started Activity.
        // getApplicationContext() ensures that navigating backward from
        // the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder
                .create(getApplicationContext());

        // Adds the back stack for the Intent (but not the Intent
        // itself)
        stackBuilder.addParentStack(MainScreen.class);

        // Adds the Intent that starts the Activity to the top of the
        // stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
    }

    /**
     *
     * Call when received message priority
     *
     * Wake up screen display message
     *
     */
    private void wakeUpScreenDevice(){

        PowerManager.WakeLock screenLock = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();

        screenLock.release();
    }

    @Override
    public void onDeletedMessages() {
    }

    @Override
    public void onMessageSent(String msgId) {
        AppCoreBase.showLog(" onMessageSent -------- : " + msgId);
    }

    @Override
    public void onSendError(String msgId, String error) {
    }
}
