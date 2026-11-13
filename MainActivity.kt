package com.example.powerlock

import android.app.Activity
import android.content.ComponentName
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
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
        }
        root.addView(TextView(this).apply {
            text = "PowerLock\n\nPower menu দেখা গেলে একটি সতর্কতা-লক ওভারলে দেখানোর চেষ্টা করবে। Android-এর আসল Power Off, Restart বা SOS বন্ধ করতে পারবে না।"
            textSize = 18f
            gravity = Gravity.CENTER
        }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        root.addView(Button(this).apply {
            text = "১. অন্য অ্যাপের ওপর দেখানোর অনুমতি"
            setOnClickListener {
                if (!Settings.canDrawOverlays(this@MainActivity)) {
                    startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
                }
            }
        })
        root.addView(Button(this).apply {
            text = "২. Accessibility চালু করার সেটিংস"
            setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
        })
        root.addView(Button(this).apply {
            text = "৩. পরীক্ষা / নির্দেশনা"
            setOnClickListener {
                startActivity(Intent(this@MainActivity, HelpActivity::class.java))
            }
        })
        setContentView(root)
    }
}
