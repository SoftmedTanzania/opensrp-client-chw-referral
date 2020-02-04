package org.smartregister.chw.referral

import com.google.gson.Gson
import id.zelory.compressor.Compressor
import org.json.JSONArray
import org.json.JSONObject
import org.smartregister.Context
import org.smartregister.CoreLibrary
import org.smartregister.chw.referral.domain.FollowupFeedbackObject
import org.smartregister.chw.referral.domain.ReferralMetadata
import org.smartregister.chw.referral.domain.ReferralServiceIndicatorObject
import org.smartregister.chw.referral.domain.ReferralServiceObject
import org.smartregister.chw.referral.repository.FollowupFeedbackRepository
import org.smartregister.chw.referral.repository.ReferralServiceIndicatorRepository
import org.smartregister.chw.referral.repository.ReferralServiceRepository
import org.smartregister.chw.referral.util.JsonFormConstants
import org.smartregister.repository.Repository
import org.smartregister.repository.TaskNotesRepository
import org.smartregister.repository.TaskRepository
import org.smartregister.sync.ClientProcessorForJava
import org.smartregister.sync.helper.ECSyncHelper
import org.smartregister.util.AssetHandler
import timber.log.Timber

class ReferralLibrary private constructor(
    val context: Context, val repository: Repository,
    var referralMetadata: ReferralMetadata, val applicationVersion: Int,
    val databaseVersion: Int
) {

    val clientProcessorForJava: ClientProcessorForJava by lazy {
        ClientProcessorForJava.getInstance(context.applicationContext())
    }
    val compressor: Compressor by lazy {
        Compressor.getDefault(context.applicationContext())
    }
    val taskRepository: TaskRepository by lazy {
        TaskRepository(TaskNotesRepository())
    }

    val ecSyncHelper: ECSyncHelper by lazy {
        ECSyncHelper.getInstance(context.applicationContext())
    }

    /**
     * Use this method for testing purposes ONLY.
     * It seeds various data required by the module
     * It should be replaced by data synchronized from the server
     * in future versions of the application
     */
    fun seedSampleReferralServicesAndIndicators() {
        val referralServiceRepository = ReferralServiceRepository()
        val followupFeedbackRepository = FollowupFeedbackRepository()
        if (referralServiceRepository.referralServices == null) {
            try {
                val referralServicesAndIndicatorsJsonString =
                    AssetHandler.readFileFromAssetsFolder(
                        "ec_referral_services_and_indicators.json",
                        context.applicationContext()
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
                            "ec_referral_feedback.json", context.applicationContext()
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

    companion object {

        @Volatile
        private var instance: ReferralLibrary? = null

        @JvmStatic
        fun init(
            context: Context, repository: Repository, referralMetadata: ReferralMetadata,
            applicationVersion: Int, databaseVersion: Int
        ) {
            instance ?: synchronized(this) {
                ReferralLibrary(
                    context, repository, referralMetadata, applicationVersion, databaseVersion
                ).also { instance = it }
            }
        }

        @JvmStatic
        fun getInstance(): ReferralLibrary {
            checkNotNull(instance) {
                (" Instance does not exist!!! Call ${CoreLibrary::class.java.name}" +
                        ".init method in the onCreate method of  your Application class")
            }
            return instance!!
        }

    }
}