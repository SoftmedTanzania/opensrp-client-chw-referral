package org.smartregister.chw.referral.interactor;

import androidx.annotation.VisibleForTesting;

import com.google.gson.Gson;
import com.nerdstone.neatformcore.domain.model.NFormViewData;

import org.json.JSONObject;
import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.contract.BaseIssueReferralContract;
import org.smartregister.chw.referral.domain.ReferralTask;
import org.smartregister.chw.referral.util.AppExecutors;
import org.smartregister.chw.referral.util.Constants;
import org.smartregister.chw.referral.util.JsonFormUtils;
import org.smartregister.chw.referral.util.ReferralUtil;
import org.smartregister.chw.referral.util.Util;
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
    public void saveRegistration(String baseEntityId, HashMap<String, NFormViewData> valuesHashMap, JSONObject jsonObject, final BaseIssueReferralContract.InteractorCallBack callBack) {

        Runnable runnable = () -> {
            // save it
            try {
                saveRegistration(baseEntityId, valuesHashMap, jsonObject);
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
        ReferralTask referralTask = JsonFormUtils.processJsonForm(allSharedPreferences, baseEntityId, valuesHashMap, jsonObject, Constants.EVENT_TYPE.REGISTRATION);
        referralTask.setGroupId("718b2864-7d6a-44c8-b5b6-bb375f82654e"); //TODO obtain this from locationsMap from [ReferralMetadata] i.e use the facility value retrieved from the spinner
        referralTask.setFocus("Harmonized Referral"); //TODO use the referral type as focus
        referralTask.setReferralDescription("Testing Harmonization referral"); //TODO use the referral problems/danger signs instead should be comma separated values in one string e.g. Coughing, Heavy breathing
        Objects.requireNonNull(referralTask.getEvent()).setEventId(UUID.randomUUID().toString());

        Timber.i("Referral Event = %s", new Gson().toJson(referralTask));

        Util.processEvent(allSharedPreferences, referralTask.getEvent());

        ReferralUtil.createReferralTask(referralTask, allSharedPreferences);
    }

}
