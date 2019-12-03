package org.smartregister.chw.referral.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cozej4 on 2019-12-02.
 *
 * @author cozej4 https://github.com/cozej4
 */
public class NeatFormMetaData implements Serializable {
    @SerializedName("openmrs_entity")
    public String openmrsEntity;

    @SerializedName("openmrs_entity_id")
    public String openmrsEntityId;

    @SerializedName("openmrs_entity_parent")
    public String openmrsEntityParent;
}
