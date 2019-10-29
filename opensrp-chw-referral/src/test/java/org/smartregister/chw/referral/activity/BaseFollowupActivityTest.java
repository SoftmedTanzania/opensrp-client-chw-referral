package org.smartregister.chw.referral.activity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.mockito.Mockito.verify;

/**
 * Created by cozej4 on 2019-10-26.
 *
 * @cozej4 https://github.com/cozej4
 */
public class BaseFollowupActivityTest {
    @Spy
    private BaseFollowupActivity baseFollowupActivity;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void initializePresenter() {
        baseFollowupActivity.initializePresenter();
        verify(baseFollowupActivity).fetchProfileData();
    }

}