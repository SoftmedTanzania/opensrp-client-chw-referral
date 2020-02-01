package org.smartregister.chw.referral.contract

interface BaseReferralCallDialogContract {

    interface View {

        var pendingCallRequest: Dialer?

    }

    interface Model {
        var name: String?
    }

    interface Dialer {
        fun callMe()
    }
}