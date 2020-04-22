package org.smartregister.chw.referral.model

import org.smartregister.chw.referral.domain.FollowupFeedbackObject

open class BaseReferralFollowupModel : AbstractReferralFollowupModel() {
    @Throws(Exception::class)

    override fun followupFeedbackList(): List<FollowupFeedbackObject>? {
        return null
    }

}