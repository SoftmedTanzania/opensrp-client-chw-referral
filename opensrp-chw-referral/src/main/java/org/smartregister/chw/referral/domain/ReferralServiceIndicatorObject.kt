package org.smartregister.chw.referral.domain

import com.google.gson.annotations.SerializedName
import org.smartregister.chw.referral.util.DBConstants
import org.smartregister.commonregistry.CommonPersonObjectClient
import java.io.Serializable

/**
 * This is the entity class for the referral service indicators that wraps the content of [client] and implements [Serializable]
 */
data class ReferralServiceIndicatorObject(val client: CommonPersonObjectClient) :
    Serializable {

    var isActive = client.columnmaps[DBConstants.Key.IS_ACTIVE] == "1"
    var isChecked = false  //Only used in selection of indicators from UI
    var relationalId = client.columnmaps[DBConstants.Key.RELATIONAL_ID]

    @SerializedName("indicatorId")
    var id = client.columnmaps[DBConstants.Key.ID]

    @SerializedName("indicatorName")
    var nameEn = client.columnmaps[DBConstants.Key.NAME_EN] ?: ""

    @SerializedName("indicatorNameSw")
    var nameSw: String? = client.columnmaps[DBConstants.Key.NAME_SW] ?: ""

}