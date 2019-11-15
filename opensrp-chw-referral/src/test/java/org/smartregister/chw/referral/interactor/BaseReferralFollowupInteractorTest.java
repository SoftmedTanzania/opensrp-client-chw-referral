package org.smartregister.chw.referral.interactor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.smartregister.chw.referral.contract.BaseFollowupContract;

import static org.mockito.Mockito.verify;

/**
 * Created by cozej4 on 2019-11-12.
 *
 * @cozej4 https://github.com/cozej4
 */
public class BaseReferralFollowupInteractorTest {
    @Spy
    protected BaseReferralFollowupInteractor interactor;

    @Mock
    private BaseFollowupContract.InteractorCallBack callBack;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveFollowup() {
        interactor.saveFollowup("{}", callBack);
        verify(interactor).saveFollowup("{}", callBack);

    }
}