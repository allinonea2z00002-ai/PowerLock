package com.example.powerlock

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 36, 28, 36)
        }

        val title = TextView(this).apply {
            text = "PowerLock"
            textSize = 28f
            gravity = Gravity.CENTER
        }

        val info = TextView(this).apply {
            text = """
                Power menu reminder

                ১. Overlay permission দিন।
                ২. Accessibility service চালু করুন।
                ৩. Power menu শনাক্ত করার চেষ্টা করবে।
            """.trimIndent()
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 24, 0, 24)
        }

        val overlayButton = Button(this).apply {
            text = "Overlay Permission চালু করুন"

            setOnClickListener {
                if (!Settings.canDrawOverlays(this@MainActivity)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    startActivity(intent)
                }
            }
        }

        val accessibilityButton = Button(this).apply {
            text = "Accessibility Settings খুলুন"

            setOnClickListener {
                startActivity(
                    Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                )
            }
        }

        val helpButton = Button(this).apply {
            text = "Help / সীমাবদ্ধতা"

            setOnClickListener {
                startActivity(
                    Intent(this@MainActivity, HelpActivity::class.java)
                )
            }
        }

        layout.addView(title, matchParams())
        layout.addView(info, matchParams())
        layout.addView(overlayButton, matchParams())
        layout.addView(accessibilityButton, matchParams())
        layout.addView(helpButton, matchParams())

        setContentView(layout)
    }

    private fun matchParams() =
        LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 8, 0, 8)
        }
}
