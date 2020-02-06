package org.smartregister.chw.referral.model

import org.smartregister.chw.referral.domain.ReferralServiceIndicatorObject
import org.smartregister.chw.referral.domain.ReferralServiceObject
import org.smartregister.chw.referral.repository.ReferralServiceIndicatorRepository
import org.smartregister.chw.referral.repository.ReferralServiceRepository
import org.smartregister.chw.referral.util.DBConstants
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

     open fun mainColumns(tableName: String) = arrayOf(
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

}