package org.smartregister.chw.referral.listener

import android.view.View
import org.smartregister.chw.referral.R
import org.smartregister.chw.referral.fragment.BaseReferralCallDialogFragment
import org.smartregister.chw.referral.util.Util
import timber.log.Timber

class BaseReferralCallWidgetDialogListener(private val callDialogFragment: BaseReferralCallDialogFragment) :
    View.OnClickListener {
    override fun onClick(view: View) {
        when (view.id) {
            R.id.malaria_call_close -> {
                callDialogFragment.dismiss()
            }
            R.id.referral_call_head_phone, R.id.call_referral_client_phone -> {
                try {
                    val phoneNumber = view.tag as String
                    Util.launchDialer(callDialogFragment.activity, callDialogFragment, phoneNumber)
                    callDialogFragment.dismiss()
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

}