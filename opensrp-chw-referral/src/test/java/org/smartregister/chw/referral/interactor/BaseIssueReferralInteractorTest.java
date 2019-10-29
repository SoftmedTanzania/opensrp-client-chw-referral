package org.smartregister.chw.referral.interactor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.smartregister.chw.referral.contract.BaseIssueReferralContract;
import org.smartregister.chw.referral.model.BaseIssueReferralModel;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import static org.mockito.Mockito.verify;

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

    @Test
    public void setFormValues() throws Exception {
        interactor.saveRegistration("{}", callBack);
        verify(interactor).saveRegistration("{}", callBack);

    }
}