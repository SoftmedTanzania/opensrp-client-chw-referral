package org.smartregister.chw.referral.contract;

import android.content.Context;

import org.jetbrains.annotations.Nullable;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.domain.AlertStatus;

import java.util.Date;

public interface BaseReferralProfileContract {
    interface View extends InteractorCallBack {

        void setProfileViewWithData();

        void hideView();

        void setDueColor();

        void setOverDueColor();

        void openMedicalHistory();


        void openReferralHistory();

        void openUpcomingService();

        void openFamilyDueServices();

        void showProgressBar(boolean status);
    }

    interface Presenter {

        void fillProfileData(@Nullable MemberObject memberObject);

        @Nullable
        View getView();

        void refreshProfileBottom();

        void recordReferralFollowUp(Context context);

        String getIndicatorNameById(String indicatorId);
    }

    interface Interactor {

        void refreshProfileInfo(MemberObject memberObject, InteractorCallBack callback);

    }

    interface InteractorCallBack {

        void refreshMedicalHistory(boolean hasHistory);

        void refreshUpComingServicesStatus(String service, AlertStatus status, Date date);

        void refreshFamilyStatus(AlertStatus status);
    }
}