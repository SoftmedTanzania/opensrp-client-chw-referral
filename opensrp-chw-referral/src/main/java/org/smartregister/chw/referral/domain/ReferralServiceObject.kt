package org.smartregister.chw.referral.domain

import org.smartregister.chw.referral.util.DBConstants
import org.smartregister.commonregistry.CommonPersonObjectClient
import java.io.Serializable

/**
 * Entity class for referral services. It maps the data from [client] into a new [ReferralServiceObject].
 * It also implements [Serializable]
 */
data class ReferralServiceObject(val client: CommonPersonObjectClient) : Serializable {
    var id = client.columnmaps[DBConstants.Key.ID]
    var nameEn = client.columnmaps[DBConstants.Key.NAME_EN] ?: ""
    var nameSw = client.columnmaps[DBConstants.Key.NAME_SW] ?: ""
    var identifier = client.columnmaps[DBConstants.Key.REFERRAL_SERVICE_IDENTIFIER] ?: ""
    var isActive = client.columnmaps[DBConstants.Key.IS_ACTIVE] == "1"
}