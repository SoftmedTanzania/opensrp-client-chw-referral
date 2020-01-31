package org.smartregister.chw.referral.domain;

import com.google.gson.annotations.SerializedName;

import org.smartregister.chw.referral.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import java.io.Serializable;

public class ReferralFollowupObject implements Serializable {
    private String relationalId;
    private String details;
    private String baseEntityId;

    @SerializedName("chw_followup_feedback")
    private String chwFollowupFeedback;

    @SerializedName("chw_followup_feedback_id")
    private String chwFollowupFeedbackId;

    @SerializedName("other_followup_feedback_information")
    private String otherFollowupFeedbackInformation;

    @SerializedName("chw_followup_date")
    private String chwFollowupDate;
    private boolean isClosed;

    public ReferralFollowupObject() {
    }

    public ReferralFollowupObject(CommonPersonObjectClient client) {
        baseEntityId = client.getColumnmaps().get(DBConstants.Key.BASE_ENTITY_ID);
        relationalId = client.getColumnmaps().get(DBConstants.Key.RELATIONAL_ID);
        chwFollowupFeedbackId = client.getColumnmaps().get(DBConstants.Key.CHW_FOLLOWUP_FEEDBACK_ID);
        otherFollowupFeedbackInformation = client.getColumnmaps().get(DBConstants.Key.OTHER_FOLLOWUP_FEEDBACK_INFORMATION);
        chwFollowupDate = client.getColumnmaps().get(DBConstants.Key.CHW_FOLLOWUP_DATE);

        details = client.getColumnmaps().get(DBConstants.Key.DETAILS);
        isClosed = Boolean.parseBoolean(client.getColumnmaps().get(DBConstants.Key.IS_CLOSED));
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getBaseEntityId() {
        return baseEntityId;
    }

    public void setBaseEntityId(String baseEntityId) {
        this.baseEntityId = baseEntityId;
    }

    public String getChwFollowupFeedback() {
        return chwFollowupFeedback;
    }

    public void setChwFollowupFeedback(String chwFollowupFeedback) {
        this.chwFollowupFeedback = chwFollowupFeedback;
    }

    public String getOtherFollowupFeedbackInformation() {
        return otherFollowupFeedbackInformation;
    }

    public void setOtherFollowupFeedbackInformation(String otherFollowupFeedbackInformation) {
        this.otherFollowupFeedbackInformation = otherFollowupFeedbackInformation;
    }

    public boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(boolean closed) {
        isClosed = closed;
    }

    public String getRelationalId() {
        return relationalId;
    }

    public void setRelationalId(String relationalid) {
        this.relationalId = relationalid;
    }

    public String getChwFollowupDate() {
        return chwFollowupDate;
    }

    public void setChwFollowupDate(String chwFollowupDate) {
        this.chwFollowupDate = chwFollowupDate;
    }

    public String getChwFollowupFeedbackId() {
        return chwFollowupFeedbackId;
    }

    public void setChwFollowupFeedbackId(String chwFollowupFeedbackId) {
        this.chwFollowupFeedbackId = chwFollowupFeedbackId;
    }
}
