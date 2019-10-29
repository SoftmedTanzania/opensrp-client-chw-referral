package org.smartregister.chw.referral.interactor;

import android.support.annotation.VisibleForTesting;

import org.smartregister.chw.referral.contract.BaseFollowupContract;
import org.smartregister.chw.referral.util.AppExecutors;

public class BaseFollowupInteractor implements BaseFollowupContract.Interactor {
    protected AppExecutors appExecutors;

    @VisibleForTesting
    BaseFollowupInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseFollowupInteractor() {
        this(new AppExecutors());
    }


    @Override
    public void saveFollowup(String jsonString, BaseFollowupContract.InteractorCallBack callBack) {

        Runnable runnable = () -> appExecutors.mainThread().execute(() -> {
            callBack.onRegistrationSaved(jsonString);
        });
        appExecutors.diskIO().execute(runnable);

    }
}
