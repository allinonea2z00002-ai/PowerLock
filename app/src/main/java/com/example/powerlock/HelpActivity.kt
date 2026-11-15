package com.example.powerlock

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView

class HelpActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val message = TextView(this).apply {
            text = """
                PowerLock-এর সীমাবদ্ধতা

                • সাধারণ Android অ্যাপ Power button বন্ধ করতে পারে না।

                • ফোন বন্ধ বা Restart আটকানোর নিশ্চয়তা নেই।

                • Power menu শনাক্তকরণ সব ফোনে কাজ নাও করতে পারে।

                • Overlay permission এবং Accessibility service
                  আলাদাভাবে চালু করতে হবে।

                • SOS বোতামটি শুধু PowerLock-এর reminder বন্ধ করে।
                  এটি জরুরি কল করে না।
            """.trimIndent()

            textSize = 18f
            gravity = Gravity.CENTER
            setPadding(28, 28, 28, 28)
        }

        setContentView(message)
    }
}
