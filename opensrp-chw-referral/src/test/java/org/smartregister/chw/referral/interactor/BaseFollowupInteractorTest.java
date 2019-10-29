package org.smartregister.chw.referral.interactor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.smartregister.chw.referral.contract.BaseFollowupContract;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by cozej4 on 2019-10-26.
 *
 * @cozej4 https://github.com/cozej4
 */
public class BaseFollowupInteractorTest {

    @Spy
    BaseFollowupInteractor baseFollowupInteractor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveFollowup() {
        BaseFollowupContract.InteractorCallBack callBack = Mockito.spy(BaseFollowupContract.InteractorCallBack.class);
        baseFollowupInteractor.saveFollowup("{}", callBack);
        verify(callBack, never()).onRegistrationSaved("{}");
    }
}