package org.smartregister.chw.referral.domain

import com.google.gson.annotations.SerializedName
import org.smartregister.chw.referral.util.DBConstants
import org.smartregister.commonregistry.CommonPersonObjectClient
import java.io.Serializable

data class FollowupFeedbackObject(val client: CommonPersonObjectClient) : Serializable {

    var id = client.columnmaps[DBConstants.Key.ID]

    @SerializedName("name_en")
    var nameEn = client.columnmaps[DBConstants.Key.NAME_EN] ?: ""

    @SerializedName("name_sw")
    var nameSw = client.columnmaps[DBConstants.Key.NAME_SW] ?: ""

    @SerializedName("is_active")
    var isActive = client.columnmaps[DBConstants.Key.IS_ACTIVE] == "1"
}