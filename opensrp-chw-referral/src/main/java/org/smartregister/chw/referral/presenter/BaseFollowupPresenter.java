package org.smartregister.chw.referral.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.contract.BaseFollowupContract;
import org.smartregister.chw.referral.domain.MemberObject;

import java.lang.ref.WeakReference;


public class BaseFollowupPresenter implements BaseFollowupContract.Presenter {
    protected WeakReference<BaseFollowupContract.View> view;
    protected MemberObject memberObject;
    protected BaseFollowupContract.Interactor interactor;
    protected Context context;

    public BaseFollowupPresenter(BaseFollowupContract.View view, BaseFollowupContract.Interactor interactor, MemberObject memberObject) {
        this.view = new WeakReference<>(view);
        this.memberObject = memberObject;
        this.interactor = interactor;
    }

    @Override
    public void fillProfileData(MemberObject memberObject) {
        if (memberObject != null && getView() != null) {
            getView().setProfileViewWithData();
        }
    }


    @Override
    public void recordReferralFollowUp(Context context) {
        Toast.makeText(context, R.string.record_followup, Toast.LENGTH_SHORT).show();
    }

    @Override
    @Nullable
    public BaseFollowupContract.View getView() {
        if (view != null && view.get() != null)
            return view.get();

        return null;
    }
}
