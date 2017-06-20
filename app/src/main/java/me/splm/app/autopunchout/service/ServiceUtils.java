package me.splm.app.autopunchout.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import java.util.Calendar;
import java.util.List;

import me.splm.app.autopunchout.broadcast.StartPunchoutServiceBroadcast;

import static android.content.Context.ALARM_SERVICE;


public class ServiceUtils {
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    public static AlarmManager buildAlarmanger(Context context) {
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        long time = 6 * 1000 * 10;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 11);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        time = calendar.getTimeInMillis();
        long triggerTime = SystemClock.elapsedRealtime();
//        long triggerTime = System.currentTimeMillis();
        Intent intent2 = new Intent(context, StartPunchoutServiceBroadcast.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent2, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
//        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime + time, pi);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            manager.setExact(AlarmManager.RTC_WAKEUP, time, pi);
//        } else {
//            manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), time, pi);
//        }
        return manager;
    }
}
