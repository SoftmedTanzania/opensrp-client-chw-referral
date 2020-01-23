package org.smartregister.chw.referral.interactor;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.smartregister.chw.referral.contract.BaseIssueReferralContract;
import org.smartregister.chw.referral.model.BaseIssueReferralModel;
import org.smartregister.commonregistry.CommonPersonObjectClient;

/**
 * Created by cozej4 on 2019-10-26.
 *
 * @cozej4 https://github.com/cozej4
 */
public class BaseIssueReferralInteractorTest {

    @Spy
    protected BaseIssueReferralInteractor interactor;

    @Spy
    protected BaseIssueReferralModel model;

    @Mock
    protected CommonPersonObjectClient client;

    @Mock
    private BaseIssueReferralContract.InteractorCallBack callBack;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

}