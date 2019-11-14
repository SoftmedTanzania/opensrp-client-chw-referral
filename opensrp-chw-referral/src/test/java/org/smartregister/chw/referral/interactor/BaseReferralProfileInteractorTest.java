package org.smartregister.chw.referral.interactor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.smartregister.chw.referral.contract.BaseReferralProfileContract;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.AlertStatus;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by cozej4 on 2019-10-26.
 *
 * @cozej4 https://github.com/cozej4
 */
public class BaseReferralProfileInteractorTest {

    @Spy
    private BaseReferralProfileInteractor profileInteractor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void refreshProfileInfo() {
        BaseReferralProfileContract.InteractorCallBack callBack = Mockito.spy(BaseReferralProfileContract.InteractorCallBack.class);

        CommonPersonObjectClient client = Mockito.mock(CommonPersonObjectClient.class);

        MemberObject memberObject = new MemberObject(client);
        profileInteractor.refreshProfileInfo(memberObject, callBack);

        //call back is never called because there is no mainUI thread in the AppExecutors of JVM
        verify(callBack, never()).refreshFamilyStatus(AlertStatus.normal);
    }
}