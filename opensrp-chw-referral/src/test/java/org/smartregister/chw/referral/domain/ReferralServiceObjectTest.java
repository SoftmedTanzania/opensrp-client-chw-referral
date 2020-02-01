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
        serviceMemberObject.id = "1";
        Assert.assertEquals("1", serviceMemberObject.id);

    }

    @Test
    public void setId() {
        serviceMemberObject.id = "2";
        Assert.assertEquals("2", serviceMemberObject.id);
    }

    @Test
    public void getNameEn() {
        serviceMemberObject.nameEn = "ANC";
        Assert.assertEquals("ANC", serviceMemberObject.nameEn);
    }


    @Test
    public void getNameSw() {
        serviceMemberObject.nameSw = "Wajawazito";
        Assert.assertEquals("Wajawazito", serviceMemberObject.nameSw);
    }

    @Test
    public void isActive() {
        serviceMemberObject.setActive(true);
        Assert.assertTrue(serviceMemberObject.isActive());
    }

    @Test
    public void getIdentifier() {
        serviceMemberObject.identifier = "hiv";
        Assert.assertEquals("hiv", serviceMemberObject.identifier);

    }
}