package org.smartregister.chw.referral.util

import android.content.Context
import org.joda.time.DateTime
import org.opensrp.api.constants.Gender
import org.smartregister.chw.referral.R
import org.smartregister.chw.referral.ReferralLibrary
import org.smartregister.chw.referral.domain.ReferralTask
import org.smartregister.chw.referral.util.Constants.BusinessStatus
import org.smartregister.chw.referral.util.Constants.Referral
import org.smartregister.domain.Task
import org.smartregister.repository.BaseRepository
import java.util.*

/**
 * Offers utility functionality for Referrals
 */
object ReferralUtil {
    /**
     * Adds [referralTask] to the task repository defined in the [referralLibrary]
     */
    @JvmStatic
    fun createReferralTask(
        referralTask: ReferralTask, referralLibrary: ReferralLibrary
    ) {
        val allSharedPreferences = referralLibrary.context.allSharedPreferences()
        val task = Task().apply {
            identifier = UUID.randomUUID().toString()
            /* //TODO Implement plans remove hard coded plan (in 2020 road-map)
             val iterator = referralLibrary.planDefinitionRepository
                 .findAllPlanDefinitionIds().iterator()
             if (iterator.hasNext()) {
                 planIdentifier = iterator.next()
             } else {
                 Timber.e("No plans exist in the server")
             }*/
            planIdentifier = Referral.PLAN_ID
            groupIdentifier = referralTask.groupId
            status = Task.TaskStatus.READY
            businessStatus = BusinessStatus.REFERRED
            priority = 3
            code = Referral.CODE
            description = referralTask.referralDescription
            focus = referralTask.focus
            forEntity = referralTask.event.baseEntityId
            val now = DateTime()
            executionStartDate = now
            authoredOn = now
            lastModified = now
            reasonReference = referralTask.event.formSubmissionId
            owner = allSharedPreferences.fetchRegisteredANM()
            syncStatus = BaseRepository.TYPE_Created
            requester =
                allSharedPreferences.getANMPreferredName(allSharedPreferences.fetchRegisteredANM())
            location =
                allSharedPreferences.fetchUserLocalityId(allSharedPreferences.fetchRegisteredANM())
        }
        referralLibrary.taskRepository.addOrUpdate(task)
    }

    @JvmStatic
    fun getTranslatedGenderString(context : Context, gender: String): String {
        if (gender.equals(Gender.MALE.toString(), ignoreCase = true)) {
            return context.resources.getString(R.string.male)
        } else if (gender.equals(Gender.FEMALE.toString(), ignoreCase = true)) {
            return context.resources.getString(R.string.female)
        }
        return ""
    }


    @JvmStatic
    fun getTranslatedReferralServiceType(context : Context, type: String): String {
        when(type.toLowerCase(Locale.getDefault())) {
            Constants.ReferralServiceType.SICK_CHILD.toLowerCase(Locale.getDefault()) -> {
                return context.getString(R.string.sick_child)
            }
            Constants.ReferralServiceType.ANC_DANGER_SIGNS.toLowerCase(Locale.getDefault()) -> {
                return context.getString(R.string.anc_danger_signs)
            }
            Constants.ReferralServiceType.PNC_DANGER_SIGNS.toLowerCase(Locale.getDefault()) -> {
                return context.getString(R.string.pnc_danger_signs)
            }
            Constants.ReferralServiceType.FP_SIDE_EFFECTS.toLowerCase(Locale.getDefault()) -> {
                return context.getString(R.string.fp_initiation)
            }
            Constants.ReferralServiceType.SUSPECTED_MALARIA.toLowerCase(Locale.getDefault()) -> {
                return context.getString(R.string.suspected_malaria)
            }
            Constants.ReferralServiceType.SUSPECTED_HIV.toLowerCase(Locale.getDefault()) -> {
                return context.getString(R.string.suspected_hiv)
            }
            Constants.ReferralServiceType.SUSPECTED_TB.toLowerCase(Locale.getDefault()) -> {
                return context.getString(R.string.suspected_tb)
            }
            Constants.ReferralServiceType.SUSPECTED_GBV.toLowerCase(Locale.getDefault()) -> {
                return context.getString(R.string.suspected_gbv)
            }
            Constants.ReferralServiceType.SUSPECTED_CHILD_GBV.toLowerCase(Locale.getDefault()) -> {
                return context.getString(R.string.suspected_child_gbv)
            }
        }
        return type
    }
}