package org.smartregister.chw.referral.domain

import org.smartregister.clientandeventmodel.Event

/**
 * This is the wrapper class for both Event and Task. When a client is referred, a referral [Event] is
 * created from the provided [event] then a FHIR based task is also generated and synced to OpenSRP
 * app server. The task specification details can be found here:
 * @see [OpenSRP Referral Task Specification](https://smartregister.atlassian.net/wiki/spaces/Documentation/pages/1156251738/FHIR+Task+Based+Referrals+Software+Requirements+Specification)
 */
data class ReferralTask(var event: Event) {
    var groupId: String? = null
    var referralDescription: String? = null
    var focus: String? = null
}