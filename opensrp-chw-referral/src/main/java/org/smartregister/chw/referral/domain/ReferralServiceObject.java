package org.smartregister.chw.referral.domain;

import org.smartregister.chw.referral.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import java.io.Serializable;

public class ReferralServiceObject implements Serializable {
    private String id;
    private String nameEn;
    private String nameSw;
    private String identifier;
    private boolean isActive;

    public ReferralServiceObject(CommonPersonObjectClient client) {
        id = client.getColumnmaps().get(DBConstants.KEY.ID);
        nameEn = client.getColumnmaps().get(DBConstants.KEY.NAME_EN) != null ? client.getColumnmaps().get(DBConstants.KEY.NAME_EN) : "";
        nameSw = client.getColumnmaps().get(DBConstants.KEY.NAME_SW) != null ? client.getColumnmaps().get(DBConstants.KEY.NAME_SW) : "";
        identifier = client.getColumnmaps().get(DBConstants.KEY.REFERRAL_SERVICE_IDENTIFIER) != null ? client.getColumnmaps().get(DBConstants.KEY.REFERRAL_SERVICE_IDENTIFIER) : "";
        isActive = Boolean.parseBoolean(client.getColumnmaps().get(DBConstants.KEY.IS_ACTIVE));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
