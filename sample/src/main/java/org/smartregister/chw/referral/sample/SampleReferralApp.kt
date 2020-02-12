package org.smartregister.chw.referral.sample

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

class SampleReferralApp : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}