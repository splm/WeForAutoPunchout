package me.splm.app.autopunchout.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;

import me.splm.app.autopunchout.broadcast.StartPunchoutServiceBroadcast;

public class LongRunningService extends Service {
    private static final String TAG="*****************";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    private class ServerHanlder extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    Log.e(TAG,"接收到客户端传递的消息");
                    break;
            }
        }
    }

    private Handler mServerHandler=new ServerHanlder();

    private Messenger mMessenger = new Messenger(mServerHandler);
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: 轮询服务启动");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long time = 6 * 1000*10;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
//        time=calendar.getTimeInMillis();
        long triggerTime = SystemClock.elapsedRealtime();
        Intent intent2 = new Intent(this, StartPunchoutServiceBroadcast.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent2, 0);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerTime,time, pi);
        Log.e(TAG, "onStartCommand: onStartCommand执行");
        return super.onStartCommand(intent, flags, startId);
    }


}
