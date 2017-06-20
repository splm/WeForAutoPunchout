package me.splm.app.autopunchout.Utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import me.splm.app.autopunchout.R;


public class OpenSysUI {
    public static void openSetting(Context context){
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private static NotificationManager getNotificationMananger(Context context){
        //获取NotificationManager实例
        NotificationManager notifyManager = (NotificationManager)context. getSystemService(Context.NOTIFICATION_SERVICE);
        return notifyManager;
    }

    public static void sendNotification(Context context) {
        NotificationManager notifyManager=getNotificationMananger(context);
        //实例化NotificationCompat.Builde并设置相关属性
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setTicker("[Punchout] is comming!")
                .setContentInfo("info")
                //设置小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置通知标题
                .setContentTitle("AutoPunchout")
                //设置通知内容
                .setContentText("The logic business of AutoPunchout!");
        //设置通知时间，默认为系统发出通知的时间，通常不用设置
        //.setWhen(System.currentTimeMillis());
        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        notifyManager.notify(1, builder.build());
    }

    public static void cleanAllNotification(Context context){
        NotificationManager notifyManager=getNotificationMananger(context);
        notifyManager.cancelAll();
    }
}
