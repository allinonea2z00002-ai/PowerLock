package com.example.powerlock

import android.accessibilityservice.AccessibilityService
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class PowerMenuAccessibilityService : AccessibilityService() {

    private var windowManager: WindowManager? = null
    private var reminderView: LinearLayout? = null

    override fun onServiceConnected() {
        super.onServiceConnected()

        windowManager =
            getSystemService(WINDOW_SERVICE) as WindowManager
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (event == null) return

        val packageName = event.packageName?.toString().orEmpty()

        val text = buildString {
            append(event.text.joinToString(" "))
            append(" ")
            append(event.contentDescription ?: "")
            append(" ")
            append(event.className ?: "")
        }.lowercase()

        val systemUiEvent =
            packageName.contains("systemui", ignoreCase = true)

        val powerClue = listOf(
            "power off",
            "power menu",
            "shutdown",
            "shut down",
            "restart",
            "reboot",
            "emergency",
            "sos",
            "বন্ধ করুন",
            "পুনরায় চালু"
        ).any {
            text.contains(it)
        }

        if (systemUiEvent && powerClue) {
            showReminder()
        }
    }

    override fun onInterrupt() {
        removeReminder()
    }

    private fun showReminder() {

        if (reminderView != null) return

        val wm = windowManager ?: return

        val panel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 28, 28, 28)
            setBackgroundColor(0xEE202124.toInt())
        }

        val message = TextView(this).apply {
            text = "একটু থামুন!\nআপনি কি সত্যিই ফোন বন্ধ করতে চান?"
            textSize = 21f
            setTextColor(0xFFFFFFFF.toInt())
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 20)
        }

        val backButton = Button(this).apply {
            text = "ফিরে যান"
            setOnClickListener {
                removeReminder()
            }
        }

        val sosButton = Button(this).apply {
            text = "SOS / বন্ধ করুন"
            setOnClickListener {
                removeReminder()
            }
        }

        panel.addView(message)
        panel.addView(backButton)
        panel.addView(sosButton)

        val overlayType =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE
            }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            overlayType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
        }

        try {
            wm.addView(panel, params)
            reminderView = panel
        } catch (_: Exception) {
            reminderView = null
        }
    }

    private fun removeReminder() {

        val view = reminderView ?: return

        try {
            windowManager?.removeView(view)
        } catch (_: Exception) {
        } finally {
            reminderView = null
        }
    }

    override fun onDestroy() {
        removeReminder()
        super.onDestroy()
    }
}
