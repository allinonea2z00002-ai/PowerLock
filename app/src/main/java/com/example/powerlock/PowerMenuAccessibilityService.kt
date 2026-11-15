
package com.example.powerlock

import android.accessibilityservice.AccessibilityService
import android.app.KeyguardManager
import android.graphics.Color
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

class PowerMenuAccessibilityService : AccessibilityService() {

    private var windowManager: WindowManager? = null
    private var lockView: FrameLayout? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        windowManager =
            getSystemService(WINDOW_SERVICE) as WindowManager
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || lockView != null) return

        val packageName = event.packageName?.toString().orEmpty()

        val text = buildString {
            append(event.text.joinToString(" "))
            append(" ")
            append(event.contentDescription ?: "")
            append(" ")
            append(event.className ?: "")
        }.lowercase()

        val isSystemUI =
            packageName.contains("systemui", ignoreCase = true)

        val powerWords = listOf(
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
        )

        if (isSystemUI && powerWords.any { text.contains(it) }) {
            showLockScreen()
        }
    }

    private fun showLockScreen() {
        if (lockView != null) return

        val wm = windowManager ?: return

        val root = FrameLayout(this).apply {
            setBackgroundColor(Color.BLACK)
            isClickable = true
            isFocusable = true
            setOnTouchListener { _, _ -> true }
        }

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(30, 30, 30, 30)
        }

        val title = TextView(this).apply {
            text = "POWERLOCK"
            textSize = 30f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        val message = TextView(this).apply {
            text = "ফোন বন্ধ করা থেকে ফিরে আসুন"
            textSize = 18f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 25, 0, 35)
        }

        val backButton = Button(this).apply {
            text = "← BACK"
            textSize = 18f

            setOnClickListener {
                val keyguard =
                    getSystemService(KEYGUARD_SERVICE) as KeyguardManager

                val phoneIsLocked = keyguard.isKeyguardLocked

                removeLockScreen()

                if (phoneIsLocked) {
                    performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)
                } else {
                    performGlobalAction(GLOBAL_ACTION_HOME)
                }
            }
        }

        content.addView(title)
        content.addView(message)

        content.addView(
            backButton,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(
            content,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
            )
        )

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.OPAQUE
        ).apply {
            gravity = Gravity.TOP or Gravity.START
        }

        try {
            wm.addView(root, params)
            lockView = root
        } catch (_: Exception) {
            lockView = null
        }
    }

    private fun removeLockScreen() {
        val view = lockView ?: return

        try {
            windowManager?.removeView(view)
        } catch (_: Exception) {
        }

        lockView = null
    }

    override fun onInterrupt() {
        removeLockScreen()
    }

    override fun onDestroy() {
        removeLockScreen()
        super.onDestroy()
    }
}
