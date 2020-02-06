package org.smartregister.chw.referral.model

import org.smartregister.chw.referral.contract.BaseReferralRegisterContract

open class BaseReferralRegisterModel :
    BaseReferralRegisterContract.Model {

    override fun registerViewConfigurations(viewIdentifiers: List<String?>?) = Unit

    override fun unregisterViewConfiguration(viewIdentifiers: List<String?>?) = Unit

    override fun saveLanguage(language: String?) = Unit

    override fun getLocationId(locationName: String?): String? = null

    override val initials: String?
        get() = null
}