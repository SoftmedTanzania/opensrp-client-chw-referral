package org.smartregister.chw.referral.presenter;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.chw.referral.contract.BaseReferralProfileContract;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class BaseReferralProfilePresenterTest {
    @Mock
    private CommonPersonObjectClient commonPersonObjectClient = Mockito.mock(CommonPersonObjectClient.class);

    @Mock
    private BaseReferralProfileContract.View view = Mockito.mock(BaseReferralProfileContract.View.class);

    @Mock
    private BaseReferralProfileContract.Interactor interactor = Mockito.mock(BaseReferralProfileContract.Interactor.class);

    @Mock
    private MemberObject memberObject = new MemberObject(commonPersonObjectClient);

    private BaseReferralProfilePresenter profilePresenter = new BaseReferralProfilePresenter(view, interactor, memberObject);


    @Test
    public void fillProfileDataCallsSetProfileViewWithDataWhenPassedMemberObject() {
        profilePresenter.fillProfileData(memberObject);
        verify(view).setProfileViewWithData();
    }

    @Test
    public void fillProfileDataDoesntCallsSetProfileViewWithDataIfMemberObjectEmpty() {
        profilePresenter.fillProfileData(null);
        verify(view, never()).setProfileViewWithData();
    }

    @Test
    public void malariaTestDatePeriodIsLessThanSeven() {
        profilePresenter.recordReferralButton(4);
        verify(view).hideView();
    }

    @Test
    public void malariaTestDatePeriodIsBetweenSevenAndTen() {
        profilePresenter.recordReferralButton(7);
        verify(view).setDueColor();
    }

    @Test
    public void malariaTestDatePeriodGreaterThanTen() {
        profilePresenter.recordReferralButton(14);
        verify(view).setOverDueColor();
    }
}
