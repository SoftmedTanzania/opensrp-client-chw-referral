package org.smartregister.chw.referral.activity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.smartregister.chw.referral.contract.BaseIssueReferralContract;

import static org.hamcrest.CoreMatchers.instanceOf;

public class BaseIssueReferralActivityTest {
    @Mock
    protected BaseIssueReferralActivity baseReferralProfileActivity;

    @Spy
    protected BaseIssueReferralActivity spy;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void assertNotNull() {
        Assert.assertNotNull(baseReferralProfileActivity);
    }

    @Test
    public void presenter() {
//        Assert.assertNotNull(spy.presenter());
        Assert.assertThat(spy.presenter(), instanceOf(BaseIssueReferralContract.Presenter.class));
    }

    @Test
    public void getCurrentContext() {
        Assert.assertNotNull(spy.getCurrentContext());
    }
}
