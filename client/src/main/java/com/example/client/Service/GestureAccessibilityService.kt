package com.example.client.Service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent

class GestureAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Обработка событий, если необходимо
    }

    override fun onInterrupt() {
        // Обработка прерываний
    }

    fun performSwipeGesture(direction: String) {
        val path = Path()
        val gestureBuilder = GestureDescription.Builder()

        when (direction) {
            "up" -> {
                path.moveTo(500f, 1000f)
                path.lineTo(500f, 500f)
            }
            "down" -> {
                path.moveTo(500f, 500f)
                path.lineTo(500f, 1000f)
            }
        }

        gestureBuilder.addStroke(GestureDescription.StrokeDescription(path, 0, 500))
        dispatchGesture(gestureBuilder.build(), null, null)
    }
}