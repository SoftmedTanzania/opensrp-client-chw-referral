package org.smartregister.chw.referral.presenter;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.chw.referral.contract.BaseIssueReferralContract;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.model.BaseIssueReferralModel;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class BaseIssueReferralPresenterTest {
    @Mock
    protected CommonPersonObjectClient client;
    @Mock
    private CommonPersonObjectClient commonPersonObjectClient = Mockito.mock(CommonPersonObjectClient.class);
    @Mock
    private BaseIssueReferralContract.View view = Mockito.mock(BaseIssueReferralContract.View.class);
    @Mock
    private BaseIssueReferralContract.Interactor interactor = Mockito.mock(BaseIssueReferralContract.Interactor.class);
    @Mock
    private BaseIssueReferralModel model = Mockito.mock(BaseIssueReferralModel.class);
    @Mock
    private MemberObject memberObject = new MemberObject(commonPersonObjectClient);

    private BaseIssueReferralPresenter issueReferralPresenter = new BaseIssueReferralPresenter("sampleBaseEntityID", view, model.getClass(), interactor);


    @Test
    public void getViewModel() {
        Assert.assertEquals(issueReferralPresenter.getViewModel(), model.getClass());
    }

    @Test
    public void getMainCondition() {
        Assert.assertNull(issueReferralPresenter.getMainCondition());

        issueReferralPresenter.initializeMemberObject(memberObject);
        Assert.assertNotNull(issueReferralPresenter.getMainCondition());
    }

    @Test
    public void getMainTable() {
        Assert.assertNotNull(issueReferralPresenter.getMainTable());
    }

    @Test
    public void fillClientData() {
        issueReferralPresenter.fillClientData(memberObject);
        verify(view).setProfileViewWithData();
    }

    @Test
    public void fillClientDataWithNull() {
        issueReferralPresenter.fillClientData(null);
        verify(view, never()).setProfileViewWithData();
    }

    @Test
    public void initializeMemberObject() {
        issueReferralPresenter.initializeMemberObject(memberObject);
        Assert.assertNotNull(issueReferralPresenter.getMainCondition());
    }


    @Test
    public void saveForm() {
        issueReferralPresenter.saveForm("sample string");
        verify(interactor).saveRegistration("sample string", issueReferralPresenter);
    }

    @Test
    public void validateValues() {
        MemberObject memberObject = new MemberObject(commonPersonObjectClient);
        Assert.assertFalse(issueReferralPresenter.validateValues(memberObject));

        memberObject.setChwReferralHf("");
        Assert.assertFalse(issueReferralPresenter.validateValues(memberObject));

        memberObject.setChwReferralHf("Test Facility A");
        Assert.assertFalse(issueReferralPresenter.validateValues(memberObject));


        memberObject.setChwReferralService(null);
        Assert.assertFalse(issueReferralPresenter.validateValues(memberObject));

        memberObject.setChwReferralService("HIV");
        Assert.assertTrue(issueReferralPresenter.validateValues(memberObject));
    }

    @Test
    public void getBaseEntityID() {
        BaseIssueReferralPresenter mPresenter = new BaseIssueReferralPresenter("sampleBaseEntityID", view, model.getClass(), interactor);
        Assert.assertEquals("sampleBaseEntityID", mPresenter.getBaseEntityID());
    }


}
