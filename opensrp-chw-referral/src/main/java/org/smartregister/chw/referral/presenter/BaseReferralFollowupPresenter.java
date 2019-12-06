package org.smartregister.chw.referral.presenter;

import android.util.Log;

import com.nerdstone.neatformcore.domain.model.NFormViewData;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.smartregister.chw.referral.contract.BaseFollowupContract;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.model.AbstractReferralFollowupModel;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import timber.log.Timber;

public class BaseReferralFollowupPresenter implements BaseFollowupContract.Presenter, BaseFollowupContract.InteractorCallBack {
    protected BaseFollowupContract.Interactor interactor;
    private WeakReference<BaseFollowupContract.View> viewReference;
    private Class<? extends AbstractReferralFollowupModel> viewModelClass;
    private MemberObject memberObject;
    private String baseEntityID;

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
    public void saveForm(HashMap<String, NFormViewData> valuesHashMap, JSONObject jsonObject) {
        try {
            interactor.saveFollowup(memberObject.getBaseEntityId(), valuesHashMap, jsonObject, this);
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
    public void fillProfileData(@Nullable MemberObject memberObject) {
        if (memberObject != null && getView() != null) {
            getView().setProfileViewWithData();
        }
    }

    @Override
    public void onFollowupSaved() {
        //Implement
    }

    @Override
    public void initializeMemberObject(MemberObject memberObject) {
        this.memberObject = memberObject;
    }

}
