package me.splm.app.autopunchout.service;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
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
                            OpenSysUI.openAPP(this,TARGETPACKAGE);
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
}
