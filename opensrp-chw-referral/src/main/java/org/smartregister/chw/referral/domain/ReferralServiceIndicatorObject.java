package org.smartregister.chw.referral.domain;

import com.google.gson.annotations.SerializedName;

import org.smartregister.chw.referral.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import java.io.Serializable;

public class ReferralServiceIndicatorObject implements Serializable {
    @SerializedName("indicatorId")
    private String id;
    private String relationalId;

    @SerializedName("indicatorName")
    private String nameEn;

    @SerializedName("indicatorNameSw")
    private String nameSw;

    private boolean isActive;

    //Only used in selection of indicators from UI
    private boolean isChecked = false;

    public ReferralServiceIndicatorObject(CommonPersonObjectClient client) {
        id = client.getColumnmaps().get(DBConstants.Key.ID);
        relationalId = client.getColumnmaps().get(DBConstants.Key.RELATIONAL_ID);
        nameEn = client.getColumnmaps().get(DBConstants.Key.NAME_EN) != null ? client.getColumnmaps().get(DBConstants.Key.NAME_EN) : "";
        nameSw = client.getColumnmaps().get(DBConstants.Key.NAME_SW) != null ? client.getColumnmaps().get(DBConstants.Key.NAME_SW) : "";
        isActive = Boolean.parseBoolean(client.getColumnmaps().get(DBConstants.Key.IS_ACTIVE));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelationalId() {
        return relationalId;
    }

    public void setRelationalId(String relationalId) {
        this.relationalId = relationalId;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameSw() {
        return nameSw;
    }

    public void setNameSw(String nameSw) {
        this.nameSw = nameSw;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
