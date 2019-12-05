package org.smartregister.chw.referral.contract;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.smartregister.chw.referral.domain.FollowupFeedbackObject;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.domain.ReferralFollowupObject;

import java.util.List;

public interface BaseFollowupContract {

    interface View extends InteractorCallBack {
        BaseFollowupContract.Presenter presenter();

        void setProfileViewWithData();

        Context getCurrentContext();
    }


    interface Presenter {
        <T extends ViewModel & BaseFollowupContract.Model> Class<T> getViewModel();

        boolean validateValues(ReferralFollowupObject referralFollowupObject);

        void fillProfileData(@Nullable MemberObject memberObject);

        void recordReferralFollowUp(Context context);

        @Nullable
        BaseFollowupContract.View getView();

        void saveForm(String jsonString);
    }

    interface Model {
        List<FollowupFeedbackObject> getFollowupFeedbackList();

        JSONObject getFormWithValuesAsJson(String formName, String entityId,
                                           String currentLocationId, ReferralFollowupObject referralFollowupObject) throws Exception;
    }

    interface Interactor {

        void saveFollowup(String jsonString, InteractorCallBack callBack);

    }

    interface InteractorCallBack {

        void onFollowupSaved();

    }

}