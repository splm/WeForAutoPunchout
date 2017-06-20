package me.splm.app.autopunchout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import me.splm.app.autopunchout.Utils.OpenSysUI;
import me.splm.app.autopunchout.service.LongRunningService;
import me.splm.app.autopunchout.service.ServiceUtils;

public class MainActivity extends AppCompatActivity {

    private Button mClick_one_btn;
    private Button mClick_two_btn;
    private String mServiceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mClick_one_btn=(Button) findViewById(R.id.click1_btn);
        mClick_two_btn=(Button) findViewById(R.id.click2_btn);
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
        mClick_one_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               OpenSysUI.sendNotification(MainActivity.this);
           }
       });
        mClick_two_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenSysUI.cleanAllNotification(MainActivity.this);
            }
        });
    }



}
