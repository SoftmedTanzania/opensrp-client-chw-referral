package org.smartregister.chw.referral.interactor

import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import com.nerdstone.neatformcore.domain.model.NFormViewData
import org.apache.commons.lang3.text.WordUtils
import org.json.JSONObject
import org.smartregister.chw.referral.ReferralLibrary
import org.smartregister.chw.referral.contract.BaseIssueReferralContract
import org.smartregister.chw.referral.util.Constants
import org.smartregister.chw.referral.util.JsonFormConstants
import org.smartregister.chw.referral.util.JsonFormUtils.processJsonForm
import org.smartregister.chw.referral.util.ReferralUtil.createReferralTask
import org.smartregister.chw.referral.util.Util.processEvent
import timber.log.Timber
import java.util.*

class BaseIssueReferralInteractor : BaseIssueReferralContract.Interactor {

    override fun onDestroy(isChangingConfiguration: Boolean) = Unit

    @Throws(Exception::class)
    override fun saveRegistration(
        baseEntityId: String, valuesHashMap: HashMap<String, NFormViewData>,
        jsonObject: JSONObject, callBack: BaseIssueReferralContract.InteractorCallBack
    ) {
        saveRegistration(baseEntityId, valuesHashMap, jsonObject)
        callBack.onRegistrationSaved()
    }

    @VisibleForTesting
    @Throws(Exception::class)
    fun saveRegistration(
        baseEntityId: String?, valuesHashMap: HashMap<String, NFormViewData>, jsonObject: JSONObject
    ) {
        val allSharedPreferences =
            ReferralLibrary.getInstance().context.allSharedPreferences()
        val referralTask =
            processJsonForm(
                allSharedPreferences, baseEntityId, valuesHashMap,
                jsonObject, Constants.EventType.REGISTRATION
            )

        referralTask.apply {
            groupId =
                (valuesHashMap["chw_referral_hf"]?.value as NFormViewData?)?.metadata?.get("openmrs_entity_id")
                    .toString()
            focus =
                WordUtils.capitalize(jsonObject.getString(JsonFormConstants.REFERRAL_TASK_FOCUS))
            referralDescription = extractReferralProblems(valuesHashMap)
            event.eventId = UUID.randomUUID().toString()
        }

        Timber.i("Referral Event = %s", Gson().toJson(referralTask))
        processEvent(allSharedPreferences, referralTask.event)
        createReferralTask(referralTask, allSharedPreferences)
    }

    private fun extractReferralProblems(valuesHashMap: HashMap<String, NFormViewData>): String? {
        val valuesMap = valuesHashMap[JsonFormConstants.PROBLEM]?.value as HashMap<*, *>?
        valuesMap?.also { mapValues ->
            return mapValues.map { (it.value as NFormViewData).value as String }
                .toList()
                .joinToString()
        }
        return null
    }
}