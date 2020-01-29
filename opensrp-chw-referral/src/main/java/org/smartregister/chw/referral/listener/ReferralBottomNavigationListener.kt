package org.smartregister.chw.referral.listener

import android.app.Activity
import android.view.MenuItem
import org.smartregister.chw.referral.R
import org.smartregister.listener.BottomNavigationListener
import org.smartregister.view.activity.BaseRegisterActivity

class ReferralBottomNavigationListener(private val context: Activity) :
    BottomNavigationListener(context) {
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        super.onNavigationItemSelected(item)
        val baseRegisterActivity = context as BaseRegisterActivity
        when (item.itemId) {
            R.id.action_sent_referrals -> {
                baseRegisterActivity.switchToBaseFragment()
            }
            R.id.action_received_referrals -> {
                baseRegisterActivity.switchToFragment(1)
            }
            R.id.action_scan_qr -> {
                baseRegisterActivity.startQrCodeScanner()
            }
        }
        return true
    }

}