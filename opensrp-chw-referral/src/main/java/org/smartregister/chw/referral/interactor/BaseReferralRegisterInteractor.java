package org.smartregister.chw.referral.interactor;

import android.support.annotation.VisibleForTesting;

import org.apache.commons.lang3.tuple.Triple;
import org.smartregister.chw.referral.contract.BaseReferralRegisterContract;
import org.smartregister.chw.referral.util.AppExecutors;

public class BaseReferralRegisterInteractor implements BaseReferralRegisterContract.Interactor {

    @VisibleForTesting
    BaseReferralRegisterInteractor(AppExecutors appExecutors) {
    }

    public BaseReferralRegisterInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration) {
//        implement
    }

    @Override
    public void getNextUniqueId(Triple<String, String, String> triple, BaseReferralRegisterContract.InteractorCallBack callBack) {
//        implement
    }


    @Override
    public void removeFamilyFromRegister(String closeFormJsonString, String providerId) {
//        implement
    }

}
