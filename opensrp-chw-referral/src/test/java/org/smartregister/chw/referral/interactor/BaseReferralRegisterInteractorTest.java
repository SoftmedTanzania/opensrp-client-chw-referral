package org.smartregister.chw.referral.interactor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

/**
 * Created by cozej4 on 2019-10-26.
 *
 * @cozej4 https://github.com/cozej4
 */
public class BaseReferralRegisterInteractorTest {

    @Spy
    private BaseReferralRegisterInteractor baseReferralRegisterInteractor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test() {
        baseReferralRegisterInteractor.onDestroy(true);
        baseReferralRegisterInteractor.removeFamilyFromRegister("closeFormJsonString", "providerId");
        assert true : "No error occured";
    }


}