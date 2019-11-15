package org.smartregister.chw.referral.custom_views;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

@PrepareForTest(BaseReferralFloatingMenu.class)
public class BaseReferralFloatingMenuTest {
    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Test
    public void testInitialization() {
        BaseReferralFloatingMenu baseReferralFloatingMenu = Mockito.spy(new BaseReferralFloatingMenu(null, null));
        Assert.assertNotNull(baseReferralFloatingMenu);

        baseReferralFloatingMenu = Mockito.spy(new BaseReferralFloatingMenu(null, null, 0));
        Assert.assertNotNull(baseReferralFloatingMenu);

    }

    @Test
    public void getPhoneNumber() {
        BaseReferralFloatingMenu baseReferralFloatingMenu = Mockito.spy(new BaseReferralFloatingMenu(null, "John", "0744112211", "Doe", "0744112244"));
        Assert.assertEquals("0744112211", baseReferralFloatingMenu.getPhoneNumber());
    }

    @Test
    public void getFamilyHeadName() {
        BaseReferralFloatingMenu baseReferralFloatingMenu = Mockito.spy(new BaseReferralFloatingMenu(null, "John", "0744112211", "Doe", "0744112244"));
        Assert.assertEquals("Doe", baseReferralFloatingMenu.getFamilyHeadName());
    }

    @Test
    public void getFamilyHeadPhone() {
        BaseReferralFloatingMenu baseReferralFloatingMenu = Mockito.spy(new BaseReferralFloatingMenu(null, "John", "0744112211", "Doe", "0744112244"));
        Assert.assertEquals("0744112244", baseReferralFloatingMenu.getFamilyHeadPhone());
    }

    @Test
    public void getClientName() {
        BaseReferralFloatingMenu baseReferralFloatingMenu = Mockito.spy(new BaseReferralFloatingMenu(null, "John", "0744112211", "Doe", "0744112244"));
        Assert.assertEquals("John", baseReferralFloatingMenu.getClientName());
    }
}