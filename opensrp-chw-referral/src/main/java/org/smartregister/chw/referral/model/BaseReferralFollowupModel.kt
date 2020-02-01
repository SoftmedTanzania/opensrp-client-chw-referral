package org.smartregister.chw.referral.model

import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.smartregister.chw.referral.domain.FollowupFeedbackObject
import org.smartregister.chw.referral.domain.ReferralFollowupObject
import org.smartregister.chw.referral.repository.FollowupFeedbackRepository
import org.smartregister.chw.referral.util.JsonFormConstants
import org.smartregister.chw.referral.util.JsonFormUtils.addFormMetadata
import org.smartregister.chw.referral.util.JsonFormUtils.getFormAsJson
import timber.log.Timber

open class BaseReferralFollowupModel : AbstractReferralFollowupModel() {
    @Throws(Exception::class)
    override fun getFormWithValuesAsJson(
        formName: String,
        entityId: String,
        currentLocationId: String,
        referralFollowupObject: ReferralFollowupObject?
    ): JSONObject? {
        val jsonForm =
            getFormAsJson(formName)
        addFormMetadata(
            jsonForm,
            entityId,
            currentLocationId
        )
        return setFormValues(
            jsonForm,
            JsonFormConstants.STEP1,
            referralFollowupObject
        )
    }

    override fun followupFeedbackList(): List<FollowupFeedbackObject>? {
        return try {
            FollowupFeedbackRepository().followupFeedbacks
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    @VisibleForTesting
    fun setFormValues(
        form: JSONObject,
        step: String?,
        referralFollowupObject: ReferralFollowupObject?
    ): JSONObject {
        try {
            val fieldsArray = form.getJSONObject(step)
                .getJSONArray(JsonFormConstants.FIELDS)
            setFieldValues(fieldsArray, referralFollowupObject)
            Timber.i("Form JSON = %s", form.toString())
        } catch (e: Exception) {
            Timber.e(e)
        }
        return form
    }

    private fun setFieldValues(
        fieldsArray: JSONArray,
        referralFollowupObject: ReferralFollowupObject?
    ) {
        var followupJSONObject: JSONObject? = null
        try {
            followupJSONObject = JSONObject(Gson().toJson(referralFollowupObject))
        } catch (e: JSONException) {
            Timber.e(e)
        }
        for (i in 0 until fieldsArray.length()) {
            var fieldObject: JSONObject
            try {
                fieldObject = fieldsArray.getJSONObject(i)
                val key =
                    fieldObject.getString(JsonFormConstants.KEY)
                if (followupJSONObject != null) {
                    fieldObject.put(
                        JsonFormConstants.VALUE,
                        followupJSONObject.getString(key)
                    )
                }
            } catch (e: JSONException) {
                Timber.e(e)
            }
        }
    }
}