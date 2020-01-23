package org.smartregister.chw.referral.contract;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.nerdstone.neatformcore.domain.model.NFormViewData;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.smartregister.chw.referral.domain.FollowupFeedbackObject;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.domain.ReferralFollowupObject;

import java.util.HashMap;
import java.util.List;

public interface BaseFollowupContract {

    interface View extends InteractorCallBack {
        BaseFollowupContract.Presenter presenter();

        void setProfileViewWithData();

        Context getCurrentContext();
    }


    interface Presenter {
        <T extends ViewModel & BaseFollowupContract.Model> Class<T> getViewModel();

        void fillProfileData(@Nullable MemberObject memberObject);

        @Nullable
        BaseFollowupContract.View getView();


        void initializeMemberObject(MemberObject memberObject);

        void saveForm(HashMap<String, NFormViewData> valuesHashMap, JSONObject jsonObject);
    }

    interface Model {
        List<FollowupFeedbackObject> getFollowupFeedbackList();

        JSONObject getFormWithValuesAsJson(String formName, String entityId,
                                           String currentLocationId, ReferralFollowupObject referralFollowupObject) throws Exception;
    }

    interface Interactor {

        void saveFollowup(String baseEntityId, HashMap<String, NFormViewData> valuesHashMap, JSONObject jsonObject, InteractorCallBack callBack);

    }

    interface InteractorCallBack {

        void onFollowupSaved();

    }

}