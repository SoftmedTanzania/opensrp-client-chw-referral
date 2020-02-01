package org.smartregister.chw.referral.presenter;

import com.nerdstone.neatformcore.domain.model.NFormViewData;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.referral.contract.BaseFollowupContract;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.model.BaseReferralFollowupModel;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import java.util.HashMap;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class BaseReferralFollowupPresenterTest {
    @Mock
    private CommonPersonObjectClient commonPersonObjectClient;

    @Mock
    private BaseFollowupContract.View view;

    @Mock
    private BaseFollowupContract.Interactor interactor;

    private MemberObject memberObject;

    private BaseReferralFollowupPresenter followupPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        memberObject = new MemberObject(commonPersonObjectClient);
        memberObject.baseEntityId = "sampleBaseEntityID";
        followupPresenter = new BaseReferralFollowupPresenter(view, BaseReferralFollowupModel.class, interactor);
        followupPresenter.initializeMemberObject(memberObject);
    }

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
        HashMap<String, NFormViewData> viewData = new HashMap<>();
        JSONObject jsonForm = new JSONObject();
        followupPresenter.saveForm(viewData, jsonForm);
        verify(interactor).saveFollowup("sampleBaseEntityID", viewData, jsonForm, followupPresenter);
    }
}

