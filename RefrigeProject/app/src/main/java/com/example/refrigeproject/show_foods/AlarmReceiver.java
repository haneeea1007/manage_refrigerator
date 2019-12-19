package com.example.refrigeproject.show_foods;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.refrigeproject.KakaoLoginActivity;
import com.example.refrigeproject.MainActivity;
import com.example.refrigeproject.R;

import androidx.core.app.NotificationCompat;

import static android.app.Notification.VISIBILITY_PUBLIC;

public class AlarmReceiver extends BroadcastReceiver {

    private int alarmID;
    private int dateSetting;
    private Boolean switchSetting;
    private String foodName;

    //    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onReceive(Context context, Intent intent) {

        // 메인에서 인텐트로 받아오기
        foodName = intent.getStringExtra("foodName");
        dateSetting = intent.getIntExtra("dateSetting", 0);
        alarmID = intent.getIntExtra("id", 0);
        switchSetting = intent.getBooleanExtra("switchSetting", false);

        // 서비스로 넘기는 인텐트 (알림을 클릭하면 메인창이 뜨도록 함)
        Intent rIntent = new Intent(context, AlarmService.class);
        PendingIntent pend = PendingIntent.getActivity(context, alarmID, new Intent(context, KakaoLoginActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        // 알람 설정이 켜져 있을 때
        if (switchSetting == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(rIntent);
                //NotificationManager 안드로이드 상태바에 메세지를 던지기위한 서비스 불러오고
                NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                Notification.Builder builder = new Notification.Builder(context);
                builder.setSmallIcon(R.drawable.ic_launcher_background)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("소비 만료기한 알림")
                        .setContentText("'" + foodName + "'" + " 소비 만료기한이 " + dateSetting + " 일 남았습니다.")
                        .setChannelId("Alarm")
                        .setCategory(NotificationCompat.CATEGORY_REMINDER)
                        .setFullScreenIntent(pend, true)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setContentIntent(pend)
//                        .setContentIntent(activityPending)
                        .setVisibility(VISIBILITY_PUBLIC)
                        .setAutoCancel(true);
                WakeLocker.acquire(context);
                notificationmanager.notify(alarmID, builder.build());

            } else {
                context.startService(rIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("소비 만료기한 알림")
                                .setContentText("'" + foodName + "'" + " 소비 만료기한이 " + dateSetting + " 일 남았습니다.")
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                                .setFullScreenIntent(pend, true)
                                .setContentIntent(pend)
//                                .setContentIntent(activityPending)
                                .setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                WakeLocker.acquire(context);
                mNotificationManager.notify(alarmID, mBuilder.build());
            }
            Log.d("리시버", "on");
            WakeLocker.release();

            // 알람 설정이 꺼져 있을 때
        } else {
            Log.d("리시버", "off");
            context.stopService(intent);
        }
    }
}
