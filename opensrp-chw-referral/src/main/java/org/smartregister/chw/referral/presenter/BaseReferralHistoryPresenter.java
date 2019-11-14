package org.smartregister.chw.referral.presenter;

import org.jetbrains.annotations.Nullable;
import org.smartregister.chw.referral.contract.BaseReferralHistoryContract;
import org.smartregister.chw.referral.domain.MemberObject;

import java.lang.ref.WeakReference;

import timber.log.Timber;

public class BaseReferralHistoryPresenter implements BaseReferralHistoryContract.Presenter {
    private MemberObject MEMBER_OBJECT;
    private WeakReference<BaseReferralHistoryContract.View> viewReference;

    public BaseReferralHistoryPresenter(MemberObject MEMBER_OBJECT, BaseReferralHistoryContract.View view) {
        viewReference = new WeakReference<>(view);
        this.MEMBER_OBJECT = MEMBER_OBJECT;
    }

    @Override
    public String getMainCondition() {
        try {
            return "ec_referral.base_entity_id = '" + MEMBER_OBJECT.getBaseEntityId() + "'";
        } catch (Exception e) {
            Timber.e(e);
        }
        return null;

    }

    @Override
    public String getMainTable() {
        return "ec_referral";
    }

    @Override
    public BaseReferralHistoryContract.View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }


    @Override
    public void fillClientData(@Nullable MemberObject memberObject) {
        if (memberObject != null && getView() != null) {
            getView().setProfileViewWithData(memberObject);
        }
    }


}
