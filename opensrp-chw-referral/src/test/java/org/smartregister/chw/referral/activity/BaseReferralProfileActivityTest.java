package org.smartregister.chw.referral.activity;

import android.view.View;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BaseReferralProfileActivityTest {
    @Mock
    protected ReferralDetailsViewActivity baseReferralProfileActivity;

    @Mock
    protected View view;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void assertNotNull() {
        Assert.assertNotNull(baseReferralProfileActivity);
    }

}
