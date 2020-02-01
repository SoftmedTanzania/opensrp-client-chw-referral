package org.smartregister.chw.referral.domain

import com.google.gson.annotations.SerializedName
import org.smartregister.chw.referral.util.DBConstants
import org.smartregister.commonregistry.CommonPersonObjectClient
import java.io.Serializable

data class MemberObject(val client: CommonPersonObjectClient) : Serializable {

    var firstName = client.columnmaps[DBConstants.Key.FIRST_NAME] ?: ""
    var middleName = client.columnmaps[DBConstants.Key.MIDDLE_NAME] ?: ""
    var lastName = client.columnmaps[DBConstants.Key.LAST_NAME] ?: ""
    var address = client.columnmaps[DBConstants.Key.VILLAGE_TOWN] ?: ""
    var gender = client.columnmaps[DBConstants.Key.GENDER] ?: ""
    var uniqueId = client.columnmaps[DBConstants.Key.UNIQUE_ID]
    var age = client.columnmaps[DBConstants.Key.DOB] ?: ""
    var relationalid = client.columnmaps[DBConstants.Key.RELATIONAL_ID]
    var details = client.columnmaps[DBConstants.Key.DETAILS]
    var baseEntityId = client.columnmaps[DBConstants.Key.BASE_ENTITY_ID]
    var relationalId = client.columnmaps[DBConstants.Key.RELATIONAL_ID]
    var primaryCareGiver = client.columnmaps[DBConstants.Key.PRIMARY_CARE_GIVER]
    var familyHead = client.columnmaps[DBConstants.Key.FAMILY_HEAD]
    var familyBaseEntityId = client.columnmaps[DBConstants.Key.RELATIONAL_ID]
    var familyName = client.columnmaps[DBConstants.Key.FAMILY_NAME]
    var phoneNumber = client.columnmaps[DBConstants.Key.PHONE_NUMBER]
    var familyHeadPhoneNumber = client.columnmaps[DBConstants.Key.FAMILY_HEAD_PHONE_NUMBER]
    var otherPhoneNumber = client.columnmaps[DBConstants.Key.OTHER_PHONE_NUMBER]
    var servicesBeforeReferral = client.columnmaps[DBConstants.Key.SERVICE_BEFORE_REFERRAL]
    var isClosed = client.columnmaps[DBConstants.Key.IS_CLOSED] == "1"

    var servicesBeforeReferralOther =
        client.columnmaps[DBConstants.Key.SERVICE_BEFORE_REFERRAL_OTHER]


    @SerializedName("chw_referral_hf")
    var chwReferralHf = client.columnmaps[DBConstants.Key.REFERRAL_HF]

    @SerializedName("chw_referral_service")
    var chwReferralService = client.columnmaps[DBConstants.Key.REFERRAL_SERVICE]

    @SerializedName("chw_referral_service_id")
    var chwReferralServiceId = client.columnmaps[DBConstants.Key.REFERRAL_SERVICE_ID]

    @SerializedName("chw_referral_date")
    var chwReferralDate = client.columnmaps[DBConstants.Key.REFERRAL_DATE]

    @SerializedName("problem")
    var problem = client.columnmaps[DBConstants.Key.PROBLEM]

    @SerializedName("problem_other")
    var problemOther = client.columnmaps[DBConstants.Key.PROBLEM_OTHER]

    @SerializedName("referral_type")
    var referralType = client.columnmaps[DBConstants.Key.REFERRAL_TYPE]

    @SerializedName("referral_status")
    var referralStatus = client.columnmaps[DBConstants.Key.REFERRAL_STATUS]

    @SerializedName("referral_appointment_date")
    var referralAppointmentDate = client.columnmaps[DBConstants.Key.REFERRAL_APPOINTMENT_DATE]

    @SerializedName("is_emergency_referral")
    var isEmergencyReferral = client.columnmaps[DBConstants.Key.IS_EMERGENCY_REFERRAL] == "1"
}