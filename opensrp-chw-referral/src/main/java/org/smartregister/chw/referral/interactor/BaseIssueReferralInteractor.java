package org.smartregister.chw.referral.interactor;

import androidx.annotation.VisibleForTesting;

import com.google.gson.Gson;
import com.nerdstone.neatformcore.domain.model.NFormViewData;

import org.json.JSONObject;
import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.contract.BaseIssueReferralContract;
import org.smartregister.chw.referral.domain.ReferralServiceObject;
import org.smartregister.chw.referral.util.AppExecutors;
import org.smartregister.chw.referral.util.Constants;
import org.smartregister.chw.referral.util.JsonFormUtils;
import org.smartregister.chw.referral.util.Util;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.repository.AllSharedPreferences;

import java.util.HashMap;
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
    public void saveRegistration(String baseEntityId, HashMap<String, NFormViewData> valuesHashMap,JSONObject jsonObject, final BaseIssueReferralContract.InteractorCallBack callBack) {

        Runnable runnable = () -> {
            // save it
            try {
                saveRegistration(baseEntityId,valuesHashMap,jsonObject);
            } catch (Exception e) {
                Timber.e(e);
            }

            appExecutors.mainThread().execute(callBack::onRegistrationSaved);
        };
        appExecutors.diskIO().execute(runnable);
    }

    @VisibleForTesting
    void saveRegistration(String baseEntityId, HashMap<String, NFormViewData> valuesHashMap, JSONObject jsonObject) throws Exception {

        AllSharedPreferences allSharedPreferences = ReferralLibrary.getInstance().context().allSharedPreferences();
        Event baseEvent = JsonFormUtils.processJsonForm(allSharedPreferences,baseEntityId,valuesHashMap, jsonObject,Constants.EVENT_TYPE.REGISTRATION);

        Objects.requireNonNull(baseEvent).setEventId(UUID.randomUUID().toString());

        Timber.i("Referral Event = %s", new Gson().toJson(baseEvent));

        Util.processEvent(allSharedPreferences, baseEvent);
    }

}
