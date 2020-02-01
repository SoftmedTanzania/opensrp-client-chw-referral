package org.smartregister.chw.referral.contract

import org.smartregister.chw.referral.domain.MemberObject

interface BaseReferralHistoryContract {

    interface View {

        fun presenter(): Presenter?

        fun setProfileViewWithData(memberObject: MemberObject)
    }

    interface Presenter {

        fun fillClientData(memberObject: MemberObject)

        fun getView(): View?

        fun getMainTable(): String

        fun getMainCondition(): String
    }

    interface Model {
        fun getReferrals(baseEntityId: String?): List<Any?>?
    }
}