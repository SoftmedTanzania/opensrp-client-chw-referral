package org.smartregister.chw.referral.model;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.google.gson.Gson;

import org.smartregister.chw.referral.contract.BaseIssueReferralContract;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.domain.ReferralServiceIndicatorObject;
import org.smartregister.chw.referral.domain.ReferralServiceObject;
import org.smartregister.chw.referral.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import timber.log.Timber;

public abstract class AbstractIssueReferralModel extends ViewModel implements BaseIssueReferralContract.Model {
    public MemberObject memberObject;
    public ObservableField<ReferralServiceObject> selectedReferralService = new ObservableField<>();
    public List<ReferralServiceIndicatorObject> referralServiceIndicators;
    public long appointmentDateTimestamp;
    public String referralFacilityUuid;

    private ObservableField<String> referralReason = new ObservableField<>("");
    private ObservableField<String> appointmentDate = new ObservableField<>("");
    private ObservableField<Boolean> isEmergency = new ObservableField<>(false);

    public ObservableField<String> getReferralReason() {
        return referralReason;
    }

    public void setReferralReason(String referralReason) {
        this.referralReason.set(referralReason);
        this.referralReason.notifyChange();
    }

    public ObservableField<String> getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate.set(appointmentDate);
        this.appointmentDate.notifyChange();
    }

    public ObservableField<Boolean> getIsEmergency() {
        return isEmergency;
    }

    public void setIsEmergency(Boolean isEmergency) {
        this.isEmergency.set(isEmergency);
        this.isEmergency.notifyChange();
    }

    public void saveDataToMemberObject() {
        try {
            memberObject.setReferralStatus(Constants.REFERRAL_STATUS.PENDING);
            memberObject.setChwReferralReason(referralReason.get());
            memberObject.setEmergencyReferral(Objects.requireNonNull(isEmergency.get()));
            memberObject.setChwReferralService(Objects.requireNonNull(selectedReferralService.get()).getId());
            memberObject.setChwReferralHf(referralFacilityUuid);

            memberObject.setReferralAppointmentDate(appointmentDate.get());

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String referralDateString = formatter.format((Calendar.getInstance().getTime()));

            memberObject.setChwReferralDate(referralDateString);
            memberObject.setReferralType(Constants.REFERRAL_TYPE.COMMUNITY_TO_FACILITY_REFERRAL);

            List<String> selectedIndicators = new ArrayList<>();
            try {
                for (ReferralServiceIndicatorObject referralServiceIndicatorObject : referralServiceIndicators) {
                    if (referralServiceIndicatorObject.isChecked())
                        selectedIndicators.add(referralServiceIndicatorObject.getId());
                }
            } catch (NullPointerException e) {
                Timber.e(e);
            }

            memberObject.setDangerSignsIndicatorIds(new Gson().toJson(selectedIndicators));
        } catch (NullPointerException e) {
            Timber.e(e);
        }
    }
}
