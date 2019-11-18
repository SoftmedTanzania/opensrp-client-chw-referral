package org.smartregister.chw.referral.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.jetbrains.annotations.Nullable;
import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.contract.BaseFollowupContract;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.domain.ReferralFollowupObject;
import org.smartregister.chw.referral.model.AbstractReferralFollowupModel;
import org.smartregister.util.Utils;

import java.lang.ref.WeakReference;

import timber.log.Timber;

public class BaseReferralFollowupPresenter implements BaseFollowupContract.Presenter, BaseFollowupContract.InteractorCallBack {
    protected BaseFollowupContract.Interactor interactor;
    private WeakReference<BaseFollowupContract.View> viewReference;
    private Class<? extends AbstractReferralFollowupModel> viewModelClass;

    public BaseReferralFollowupPresenter(BaseFollowupContract.View view, Class<? extends AbstractReferralFollowupModel> viewModelClass, BaseFollowupContract.Interactor interactor) {
        viewReference = new WeakReference<>(view);
        this.interactor = interactor;
        this.viewModelClass = viewModelClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends AbstractReferralFollowupModel> getViewModel() {
        return viewModelClass;
    }

    @Override
    public void recordReferralFollowUp(Context context) {
        //implement
    }


    @Override
    public void saveForm(String jsonString) {
        try {
            interactor.saveFollowup(jsonString, this);
        } catch (Exception e) {
            Timber.e(Log.getStackTraceString(e));
        }
    }


    public BaseFollowupContract.View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }


    @Override
    public boolean validateValues(ReferralFollowupObject referralFollowupObject) {
        String message;

        if (TextUtils.isEmpty(referralFollowupObject.getChwFollowupFeedback()) || referralFollowupObject.getChwFollowupFeedback() == null) {
            try {
                message = getView().getCurrentContext().getResources().getString(R.string.missing_feedback);
                Utils.showToast(ReferralLibrary.getInstance().context().applicationContext(), message);

            } catch (Exception e) {
                Timber.e(e);
            }
            return false;

        } else
            return true;
    }

    @Override
    public void fillProfileData(@Nullable MemberObject memberObject) {
        if (memberObject != null && getView() != null) {
            getView().setProfileViewWithData();
        }
    }

    @Override
    public void onFollowupSaved() {
        //Implement
    }
}
