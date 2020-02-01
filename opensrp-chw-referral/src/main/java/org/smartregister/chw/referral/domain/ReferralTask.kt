package org.smartregister.chw.referral.domain

import org.smartregister.clientandeventmodel.Event

data class ReferralTask(var event: Event) {
    var groupId: String? = null
    var referralDescription: String? = null
    var focus: String? = null
}