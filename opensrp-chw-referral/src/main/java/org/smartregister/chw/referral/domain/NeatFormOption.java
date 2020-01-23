package org.smartregister.chw.referral.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cozej4 on 2019-12-02.
 *
 * @author cozej4 https://github.com/cozej4
 */
public class NeatFormOption implements Serializable {
    public String name;
    public String text;

    @SerializedName("meta_data")
    public NeatFormMetaData neatFormMetaData;
}
