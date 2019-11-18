package org.smartregister.chw.referral.activity;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

public class BaseReferralRegisterActivityTest {
    @Mock
    private BaseReferralRegisterActivity baseReferralRegisterActivity = new BaseReferralRegisterActivity();

    @Test
    public void testGetLocationID() {
        Assert.assertEquals(BaseReferralRegisterActivity.class,
                baseReferralRegisterActivity.getFamilyFormActivity());
    }
}
