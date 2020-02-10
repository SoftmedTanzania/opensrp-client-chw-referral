package org.smartregister.chw.referral.util

import org.joda.time.DateTime
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
            owner = allSharedPreferences.fetchRegisteredANM()
            syncStatus = BaseRepository.TYPE_Created
            requester =
                allSharedPreferences.getANMPreferredName(allSharedPreferences.fetchRegisteredANM())
            location =
                allSharedPreferences.fetchUserLocalityId(allSharedPreferences.fetchRegisteredANM())
        }
        referralLibrary.taskRepository.addOrUpdate(task)
    }
}