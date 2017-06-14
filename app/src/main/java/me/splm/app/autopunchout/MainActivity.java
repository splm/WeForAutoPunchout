package me.splm.app.autopunchout;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import me.splm.app.autopunchout.service.LongRunningService;

public class MainActivity extends AppCompatActivity {

    private String mServiceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mServiceName = LongRunningService.class.getCanonicalName();
        Log.e("***************", "onCreate: " + mServiceName);
        if (!isServiceWork(this, mServiceName)) {
            Intent intent = new Intent(this, LongRunningService.class);
            startService(intent);
        }
    }


    public boolean isServiceWork(Context mContext, String serviceName) {
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

}
