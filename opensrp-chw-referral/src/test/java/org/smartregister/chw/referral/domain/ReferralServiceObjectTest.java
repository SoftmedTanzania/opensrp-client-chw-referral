package org.smartregister.chw.referral.domain;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.commonregistry.CommonPersonObjectClient;

/**
 * Created by cozej4 on 2019-10-25.
 *
 * @cozej4 https://github.com/cozej4
 */
public class ReferralServiceObjectTest {
    @Mock
    private CommonPersonObjectClient service = Mockito.mock(CommonPersonObjectClient.class);

    private ReferralServiceObject serviceMemberObject = new ReferralServiceObject(service);

    @Test
    public void getId() {
        serviceMemberObject.setId("1");
        Assert.assertEquals("1", serviceMemberObject.getId());

    }

    @Test
    public void setId() {
        serviceMemberObject.setId("2");
        Assert.assertEquals("2", serviceMemberObject.getId());
    }

    @Test
    public void getNameEn() {
        serviceMemberObject.setNameEn("ANC");
        Assert.assertEquals("ANC", serviceMemberObject.getNameEn());
    }


    @Test
    public void getNameSw() {
        serviceMemberObject.setNameSw("Wajawazito");
        Assert.assertEquals("Wajawazito", serviceMemberObject.getNameSw());
    }

    @Test
    public void isActive() {
        serviceMemberObject.setActive(true);
        Assert.assertTrue(serviceMemberObject.isActive());
    }

    @Test
    public void getIdentifier() {
        serviceMemberObject.setIdentifier("hiv");
        Assert.assertEquals("hiv", serviceMemberObject.getIdentifier());

    }
}