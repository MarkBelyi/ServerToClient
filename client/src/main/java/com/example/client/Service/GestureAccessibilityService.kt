package com.example.client.Service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.util.Log

class GestureAccessibilityService : AccessibilityService() {
    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("GestureAccessibilityService", "Service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    }

    override fun onInterrupt() {
        Log.d("GestureAccessibilityService", "Service interrupted")
    }

    fun performSwipeUp() {
        Log.d("GestureAccessibilityService", "Performing swipe up")
    }

    fun performSwipeDown() {
        Log.d("GestureAccessibilityService", "Performing swipe down")
    }

    fun performSwipeRight() {
        Log.d("GestureAccessibilityService", "Performing swipe right")
    }

    fun performSwipeLeft() {
        Log.d("GestureAccessibilityService", "Performing swipe left")
    }
}