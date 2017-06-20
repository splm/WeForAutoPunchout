package me.splm.app.autopunchout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.splm.app.autopunchout.Utils.OpenSysUI;
import me.splm.app.autopunchout.service.LongRunningService;
import me.splm.app.autopunchout.service.ServiceUtils;

public class MainActivity extends AppCompatActivity {

    private TextView mClick_tv;
    private Button mClick_btn;
    private String mServiceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mClick_tv=(TextView) findViewById(R.id.click_tv);
        mClick_btn=(Button) findViewById(R.id.click2_btn);
        mServiceName = LongRunningService.class.getCanonicalName();
        Log.e("***************", "onCreate: " + mServiceName);
        boolean isWorking= ServiceUtils.isServiceWork(this,mServiceName);
        if (!isWorking) {
            Intent intent = new Intent(this, LongRunningService.class);
            startService(intent);
        }
        init();
    }

    private void init(){
       mClick_tv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Log.e("*********************", "onClick: 单击了");
               OpenSysUI.sendNotification(MainActivity.this);
           }
       });
        mClick_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenSysUI.cleanAllNotification(MainActivity.this);
            }
        });
    }



}
