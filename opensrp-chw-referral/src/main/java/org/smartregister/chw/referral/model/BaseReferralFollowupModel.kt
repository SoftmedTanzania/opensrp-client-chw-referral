package org.smartregister.chw.referral.model

import android.database.sqlite.SQLiteException
import org.smartregister.chw.referral.domain.FollowupFeedbackObject
import org.smartregister.chw.referral.repository.FollowupFeedbackRepository
import timber.log.Timber

open class BaseReferralFollowupModel : AbstractReferralFollowupModel() {
    @Throws(Exception::class)

    override fun followupFeedbackList(): List<FollowupFeedbackObject>? {
        return try {
            FollowupFeedbackRepository().followupFeedbacks
        } catch (e: SQLiteException) {
            Timber.e(e)
            null
        }
    }

}