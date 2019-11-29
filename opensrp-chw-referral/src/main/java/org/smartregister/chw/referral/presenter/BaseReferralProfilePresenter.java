package org.smartregister.chw.referral.presenter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.widget.Toast;

import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.contract.BaseReferralProfileContract;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.repository.ReferralServiceIndicatorRepository;

import java.lang.ref.WeakReference;


public class BaseReferralProfilePresenter implements BaseReferralProfileContract.Presenter {
    protected WeakReference<BaseReferralProfileContract.View> view;
    protected MemberObject memberObject;
    protected BaseReferralProfileContract.Interactor interactor;
    protected Context context;

    public BaseReferralProfilePresenter(BaseReferralProfileContract.View view, BaseReferralProfileContract.Interactor interactor, MemberObject memberObject) {
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
    public void recordReferralButton(int days_from_referral_date) {
        if (getView() == null)
            return;

        if (days_from_referral_date < 7 || days_from_referral_date > 14) {
            getView().hideView();
        } else if (days_from_referral_date < 10) {
            getView().setDueColor();
        } else
            getView().setOverDueColor();
    }

    @Override
    public void recordReferralFollowUp(Context context) {
        Toast.makeText(context, R.string.record_followup, Toast.LENGTH_SHORT).show();
    }

    @Override
    @Nullable
    public BaseReferralProfileContract.View getView() {
        if (view != null && view.get() != null)
            return view.get();

        return null;
    }

    @Override
    public void refreshProfileBottom() {
        interactor.refreshProfileInfo(memberObject, getView());
    }

    @Override
    public String getIndicatorNameById(String indicatorId) {
        ReferralServiceIndicatorRepository repository = new ReferralServiceIndicatorRepository(ReferralLibrary.getInstance().getRepository());

        //TODO coze: make use of locale to check which version of the value of the indicator object are to be returned i.e name_en or name_sw
        return repository.getServiceIndicatorById(indicatorId).getNameEn();
    }
}
