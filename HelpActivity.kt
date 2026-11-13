package com.example.powerlock

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView

class HelpActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TextView(this).apply {
            text = "সেটআপ:\n১) PowerLock-এর overlay permission দিন।\n২) Accessibility settings-এ PowerLock menu detector চালু করুন।\n৩) Power menu খুলে পরীক্ষা করুন।\n\nসীমাবদ্ধতা: Android/OriginOS সংস্করণভেদে Power Menu শনাক্ত নাও হতে পারে। এই অ্যাপ সিস্টেমের Power Off, Restart, SOS বা জরুরি কল আটকায় না। Overlay দেখা গেলে ‘জরুরি/SOS’ বাটন ব্যবহার করার আগে Back/বন্ধ করুন।"
            textSize = 18f
            gravity = Gravity.CENTER
            setPadding(28, 28, 28, 28)
        })
    }
}
