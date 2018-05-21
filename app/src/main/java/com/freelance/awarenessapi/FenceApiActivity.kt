package com.freelance.awarenessapi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.PendingIntent



class FenceApiActivity : AppCompatActivity() {
    private val FENCE_RECEIVER_ACTION = "FENCE_RECEIVE"

    private val fenceReceiver: HeadphoneFenceBroadcastReceiver? = null
    private val mFencePendingIntent: PendingIntent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fence_api)
    }
}
