package org.smartregister.chw.referral.presenter;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.chw.referral.contract.BaseFollowupContract;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.presenter.BaseFollowupPresenter;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class BaseFollowupPresenterTest {
    @Mock
    private CommonPersonObjectClient commonPersonObjectClient = Mockito.mock(CommonPersonObjectClient.class);

    @Mock
    private BaseFollowupContract.View view = Mockito.mock(BaseFollowupContract.View.class);

    @Mock
    private BaseFollowupContract.Interactor interactor = Mockito.mock(BaseFollowupContract.Interactor.class);

    @Mock
    private MemberObject memberObject = new MemberObject(commonPersonObjectClient);

    private BaseFollowupPresenter followupPresenter = new BaseFollowupPresenter(view, interactor, memberObject);


    @Test
    public void fillProfileDataCallsSetProfileViewWithDataWhenPassedMemberObject() {
        followupPresenter.fillProfileData(memberObject);
        verify(view).setProfileViewWithData();
    }

    @Test
    public void fillProfileDataDoesntCallsSetProfileViewWithDataIfMemberObjectEmpty() {
        followupPresenter.fillProfileData(null);
        verify(view, never()).setProfileViewWithData();
    }
}
