package me.splm.app.autopunchout.service;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;


public class EnvelopeService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        int eventType=event.getEventType();
    }

    @Override
    public void onInterrupt() {

    }
}
