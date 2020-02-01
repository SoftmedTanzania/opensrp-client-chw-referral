package org.smartregister.chw.referral.model

import androidx.lifecycle.ViewModel
import org.smartregister.chw.referral.contract.BaseFollowupContract
import org.smartregister.chw.referral.domain.MemberObject
import org.smartregister.chw.referral.domain.ReferralFollowupObject

abstract class AbstractReferralFollowupModel : ViewModel(),
    BaseFollowupContract.Model {

    var memberObject: MemberObject? = null

    var referralFollowupObject: ReferralFollowupObject? = null
}