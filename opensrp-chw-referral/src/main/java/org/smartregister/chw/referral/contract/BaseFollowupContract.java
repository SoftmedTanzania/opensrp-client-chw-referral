package org.smartregister.chw.referral.contract;

import android.content.Context;

import org.jetbrains.annotations.Nullable;
import org.smartregister.chw.referral.domain.MemberObject;

public interface BaseFollowupContract {

    interface View extends InteractorCallBack {

        void setProfileViewWithData();
    }

    interface Presenter {

        void fillProfileData(@Nullable MemberObject memberObject);

        void recordReferralFollowUp(Context context);

        @Nullable
        BaseFollowupContract.View getView();
    }

    interface Interactor {

        void saveFollowup(String jsonString, InteractorCallBack callBack);

    }

    interface InteractorCallBack {

        void onRegistrationSaved(String jsonString);

    }

}