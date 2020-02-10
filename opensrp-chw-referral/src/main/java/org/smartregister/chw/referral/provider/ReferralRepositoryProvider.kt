package org.smartregister.chw.referral.provider

import android.content.Context
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.smartregister.chw.referral.domain.FollowupFeedbackObject
import org.smartregister.chw.referral.domain.ReferralServiceIndicatorObject
import org.smartregister.chw.referral.domain.ReferralServiceObject
import org.smartregister.chw.referral.repository.FollowupFeedbackRepository
import org.smartregister.chw.referral.repository.ReferralServiceIndicatorRepository
import org.smartregister.chw.referral.repository.ReferralServiceRepository
import org.smartregister.chw.referral.util.JsonFormConstants
import org.smartregister.util.AssetHandler
import timber.log.Timber

class ReferralRepositoryProvider : KoinComponent {

    private val referralServiceRepository by inject<ReferralServiceRepository>()
    private val followupFeedbackRepository by inject<FollowupFeedbackRepository>()
    private val context by inject<Context>()
    /**
     * Use this method for testing purposes ONLY.
     * It seeds various data required by the module
     * It should be replaced by data synchronized from the server
     * in future versions of the application
     */
    fun seedSampleReferralServicesAndIndicators() {

        if (referralServiceRepository.referralServiceObjects == null) {
            try {
                val referralServicesAndIndicatorsJsonString =
                    AssetHandler.readFileFromAssetsFolder(
                        "ec_referral_services_and_indicators.json", context
                    )

                val services = JSONObject(referralServicesAndIndicatorsJsonString)
                    .getJSONArray(JsonFormConstants.SERVICES)
                for (i in 0 until services.length()) {
                    val serviceObj = services.getJSONObject(i)
                    val referralServiceObject =
                        Gson().fromJson(serviceObj.toString(), ReferralServiceObject::class.java)
                    referralServiceRepository.saveReferralService(referralServiceObject)
                    val indicatorsArray =
                        serviceObj.getJSONArray(JsonFormConstants.INDICATORS)
                    for (j in 0 until indicatorsArray.length()) {
                        val indicatorObj = indicatorsArray.getJSONObject(j)
                        val referralServiceIndicatorObject =
                            Gson().fromJson(
                                indicatorObj.toString(),
                                ReferralServiceIndicatorObject::class.java
                            )
                        Timber.i(
                            "Before saving indicator = %s",
                            Gson().toJson(referralServiceIndicatorObject)
                        )
                        ReferralServiceIndicatorRepository().saveReferralServiceIndicator(
                            referralServiceIndicatorObject
                        )
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        try {
            if (followupFeedbackRepository.followupFeedbacks!!.isEmpty()) {

                val followupFeedbackJSONArrayList =
                    JSONArray(
                        AssetHandler.readFileFromAssetsFolder(
                            "ec_referral_feedback.json", context
                        )
                    )
                for (i in 0 until followupFeedbackJSONArrayList.length()) {

                    val followupFeedbackObject =
                        Gson().fromJson(
                            followupFeedbackJSONArrayList.getJSONObject(i).toString(),
                            FollowupFeedbackObject::class.java
                        )
                    followupFeedbackRepository.saveFollowupFeedback(followupFeedbackObject)
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}