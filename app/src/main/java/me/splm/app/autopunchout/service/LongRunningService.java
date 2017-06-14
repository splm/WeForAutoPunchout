package me.splm.app.autopunchout.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import me.splm.app.autopunchout.broadcast.StartPunchoutServiceBroadcast;

public class LongRunningService extends Service {
    private static final String TAG="*****************";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: 轮询服务启动");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = 6 * 1000*10;
        long triggerTime = SystemClock.elapsedRealtime();
        Intent intent2 = new Intent(this, StartPunchoutServiceBroadcast.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent2, 0);
//        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerTime,time, pi);
        manager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),time,pi);
        Log.e(TAG, "onStartCommand: onStartCommand执行");
        return super.onStartCommand(intent, flags, startId);
    }
}
