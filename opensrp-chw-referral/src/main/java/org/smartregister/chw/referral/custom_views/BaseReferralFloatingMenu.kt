package org.smartregister.chw.referral.custom_views

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.smartregister.chw.referral.R
import org.smartregister.chw.referral.fragment.BaseReferralCallDialogFragment

open class BaseReferralFloatingMenu : LinearLayout, View.OnClickListener {

    var phoneNumber: String? = null
    var familyHeadName: String? = null
    var familyHeadPhone: String? = null
    var clientName: String? = null

    constructor(
        context: Context?, referralClientName: String?, malariaClientPhone: String?,
        clientFamilyHeadName: String?, clientFamilyHeadPhone: String?
    ) : super(context) {
        initUi()
        clientName = referralClientName
        phoneNumber = malariaClientPhone
        familyHeadName = clientFamilyHeadName
        familyHeadPhone = clientFamilyHeadPhone
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initUi()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        initUi()
    }

    protected fun initUi() {
        View.inflate(context, R.layout.view_referral_call_floating_menu, this)
        val fab = findViewById<FloatingActionButton>(R.id.referral_fab)
        fab.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.referral_fab) {
            val activity = context as Activity
            BaseReferralCallDialogFragment.launchDialog(
                activity, clientName, phoneNumber, familyHeadName, familyHeadPhone
            )
        }
    }

}