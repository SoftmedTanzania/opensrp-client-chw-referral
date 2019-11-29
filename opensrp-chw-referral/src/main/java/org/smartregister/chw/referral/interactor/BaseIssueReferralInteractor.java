package org.smartregister.chw.referral.interactor;

import android.support.annotation.VisibleForTesting;

import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.contract.BaseIssueReferralContract;
import org.smartregister.chw.referral.util.AppExecutors;
import org.smartregister.chw.referral.util.ReferralUtil;
import org.smartregister.location.helper.LocationHelper;

import java.util.ArrayList;
import java.util.Arrays;

import timber.log.Timber;

public class BaseIssueReferralInteractor implements BaseIssueReferralContract.Interactor {

    private AppExecutors appExecutors;

    @VisibleForTesting
    private BaseIssueReferralInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseIssueReferralInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration) {
//        implement
    }

    @Override
    public void saveRegistration(final String jsonString, final BaseIssueReferralContract.InteractorCallBack callBack) {

        Runnable runnable = () -> {
            try {
                saveRegistration(jsonString);
            } catch (Exception e) {
                Timber.e(e);
            }

            appExecutors.mainThread().execute(callBack::onRegistrationSaved);
        };
        appExecutors.diskIO().execute(runnable);
    }

    @VisibleForTesting
    private void saveRegistration(final String jsonString) throws Exception {
        ReferralUtil.createReferralEventAndTask(ReferralLibrary.getInstance().context().allSharedPreferences(),
                jsonString, Arrays.asList("MOH Jhpiego Facility Name" , "Village"));//TODO remove hard corded allowed locations location
    }

}
