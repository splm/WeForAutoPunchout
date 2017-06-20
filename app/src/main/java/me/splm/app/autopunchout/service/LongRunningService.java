package me.splm.app.autopunchout.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;

public class LongRunningService extends Service {
    private static final String TAG = "*****************";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    private class ServerHanlder extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.e(TAG, "接收到客户端传递的消息");
                    break;
            }
        }
    }

    private Handler mServerHandler = new ServerHanlder();

    private Messenger mMessenger = new Messenger(mServerHandler);

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: 轮询服务启动");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ServiceUtils.buildAlarmanger(this);
        Log.e(TAG, "onStartCommand: onStartCommand执行");
        return super.onStartCommand(intent, flags, startId);
    }


}
