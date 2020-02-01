package org.smartregister.chw.referral.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by cozej4 on 2019-12-02.
 *
 * @author cozej4 https://github.com/cozej4
 */
class NeatFormMetaData : Serializable {

    @JvmField
    @SerializedName("openmrs_entity")
    var openmrsEntity: String? = null

    @JvmField
    @SerializedName("openmrs_entity_id")
    var openmrsEntityId: String? = null

    @JvmField
    @SerializedName("openmrs_entity_parent")
    var openmrsEntityParent: String? = null
}