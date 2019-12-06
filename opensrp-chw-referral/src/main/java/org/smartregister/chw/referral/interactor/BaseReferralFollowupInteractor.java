package org.smartregister.chw.referral.interactor;

import androidx.annotation.VisibleForTesting;

import com.google.gson.Gson;

import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.contract.BaseFollowupContract;
import org.smartregister.chw.referral.util.AppExecutors;
import org.smartregister.chw.referral.util.JsonFormUtils;
import org.smartregister.chw.referral.util.Util;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.repository.AllSharedPreferences;

import java.util.Objects;
import java.util.UUID;

import timber.log.Timber;

public class BaseReferralFollowupInteractor implements BaseFollowupContract.Interactor {
    protected AppExecutors appExecutors;

    @VisibleForTesting
    private BaseReferralFollowupInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseReferralFollowupInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void saveFollowup(final String jsonString, final BaseFollowupContract.InteractorCallBack callBack) {
        Runnable runnable = () -> {
            try {
                saveFollowupJSON(jsonString);
            } catch (Exception e) {
                Timber.e(e);
            }
            appExecutors.mainThread().execute(callBack::onFollowupSaved);
        };
        appExecutors.diskIO().execute(runnable);
    }

    @VisibleForTesting
    void saveFollowupJSON(final String jsonString) throws Exception {

//        AllSharedPreferences allSharedPreferences = ReferralLibrary.getInstance().context().allSharedPreferences();
//        Event baseEvent = JsonFormUtils.processJsonForm(allSharedPreferences, jsonString);
//
//        Objects.requireNonNull(baseEvent).setEventId(UUID.randomUUID().toString());
//
//        Timber.i("Referral Followup Event = %s", new Gson().toJson(baseEvent));
//
//        Util.processEvent(allSharedPreferences, baseEvent);
    }

}
