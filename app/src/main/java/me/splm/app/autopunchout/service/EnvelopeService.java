package me.splm.app.autopunchout.service;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import me.splm.app.autopunchout.Utils.OpenSysUI;
import me.splm.app.autopunchout.Utils.SuUtils;


public class EnvelopeService extends AccessibilityService {
    private static final String TAG = "*******************";

    private static final String TARGETPACKAGE = "com.tencent.wework";
    private static final String SETTINGPACKAGE = "com.android.settings";
//    private static final String TARGETPACKAGE="me.splm.app.autopunchout";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                List<CharSequence> list = event.getText();
                if (list != null && !list.isEmpty()) {
                    for (CharSequence c : list) {
                        String content = c.toString();
                        if (content.contains("[Punchout]")) {
                            Log.e(TAG, "onAccessibilityEvent: 包含目标字符串");
                            openWXAPP();
                            break;
                        }
                    }
                }
                break;
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (TARGETPACKAGE.equals(event.getPackageName())) {
                    findTagToClick("工作台");
                    findTagToClick("打卡");
//                    findTagToClick("次");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG, "run: 延迟执行开始");
                            findTagToClick("com.tencent.wework:id/a93");
                        }
                    }, 2000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AccessibilityNodeInfo node = findTag("成功");
                            if (node != null) {
                                Log.e(TAG, "onAccessibilityEvent: 打卡完成");
                                OpenSysUI.cleanAllNotification(EnvelopeService.this);
                                SuUtils.kill(TARGETPACKAGE);
                                Log.e(TAG, "onAccessibilityEvent: kill");
                            }
                        }
                    },2000);
                }
                break;
        }
    }

    public void openWXAPP() {
        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(TARGETPACKAGE);
        startActivity(LaunchIntent);
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

    private List<AccessibilityNodeInfo> findNode(String id) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(id);
        return list;
    }

    private boolean hasNode(List<AccessibilityNodeInfo> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        return true;
    }

    private void logAllNodeInfo(AccessibilityNodeInfo nodeInfo, String tag) {
        if (nodeInfo != null) {
            int count = nodeInfo.getChildCount();
            if (count == 0) {
                //TODO 叶子
            } else {
                //TODO 根
                for (int i = 0; i < count; i++) {
                    AccessibilityNodeInfo info = nodeInfo.getChild(i);
                    if (info != null) {
                        Log.e(TAG, "re: " + info.getContentDescription() + "+-----------------+" + info.getText());
                        logAllNodeInfo(info, tag);
                    }
                }
            }
        }
    }

    private AccessibilityNodeInfo findTag(String tag) {
        if (tag.isEmpty()) {
            return null;
        }
        Log.e(TAG, "findTag: 需要查找的tag=" + tag);
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        logAllNodeInfo(nodeInfo, tag);//打印所有的节点信息，便于分析
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(tag);
        boolean isNothing = hasNode(list);
        if (!isNothing) {
            Log.e(TAG, "findTag: 1");
            list = nodeInfo.findAccessibilityNodeInfosByText(tag);
            Log.e(TAG, "findTag: 2");
        }
        boolean isFull = hasNode(list);
        if (isFull) {
            Log.e(TAG, "findTag: 3");
            AccessibilityNodeInfo n = list.get(0);
            Log.e(TAG, "findTag: size==" + list.size());
            if (n != null) {
                return n;
            }
        }
        Log.e(TAG, "findTag: 4");
        return null;
    }

    private void findTagToClick(String tag) {
        AccessibilityNodeInfo nodeInfo = findTag(tag);
        if (nodeInfo != null) {
            Log.e(TAG, "findTagClick: " + nodeInfo.getClassName() + "********" + nodeInfo.getText() + "********** isClckable=" + nodeInfo.isClickable());
            Log.e(TAG, "findTagClick: " + nodeInfo.getParent().getClassName() + "**************" + nodeInfo.getParent().isClickable());
            if (nodeInfo.isClickable()) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            } else {
                nodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    private Handler mClientHandler = new ClientHandler();
    private Messenger mClientMessenger = new Messenger(mClientHandler);

    private class ClientHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.e(TAG, "handleMessage: 接收到服务端的消息");
                    break;
            }
        }
    }

    private class InnerConnOfLongrunService implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Messenger serverMessenger = new Messenger(service);
            Message toServerMsg = Message.obtain(null, 1);
            toServerMsg.replyTo = mClientMessenger;
            try {
                serverMessenger.send(toServerMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private ServiceConnection mConnection = new InnerConnOfLongrunService();

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, LongRunningService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
}
