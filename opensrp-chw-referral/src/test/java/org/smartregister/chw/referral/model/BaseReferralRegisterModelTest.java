package org.smartregister.chw.referral.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cozej4 on 2019-10-26.
 *
 * @cozej4 https://github.com/cozej4
 */
public class BaseReferralRegisterModelTest {

    @Spy
    protected BaseReferralRegisterModel model;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void registerViewConfigurations() {
        List<String> viewIdentifiers = new ArrayList<>();
        model.registerViewConfigurations(viewIdentifiers);
        assert true : "No error occurred";
    }

    @Test
    public void unregisterViewConfiguration() {
        List<String> viewIdentifiers = new ArrayList<>();
        model.unregisterViewConfiguration(viewIdentifiers);
        assert true : "No error occurred";
    }

    @Test
    public void getLocationId() {
        Assert.assertNull(model.getLocationId("test location"));
    }

    @Test
    public void getInitials() {
        Assert.assertNull(model.getInitials());
    }
}