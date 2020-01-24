package org.smartregister.chw.referral.interactor;

import androidx.annotation.VisibleForTesting;

import com.google.gson.Gson;
import com.nerdstone.neatformcore.domain.model.NFormViewData;

import org.json.JSONObject;
import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.contract.BaseFollowupContract;
import org.smartregister.chw.referral.util.AppExecutors;
import org.smartregister.chw.referral.util.Constants;
import org.smartregister.chw.referral.util.JsonFormUtils;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.repository.AllSharedPreferences;

import java.util.HashMap;
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
    public void saveFollowup(String baseEntityId, HashMap<String, NFormViewData> valuesHashMap, JSONObject jsonObject, BaseFollowupContract.InteractorCallBack callBack) {
        try {
            saveFollowup(baseEntityId, valuesHashMap, jsonObject);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @VisibleForTesting
    void saveFollowup(String baseEntityId, HashMap<String, NFormViewData> valuesHashMap, JSONObject jsonObject) {

        AllSharedPreferences allSharedPreferences = ReferralLibrary.getInstance().context().allSharedPreferences();
        Event baseEvent = JsonFormUtils.processJsonForm(allSharedPreferences, baseEntityId,
                valuesHashMap, jsonObject, Constants.EVENT_TYPE.REGISTRATION)
                .getEvent();

        Objects.requireNonNull(baseEvent).setEventId(UUID.randomUUID().toString());

        Timber.i("Followup Event = %s", new Gson().toJson(baseEvent));

//        Util.processEvent(allSharedPreferences, baseEvent);
    }

}
