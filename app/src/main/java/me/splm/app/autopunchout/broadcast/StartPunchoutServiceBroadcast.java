package me.splm.app.autopunchout.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import me.splm.app.autopunchout.Utils.OpenSysUI;
import me.splm.app.autopunchout.service.EnvelopeService;
import me.splm.app.autopunchout.service.ServiceUtils;

import static me.splm.app.autopunchout.debug.Constans.TAG;

public class StartPunchoutServiceBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("***************", "onReceive: 接收到的轮询信息");
        String serviceName = EnvelopeService.class.getCanonicalName();
        boolean isWorking = ServiceUtils.isServiceWork(context, serviceName);
//        ServiceUtils.buildAlarmanger(context);
        Log.e(TAG, "onReceive: 启动Setting设置界面");
        OpenSysUI.sendNotification(context);
    }
}
