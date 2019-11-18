package org.smartregister.chw.referral.presenter;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.chw.referral.contract.BaseFollowupContract;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.domain.ReferralFollowupObject;
import org.smartregister.chw.referral.model.BaseReferralFollowupModel;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class BaseReferralFollowupPresenterTest {
    @Mock
    private CommonPersonObjectClient commonPersonObjectClient = Mockito.mock(CommonPersonObjectClient.class);

    @Mock
    private BaseFollowupContract.View view = Mockito.mock(BaseFollowupContract.View.class);

    @Mock
    private BaseFollowupContract.Interactor interactor = Mockito.mock(BaseFollowupContract.Interactor.class);


    @Mock
    private MemberObject memberObject = new MemberObject(commonPersonObjectClient);

    private BaseReferralFollowupPresenter followupPresenter = new BaseReferralFollowupPresenter(view, BaseReferralFollowupModel.class, interactor);


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

    @Test
    public void saveForm() {
        followupPresenter.saveForm(null);
        verify(interactor).saveFollowup(null, followupPresenter);
    }

    @Test
    public void validateValues() {
        ReferralFollowupObject referralFollowupObject = new ReferralFollowupObject();
        Assert.assertFalse(followupPresenter.validateValues(referralFollowupObject));

        referralFollowupObject.setOtherFollowupFeedbackInformation("client is doing well");
        Assert.assertFalse(followupPresenter.validateValues(referralFollowupObject));

        referralFollowupObject.setChwFollowupFeedback("He forgot the appointment");
        Assert.assertTrue(followupPresenter.validateValues(referralFollowupObject));
    }
}

