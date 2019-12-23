package org.smartregister.chw.referral.domain;

import org.smartregister.clientandeventmodel.Event;

public class ReferralEvent extends Event {
    private String groupId;
    private String referralDescription;
    private String focus;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getReferralDescription() {
        return referralDescription;
    }

    public void setReferralDescription(String referralDescription) {
        this.referralDescription = referralDescription;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }
}
