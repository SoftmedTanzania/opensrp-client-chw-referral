package org.smartregister.chw.referral.model

import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.smartregister.chw.referral.domain.MemberObject
import org.smartregister.chw.referral.domain.ReferralServiceIndicatorObject
import org.smartregister.chw.referral.domain.ReferralServiceObject
import org.smartregister.chw.referral.repository.ReferralServiceIndicatorRepository
import org.smartregister.chw.referral.repository.ReferralServiceRepository
import org.smartregister.chw.referral.util.DBConstants
import org.smartregister.chw.referral.util.JsonFormConstants
import org.smartregister.chw.referral.util.JsonFormUtils.addFormMetadata
import org.smartregister.chw.referral.util.JsonFormUtils.getFormAsJson
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder
import org.smartregister.domain.Location
import org.smartregister.repository.LocationRepository
import timber.log.Timber

open class BaseIssueReferralModel : AbstractIssueReferralModel() {

    override fun getLocationId(locationName: String?): String? = null

    override val healthFacilities: List<Location>?
        get() = try {
            LocationRepository().allLocations
        } catch (e: Exception) {
            Timber.e(e)
            null
        }


    override fun getReferralServicesList(referralServiceId: String): ReferralServiceObject? {
        return try {
            val referralServiceRepository = ReferralServiceRepository()
            var referralServiceObject: ReferralServiceObject? = null
            try {
                referralServiceObject =
                    referralServiceRepository.getReferralServiceById(referralServiceId)
            } catch (e: Exception) {
                Timber.e(e)
            }
            referralServiceObject
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    override fun getIndicatorsByServiceId(serviceId: String): List<ReferralServiceIndicatorObject>? {
        return try {
            ReferralServiceIndicatorRepository().getServiceIndicatorsByServiceId(serviceId)
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    override fun mainSelect(tableName: String, mainCondition: String): String {
        val queryBuilder = SmartRegisterQueryBuilder()
        queryBuilder.SelectInitiateMainTable(tableName, mainColumns(tableName))
        return queryBuilder.mainCondition(mainCondition)
    }

    protected open fun mainColumns(tableName: String) = arrayOf(
        tableName + "." + DBConstants.Key.RELATIONAL_ID,
        tableName + "." + DBConstants.Key.BASE_ENTITY_ID,
        tableName + "." + DBConstants.Key.FIRST_NAME,
        tableName + "." + DBConstants.Key.MIDDLE_NAME,
        tableName + "." + DBConstants.Key.LAST_NAME,
        tableName + "." + DBConstants.Key.UNIQUE_ID,
        tableName + "." + DBConstants.Key.GENDER,
        tableName + "." + DBConstants.Key.DOB,
        tableName + "." + DBConstants.Key.DOD
    )

    @Throws(Exception::class)
    override fun getFormWithValuesAsJson(
        formName: String?, entityId: String?, currentLocationId: String?,
        memberObject: MemberObject?
    ): JSONObject? {
        val jsonForm = getFormAsJson(formName)
        addFormMetadata(jsonForm, entityId, currentLocationId)
        return setFormValues(jsonForm, JsonFormConstants.STEP1, memberObject)
    }

    @VisibleForTesting
    fun setFormValues(form: JSONObject, step: String?, memberObject: MemberObject?): JSONObject {
        try {
            val fieldsArray = form.getJSONObject(step)
                .getJSONArray(JsonFormConstants.FIELDS)
            setFieldValues(fieldsArray, memberObject)
            Timber.i("Form JSON = %s", form.toString())
        } catch (e: Exception) {
            Timber.e(e)
        }
        return form
    }

    private fun setFieldValues(fieldsArray: JSONArray, memberObject: MemberObject?) {
        var memberJSONObject: JSONObject? = null
        try {
            memberJSONObject = JSONObject(Gson().toJson(memberObject))
        } catch (e: JSONException) {
            Timber.e(e)
        }
        (0 until fieldsArray.length()).forEach { i ->
            val fieldObject: JSONObject
            try {
                fieldObject = fieldsArray.getJSONObject(i)
                val key =
                    fieldObject.getString(JsonFormConstants.KEY)
                if (memberJSONObject != null) {
                    fieldObject.put(
                        JsonFormConstants.VALUE,
                        memberJSONObject.getString(key)
                    )
                }
            } catch (e: JSONException) {
                Timber.e(e)
            }
        }
    }
}