package org.smartregister.chw.referral.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;
import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.contract.BaseIssueReferralContract;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.model.AbstractIssueReferralModel;

import java.lang.ref.WeakReference;
import java.util.List;

import timber.log.Timber;

public class BaseIssueReferralPresenter implements BaseIssueReferralContract.Presenter, BaseIssueReferralContract.InteractorCallBack {
    protected BaseIssueReferralContract.Interactor interactor;
    private MemberObject MEMBER_OBJECT;
    private String baseEntityID;
    private WeakReference<BaseIssueReferralContract.View> viewReference;
    private Class<? extends AbstractIssueReferralModel> viewModelClass;

    public BaseIssueReferralPresenter(String baseEntityID, BaseIssueReferralContract.View view, Class<? extends AbstractIssueReferralModel> viewModelClass, BaseIssueReferralContract.Interactor interactor) {
        viewReference = new WeakReference<>(view);
        this.interactor = interactor;
        this.baseEntityID = baseEntityID;
        this.viewModelClass = viewModelClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends AbstractIssueReferralModel> getViewModel() {
        return viewModelClass;
    }

    @Override
    public String getMainCondition() {
        try {
            return "ec_family_member.base_entity_id = '" + MEMBER_OBJECT.getBaseEntityId() + "'";
        } catch (Exception e) {
            Timber.e(e);
        }
        return null;

    }

    @Override
    public String getMainTable() {
        return "ec_family_member";
    }

    @Override
    public void fillClientData(@Nullable MemberObject memberObject) {
        if (memberObject != null && getView() != null) {
            getView().setProfileViewWithData();
        }
    }

    @Override
    public void initializeMemberObject(MemberObject memberObject) {
        this.MEMBER_OBJECT = memberObject;
    }


    @Override
    public void saveForm(String jsonString) {
        try {
            interactor.saveRegistration(jsonString, this);
        } catch (Exception e) {
            Timber.e(Log.getStackTraceString(e));
        }
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
//        implement
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


    private BaseIssueReferralContract.View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }


    @Override
    public boolean validateValues(MemberObject memberObject) {
        String message = "Error: ";

        if (TextUtils.isEmpty(memberObject.getChwReferralHf()) || memberObject.getChwReferralHf() == null) {
            try {
                message = getView().getCurrentContext().getResources().getString(R.string.missing_facility);
                makeToast(message);
            } catch (Exception e) {
                Timber.e(e);
            }
            return false;

        } else if (TextUtils.isEmpty(memberObject.getChwReferralService()) || memberObject.getChwReferralService() == null) {
            try {
                message = getView().getCurrentContext().getResources().getString(R.string.missing_services);
                makeToast(message);
            } catch (Exception e) {
                Timber.e(e);
            }
            return false;

        } else
            return true;
    }

    private void makeToast(String message) {
        try {
            Toast.makeText(ReferralLibrary.getInstance().context().applicationContext(),
                    message,
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    public String getBaseEntityID() {
        return baseEntityID;
    }
}
