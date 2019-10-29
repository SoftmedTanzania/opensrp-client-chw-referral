package org.smartregister.chw.referral.interactor;

import android.support.annotation.VisibleForTesting;

import org.smartregister.chw.referral.contract.BaseReferralProfileContract;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.util.AppExecutors;
import org.smartregister.domain.AlertStatus;

import java.util.Date;

public class BaseReferralProfileInteractor implements BaseReferralProfileContract.Interactor {
    protected AppExecutors appExecutors;

    @VisibleForTesting
    BaseReferralProfileInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseReferralProfileInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void refreshProfileInfo(MemberObject memberObject, BaseReferralProfileContract.InteractorCallBack callback) {
        Runnable runnable = () -> appExecutors.mainThread().execute(() -> {
            callback.refreshFamilyStatus(AlertStatus.normal);
            callback.refreshMedicalHistory(true);
            callback.refreshUpComingServicesStatus("Referral Visit", AlertStatus.normal, new Date());
        });
        appExecutors.diskIO().execute(runnable);
    }
}
