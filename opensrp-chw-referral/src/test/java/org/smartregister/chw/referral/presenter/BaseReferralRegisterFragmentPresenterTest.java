package org.smartregister.chw.referral.presenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.referral.contract.BaseReferralRegisterFragmentContract;

import static org.mockito.Mockito.verify;

public class BaseReferralRegisterFragmentPresenterTest {
    @Mock
    protected BaseReferralRegisterFragmentContract.View view;
    @Mock
    protected BaseReferralRegisterFragmentContract.Model model;

    @Mock
    private BaseReferralRegisterFragmentPresenter baseReferralRegisterFragmentPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        baseReferralRegisterFragmentPresenter = new BaseReferralRegisterFragmentPresenter(view, model, "");
    }

    @Test
    public void assertNotNull() {
        Assert.assertNotNull(baseReferralRegisterFragmentPresenter);
    }

    @Test
    public void getMainCondition() {
        Assert.assertEquals("", baseReferralRegisterFragmentPresenter.getMainCondition());
    }

    @Test
    public void getDefaultSortQuery() {
        Assert.assertEquals("ec_referral.referral_date DESC ", baseReferralRegisterFragmentPresenter.getDefaultSortQuery());
    }

    @Test
    public void initializeQueries() {
        baseReferralRegisterFragmentPresenter.initializeQueries("");
        verify(baseReferralRegisterFragmentPresenter.getView()).countExecute();
        verify(baseReferralRegisterFragmentPresenter.getView()).filterandSortInInitializeQueries();
    }

    @Test
    public void getMainTable() {
        Assert.assertNotNull(baseReferralRegisterFragmentPresenter.getMainTable());
    }

    @Test
    public void getDueFilterCondition() {
        Assert.assertNotNull(baseReferralRegisterFragmentPresenter.getDueFilterCondition());
    }

}