package com.example.powerlock

import android.accessibilityservice.AccessibilityService
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Best-effort detector only. Android does not expose a stable public Power Menu API.
 * Detection is heuristic and varies by device/OS version.
 */
class PowerMenuAccessibilityService : AccessibilityService() {
    private var overlay: View? = null
    private var windowManager: WindowManager? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        val pkg = event.packageName?.toString().orEmpty()
        if (pkg != "com.android.systemui") return

        val clues = buildString {
            append(event.className?.toString().orEmpty().lowercase())
            append(' ')
            append(event.text.joinToString(" ").lowercase())
            append(' ')
            append(event.contentDescription?.toString()?.lowercase().orEmpty())
        }
        val likelyPowerMenu = listOf("power", "shutdown", "power off", "restart", "reboot", "emergency", "sos")
            .any { clues.contains(it) }
        if (likelyPowerMenu) showReminderOverlay()
    }

    private fun showReminderOverlay() {
        if (overlay != null || !SettingsCanDraw.canDraw(this)) return
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 36, 28, 36)
            setBackgroundColor(0xEE20242B.toInt())
            isFocusable = true
            isFocusableInTouchMode = true
            setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                    removeOverlay()
                    true
                } else false
            }
            addView(TextView(this@PowerMenuAccessibilityService).apply {
                text = "PowerLock সক্রিয়\n\nফোন বন্ধ বা Restart করার আগে নিশ্চিত হও।\n\nজরুরি সাহায্যের প্রয়োজন হলে নিচের বোতাম চাপো।"
                textSize = 19f
                setTextColor(0xFFFFFFFF.toInt())
                gravity = Gravity.CENTER
            })
            addView(Button(this@PowerMenuAccessibilityService).apply {
                text = "Back / বন্ধ করে Power Menu দেখাও"
                setOnClickListener { removeOverlay() }
            })
            addView(Button(this@PowerMenuAccessibilityService).apply {
                text = "জরুরি / SOS দরকার — ওভারলে সরাও"
                setOnClickListener { removeOverlay() }
            })
        }
        val type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            type,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.CENTER }
        try {
            windowManager?.addView(content, params)
            content.requestFocus()
            overlay = content
        } catch (_: Exception) {
            overlay = null
        }
    }

    private fun removeOverlay() {
        val current = overlay ?: return
        try { windowManager?.removeView(current) } catch (_: Exception) { }
        overlay = null
    }

    override fun onInterrupt() { removeOverlay() }
    override fun onDestroy() { removeOverlay(); super.onDestroy() }
}

private object SettingsCanDraw {
    fun canDraw(service: AccessibilityService): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) android.provider.Settings.canDrawOverlays(service) else true
}
