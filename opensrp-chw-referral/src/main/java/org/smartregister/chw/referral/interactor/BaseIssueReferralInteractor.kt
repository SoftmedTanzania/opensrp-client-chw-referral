package org.smartregister.chw.referral.interactor

import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import com.nerdstone.neatformcore.domain.model.NFormViewData
import org.apache.commons.lang3.text.WordUtils
import org.json.JSONObject
import org.koin.core.inject
import org.smartregister.chw.referral.ReferralLibrary
import org.smartregister.chw.referral.contract.BaseIssueReferralContract
import org.smartregister.chw.referral.domain.ReferralTask
import org.smartregister.chw.referral.util.Constants
import org.smartregister.chw.referral.util.JsonFormConstants
import org.smartregister.chw.referral.util.JsonFormUtils
import org.smartregister.chw.referral.util.ReferralUtil.createReferralTask
import org.smartregister.chw.referral.util.Util.processEvent
import timber.log.Timber
import java.util.*

/**
 * This interactor class provides actual implementations for all the functionality used in the
 * Referral forms, it implements [BaseIssueReferralContract.Interactor]
 */
class BaseIssueReferralInteractor : BaseIssueReferralContract.Interactor {

    val referralLibrary by inject<ReferralLibrary>()


    @Throws(Exception::class)
    override fun saveRegistration(
        baseEntityId: String, valuesHashMap: HashMap<String, NFormViewData>,
        jsonObject: JSONObject, callBack: BaseIssueReferralContract.InteractorCallBack
    ) {
        val extractReferralProblems = extractReferralProblems(valuesHashMap)
        val hasProblems = extractReferralProblems != null && extractReferralProblems.isNotEmpty()
        if (hasProblems) {
            val referralTask: ReferralTask =
                JsonFormUtils.processJsonForm(
                    referralLibrary, baseEntityId, valuesHashMap,
                    jsonObject, Constants.EventType.REGISTRATION
                )

            referralTask.apply {
                groupId =
                    (valuesHashMap["chw_referral_hf"]?.value as NFormViewData?)?.metadata?.get("openmrs_entity_id")
                        .toString()
                focus =
                    WordUtils.capitalize(jsonObject.getString(JsonFormConstants.REFERRAL_TASK_FOCUS))
                referralDescription = extractReferralProblems
                event.eventId = UUID.randomUUID().toString()
            }

            Timber.i("Referral Event = %s", Gson().toJson(referralTask))

            processEvent(referralLibrary, referralTask.event)
            createReferralTask(referralTask, referralLibrary)
        }
        callBack.onRegistrationSaved(hasProblems)
    }

    private fun extractReferralProblems(valuesHashMap: HashMap<String, NFormViewData>): String? {
        val valuesMap = valuesHashMap[JsonFormConstants.PROBLEM]?.value as HashMap<*, *>?
        valuesMap?.also { mapValues ->
            return mapValues
                .filter { it.value != null }
                .map { (it.value as NFormViewData).value as String }
                .toList()
                .joinToString()
        }
        return null
    }
}