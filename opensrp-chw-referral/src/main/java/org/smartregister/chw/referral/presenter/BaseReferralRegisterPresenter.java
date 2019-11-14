package org.smartregister.chw.referral.presenter;

import android.support.annotation.VisibleForTesting;

import org.apache.commons.lang3.tuple.Triple;
import org.smartregister.chw.referral.contract.BaseReferralRegisterContract;

import java.lang.ref.WeakReference;
import java.util.List;

public class BaseReferralRegisterPresenter implements BaseReferralRegisterContract.Presenter, BaseReferralRegisterContract.InteractorCallBack {
    protected WeakReference<BaseReferralRegisterContract.View> viewReference;
    protected BaseReferralRegisterContract.Model model;

    public BaseReferralRegisterPresenter(BaseReferralRegisterContract.View view, BaseReferralRegisterContract.Model model) {
        viewReference = new WeakReference<>(view);
        this.model = model;
    }

    @Override
    public void saveLanguage(String language) {
//        implement
    }

    @Override
    public void closeFamilyRecord(String jsonString) {
//        implement
    }

    @Override
    public void onUniqueIdFetched(Triple<String, String, String> triple, String entityId) {
//        implement
    }

    @Override
    public void onNoUniqueId() {
//        implement
    }

    @Override
    public void onRegistrationSaved() {
        getView().hideProgressDialog();
    }

    @Override
    public void registerViewConfigurations(List<String> list) {
//        implement
    }

    @Override
    public void unregisterViewConfiguration(List<String> list) {
//        implement
    }

    @Override
    public void onDestroy(boolean b) {
//        implement
    }

    @Override
    public void updateInitials() {
//        implement
    }

    @VisibleForTesting
    BaseReferralRegisterContract.View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }
}
