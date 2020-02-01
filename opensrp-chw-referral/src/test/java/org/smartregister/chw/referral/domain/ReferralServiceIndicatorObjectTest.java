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
public class ReferralServiceIndicatorObjectTest {
    @Mock
    private CommonPersonObjectClient indicator = Mockito.mock(CommonPersonObjectClient.class);

    private ReferralServiceIndicatorObject indicatorMemberObject = new ReferralServiceIndicatorObject(indicator);

    @Test
    public void getId() {
        indicatorMemberObject.id = "1";
        Assert.assertEquals("1", indicatorMemberObject.id);

    }

    @Test
    public void setId() {
        indicatorMemberObject.id = "2";
        Assert.assertEquals("2", indicatorMemberObject.id);
    }

    @Test
    public void getRelationalId() {
        indicatorMemberObject.relationalId = "744";
        Assert.assertEquals("744", indicatorMemberObject.relationalId);
    }

    @Test
    public void setRelationalId() {
        indicatorMemberObject.relationalId = "1223";
        Assert.assertEquals("1223", indicatorMemberObject.relationalId);
    }

    @Test
    public void getNameEn() {
        indicatorMemberObject.nameEn = "ANC";
        Assert.assertEquals("ANC", indicatorMemberObject.nameEn);
    }


    @Test
    public void getNameSw() {
        indicatorMemberObject.nameSw = "Wajawazito";
        Assert.assertEquals("Wajawazito", indicatorMemberObject.nameSw);
    }

    @Test
    public void isActive() {
        indicatorMemberObject.setActive(true);
        Assert.assertTrue(indicatorMemberObject.isActive());
    }

    @Test
    public void isChecked() {
        indicatorMemberObject.setChecked(true);
        Assert.assertTrue(indicatorMemberObject.isChecked());
    }
}