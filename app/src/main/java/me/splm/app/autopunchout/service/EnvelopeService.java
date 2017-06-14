package me.splm.app.autopunchout.service;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;


public class EnvelopeService extends AccessibilityService {
    private static final String TAG="*******************";

    private static final String TARGETPACKAGE="com.tencent.wework";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        int eventType = event.getEventType();
        Log.e(TAG, "onAccessibilityEvent: " + event);
        if(TARGETPACKAGE.equals(event.getPackageName())){
            Log.e(TAG, "onAccessibilityEvent: 符合当前包名(1)");
            if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                Log.e(TAG, "onAccessibilityEvent: 拦截到打开Activity的状态(2)");
                findTagClick("打卡");
            }
        }
    }

    @Override
    public void onInterrupt() {
        Log.e(TAG, "onInterrupt: Punchout服务被终止");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.e(TAG, "onServiceConnected: service connected");
    }

    private void findTagClick(String tag){
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(tag);
        if(list==null||list.isEmpty()){
            return;
        }
        for(AccessibilityNodeInfo n:list){
            n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            break;
        }
    }

    private void checkStep1(){
    }
}
