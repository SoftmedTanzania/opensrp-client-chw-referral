package org.smartregister.chw.referral.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by cozej4 on 2019-12-02.
 *
 * @author cozej4 https://github.com/cozej4
 */
class NeatFormOption : Serializable {

    @JvmField
    var name: String? = null

    @JvmField
    var text: String? = null

    @JvmField
    @SerializedName("meta_data")
    var neatFormMetaData: NeatFormMetaData? = null
}