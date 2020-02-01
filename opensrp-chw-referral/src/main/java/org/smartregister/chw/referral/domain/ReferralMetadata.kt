package org.smartregister.chw.referral.domain

class ReferralMetadata {

    var locationIdMap: Map<String, String> = hashMapOf()

    constructor()

    constructor(locationIdMap: Map<String, String>) {
        this.locationIdMap = locationIdMap
    }
}