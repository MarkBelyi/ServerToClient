package com.example.client.Service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class GestureAccessibilityService : AccessibilityService() {
    override fun onServiceConnected() {
        super.onServiceConnected()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}

    fun performSwipeUp() {
        // Логика выполнения свайпа вверх
    }

    fun performSwipeDown() {
        // Логика выполнения свайпа вниз
    }

    fun performSwipeRight() {
        // Логика выполнения свайпа вправо
    }

    fun performSwipeLeft() {
        // Логика выполнения свайпа влево
    }
}