package org.smartregister.chw.referral.model;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import org.smartregister.chw.referral.contract.BaseFollowupContract;
import org.smartregister.chw.referral.domain.FollowupFeedbackObject;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.domain.ReferralFollowupObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import timber.log.Timber;

public abstract class AbstractReferralFollowupModel extends ViewModel implements BaseFollowupContract.Model {
    public MemberObject memberObject;
    public ReferralFollowupObject referralFollowupObject;
    private ObservableField<String> clientConditionDuringTheVisit = new ObservableField<>("");
    private ObservableField<FollowupFeedbackObject> referralFollowupFeedback = new ObservableField<>();

    public ObservableField<FollowupFeedbackObject> getReferralFollowupFeedback() {
        return referralFollowupFeedback;
    }

    public void setReferralFollowupFeedback(FollowupFeedbackObject referralFollowupFeedback) {
        this.referralFollowupFeedback.set(referralFollowupFeedback);
        this.referralFollowupFeedback.notifyChange();
    }

    public ObservableField<String> getClientConditionDuringTheVisit() {
        return clientConditionDuringTheVisit;
    }

    public void setClientConditionDuringTheVisit(String clientConditionDuringTheVisit) {
        this.clientConditionDuringTheVisit.set(clientConditionDuringTheVisit);
        this.clientConditionDuringTheVisit.notifyChange();
    }

    public void saveDataToReferralFollowupObject() {
        try {
            referralFollowupObject = new ReferralFollowupObject();
            referralFollowupObject.setBaseEntityId(memberObject.getBaseEntityId());
            referralFollowupObject.setChwFollowupFeedback(referralFollowupFeedback.get().getNameEn());
            referralFollowupObject.setChwFollowupFeedbackId(referralFollowupFeedback.get().getId());
            referralFollowupObject.setOtherFollowupFeedbackInformation(clientConditionDuringTheVisit.get());


            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String followupDateString = formatter.format((Calendar.getInstance().getTime()));

            referralFollowupObject.setChwFollowupDate(followupDateString);
        } catch (NullPointerException e) {
            Timber.e(e);
        }
    }
}
