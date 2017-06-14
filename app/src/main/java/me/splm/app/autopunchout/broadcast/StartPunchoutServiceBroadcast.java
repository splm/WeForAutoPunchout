package me.splm.app.autopunchout.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import me.splm.app.autopunchout.service.EnvelopeService;


public class StartPunchoutServiceBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("***************", "onReceive: 接收到的轮询信息");
        Intent intent2=new Intent(context, EnvelopeService.class);
        context.startService(intent2);
    }
}
