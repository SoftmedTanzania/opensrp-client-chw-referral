package org.smartregister.chw.referral.model

import org.json.JSONObject
import org.smartregister.chw.referral.contract.BaseReferralRegisterContract
import org.smartregister.chw.referral.util.JsonFormUtils.addFormMetadata
import org.smartregister.chw.referral.util.JsonFormUtils.getFormAsJson

open class BaseReferralRegisterModel :
    BaseReferralRegisterContract.Model {

    override fun registerViewConfigurations(viewIdentifiers: List<String?>?) = Unit

    override fun unregisterViewConfiguration(viewIdentifiers: List<String?>?) = Unit

    override fun saveLanguage(language: String?) = Unit

    override fun getLocationId(locationName: String?): String? = null

    @Throws(Exception::class)
    override fun getFormAsJson(
        formName: String?, entityId: String?, currentLocationId: String?
    ): JSONObject? {
        val jsonObject =
            getFormAsJson(formName)
        addFormMetadata(
            jsonObject,
            entityId,
            currentLocationId
        )
        return jsonObject
    }

    override val initials: String?
        get() = null
}