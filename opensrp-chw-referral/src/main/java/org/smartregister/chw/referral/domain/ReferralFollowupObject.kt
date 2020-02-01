package org.smartregister.chw.referral.domain

import com.google.gson.annotations.SerializedName
import org.smartregister.chw.referral.util.DBConstants
import org.smartregister.commonregistry.CommonPersonObjectClient
import java.io.Serializable

class ReferralFollowupObject : Serializable {

    var relationalId: String? = null
    var details: String? = null
    var baseEntityId: String? = null
    var isClosed = false

    @SerializedName("chw_followup_feedback")
    var chwFollowupFeedback: String? = null

    @SerializedName("chw_followup_feedback_id")
    var chwFollowupFeedbackId: String? = null

    @SerializedName("other_followup_feedback_information")
    var otherFollowupFeedbackInformation: String? = null

    @SerializedName("chw_followup_date")
    var chwFollowupDate: String? = null

    constructor()

    constructor(client: CommonPersonObjectClient) {
        baseEntityId = client.columnmaps[DBConstants.Key.BASE_ENTITY_ID]
        relationalId = client.columnmaps[DBConstants.Key.RELATIONAL_ID]
        chwFollowupFeedbackId = client.columnmaps[DBConstants.Key.CHW_FOLLOWUP_FEEDBACK_ID]
        otherFollowupFeedbackInformation =
            client.columnmaps[DBConstants.Key.OTHER_FOLLOWUP_FEEDBACK_INFORMATION]
        chwFollowupDate = client.columnmaps[DBConstants.Key.CHW_FOLLOWUP_DATE]
        details = client.columnmaps[DBConstants.Key.DETAILS]
        isClosed = client.columnmaps[DBConstants.Key.IS_CLOSED] == "1"
    }

}