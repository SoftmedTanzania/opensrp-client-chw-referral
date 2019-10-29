package org.smartregister.chw.referral.interactor;

import android.support.annotation.VisibleForTesting;

import com.google.gson.Gson;

import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.contract.BaseIssueReferralContract;
import org.smartregister.chw.referral.util.AppExecutors;
import org.smartregister.chw.referral.util.JsonFormUtils;
import org.smartregister.chw.referral.util.Util;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.repository.AllSharedPreferences;

import java.util.Objects;
import java.util.UUID;

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
            // save it
            try {
                saveRegistration(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            appExecutors.mainThread().execute(callBack::onRegistrationSaved);
        };
        appExecutors.diskIO().execute(runnable);
    }

    @VisibleForTesting
    void saveRegistration(final String jsonString) throws Exception {

        AllSharedPreferences allSharedPreferences = ReferralLibrary.getInstance().context().allSharedPreferences();
        Event baseEvent = JsonFormUtils.processJsonForm(allSharedPreferences, jsonString);

        Objects.requireNonNull(baseEvent).setEventId(UUID.randomUUID().toString());

        Timber.i("Referral Event = %s", new Gson().toJson(baseEvent));

        Util.processEvent(allSharedPreferences, baseEvent);
    }

}
