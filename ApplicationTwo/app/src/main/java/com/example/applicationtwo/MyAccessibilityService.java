package com.example.applicationtwo;

import android.accessibilityservice.AccessibilityButtonController;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class MyAccessibilityService extends AccessibilityService {
  private AccessibilityButtonController accessibilityButtonController;
  private AccessibilityButtonController
      .AccessibilityButtonCallback accessibilityButtonCallback;
  private boolean mIsAccessibilityButtonAvailable;

  @Override
  protected void onServiceConnected() {
    Log.w("99899", "onServiceConnected");
    accessibilityButtonController = getAccessibilityButtonController();
    mIsAccessibilityButtonAvailable =
        accessibilityButtonController.isAccessibilityButtonAvailable();

    if (!mIsAccessibilityButtonAvailable) {
      return;
    }

    AccessibilityServiceInfo serviceInfo = getServiceInfo();
    serviceInfo.flags
        |= AccessibilityServiceInfo.FLAG_REQUEST_ACCESSIBILITY_BUTTON;
    setServiceInfo(serviceInfo);

    accessibilityButtonCallback =
        new AccessibilityButtonController.AccessibilityButtonCallback() {
          @Override
          public void onClicked(AccessibilityButtonController controller) {
            Log.d("MY_APP_TAG", "Accessibility button pressed!");
            Log.d("MY_APP_TAG", "Splitscreen mode!");

            // Go to Splitscreen Mode.
            performGlobalAction(GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN);
/*
            Log.d("MY_APP_TAG", "Start activity!");
            // Start Activity
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.applicationone");
            if (intent == null) {
              return;
            }
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

 */
          }

          @Override
          public void onAvailabilityChanged(
              AccessibilityButtonController controller, boolean available) {
            if (controller.equals(accessibilityButtonController)) {
              mIsAccessibilityButtonAvailable = available;
            }
          }
        };

    if (accessibilityButtonCallback != null) {
      accessibilityButtonController.registerAccessibilityButtonCallback(
          accessibilityButtonCallback);
    }
  }


  @Override
  public void onAccessibilityEvent(AccessibilityEvent event) {
    Log.w("99899", "onAccessibilityEvent");
    AccessibilityNodeInfo interactedNodeInfo = event.getSource();
    if(interactedNodeInfo != null && interactedNodeInfo.getText() != null && (interactedNodeInfo.getText().equals("GOTO SPLITSCREEN MODE") || interactedNodeInfo.getText().equals("LAUNCH APP")
    )) {
      // Go to Splitscreen Mode.
      performGlobalAction(GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN);
    }


    Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.applicationone");
    if (intent == null) {
      return;
    }
    intent.addCategory(Intent.CATEGORY_LAUNCHER);
    intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

    startActivity(intent);

  }

  @Override
  public void onInterrupt() {

  }
}
