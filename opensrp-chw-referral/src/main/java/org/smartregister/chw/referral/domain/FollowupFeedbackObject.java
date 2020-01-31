package org.smartregister.chw.referral.domain;

import com.google.gson.annotations.SerializedName;

import org.smartregister.chw.referral.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import java.io.Serializable;

public class FollowupFeedbackObject implements Serializable {
    private String id;

    @SerializedName("name_en")
    private String nameEn;

    @SerializedName("name_sw")
    private String nameSw;

    @SerializedName("is_active")
    private boolean isActive;

    public FollowupFeedbackObject(CommonPersonObjectClient client) {
        id = client.getColumnmaps().get(DBConstants.Key.ID);
        nameSw = client.getColumnmaps().get(DBConstants.Key.NAME_SW) != null ? client.getColumnmaps().get(DBConstants.Key.NAME_SW) : "";
        nameEn = client.getColumnmaps().get(DBConstants.Key.NAME_EN) != null ? client.getColumnmaps().get(DBConstants.Key.NAME_EN) : "";
        isActive = Boolean.parseBoolean(client.getColumnmaps().get(DBConstants.Key.IS_ACTIVE));
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
