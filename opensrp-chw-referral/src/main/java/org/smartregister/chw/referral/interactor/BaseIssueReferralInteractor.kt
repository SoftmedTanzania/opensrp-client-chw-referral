package org.smartregister.chw.referral.interactor

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
import org.smartregister.chw.referral.util.LocationUtils
import org.smartregister.chw.referral.util.ReferralUtil.createAddoLinkageTask
import org.smartregister.chw.referral.util.ReferralUtil.createReferralTask
import org.smartregister.chw.referral.util.Util.extractReferralProblems
import org.smartregister.chw.referral.util.Util.processEvent
import timber.log.Timber
import java.util.*

/**
 * This interactor class provides actual implementations for all the functionality used in the
 * Referral forms, it implements [BaseIssueReferralContract.Interactor]
 */
open class BaseIssueReferralInteractor : BaseIssueReferralContract.Interactor {

    private val referralLibrary by inject<ReferralLibrary>()


    @Throws(Exception::class)
    override fun saveRegistration(
            baseEntityId: String, valuesHashMap: HashMap<String, NFormViewData>,
            jsonObject: JSONObject, callBack: BaseIssueReferralContract.InteractorCallBack, isAddoLinkage: Boolean
    ) {
        val allSharedPreferences = referralLibrary.context.allSharedPreferences()
        val extractReferralProblems = extractReferralProblems(valuesHashMap)
        val hasProblems = !extractReferralProblems.isNullOrEmpty()
        if (hasProblems) {
            val referralTask: ReferralTask =
                    JsonFormUtils.processJsonForm(
                            referralLibrary, baseEntityId, valuesHashMap,
                            jsonObject, Constants.EventType.REGISTRATION
                    )

            referralTask.apply {
                groupId = if (isAddoLinkage) LocationUtils.getWardId()
                else (valuesHashMap[JsonFormConstants.CHW_REFERRAL_HF]?.value as NFormViewData?)
                    ?.metadata?.get(JsonFormConstants.OPENMRS_ENTITY_ID)
                    .toString()
                focus = WordUtils.capitalize(jsonObject.getString(JsonFormConstants.REFERRAL_TASK_FOCUS))
                referralDescription = extractReferralProblems
                event.eventId = UUID.randomUUID().toString()
            }

            Timber.i("Referral Event = %s", Gson().toJson(referralTask))

            processEvent(referralLibrary, referralTask.event)

            if (isAddoLinkage) {
                createAddoLinkageTask(referralTask, referralLibrary)
            } else {
                createReferralTask(referralTask, referralLibrary)
            }
        }
        callBack.onRegistrationSaved(hasProblems, isAddoLinkage)
    }

}