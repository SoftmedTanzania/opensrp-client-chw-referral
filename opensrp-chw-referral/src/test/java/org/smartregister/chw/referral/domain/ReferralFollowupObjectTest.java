package org.smartregister.chw.referral.domain;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.commonregistry.CommonPersonObjectClient;

/**
 * Created by cozej4 on 2019-11-12.
 *
 * @cozej4 https://github.com/cozej4
 */
public class ReferralFollowupObjectTest {
    @Mock
    private CommonPersonObjectClient object = Mockito.mock(CommonPersonObjectClient.class);

    private ReferralFollowupObject followupObject = new ReferralFollowupObject(object);

    @Test
    public void getDetails() {
        followupObject.setDetails("details");
        Assert.assertEquals("details", followupObject.getDetails());
    }

    @Test
    public void getBaseEntityId() {
        followupObject.setBaseEntityId("23323");
        Assert.assertEquals("23323", followupObject.getBaseEntityId());
    }

    @Test
    public void getChwFollowupFeedback() {
        followupObject.setChwFollowupFeedback("He has relocated");
        Assert.assertEquals("He has relocated", followupObject.getChwFollowupFeedback());
    }


    @Test
    public void getOtherFollowupFeedbackInformation() {
        followupObject.setOtherFollowupFeedbackInformation("He is doing well");
        Assert.assertEquals("He is doing well", followupObject.getOtherFollowupFeedbackInformation());
    }

    @Test
    public void getIsClosed() {
        followupObject.setIsClosed(false);
        Assert.assertFalse(followupObject.getIsClosed());
    }

    @Test
    public void getRelationalId() {
        followupObject.setRelationalId("45");
        Assert.assertEquals("45", followupObject.getRelationalId());
    }


    @Test
    public void getChwFollowupDate() {
        followupObject = new ReferralFollowupObject();
        followupObject.setChwFollowupDate("2019-12-01");
        Assert.assertEquals("2019-12-01", followupObject.getChwFollowupDate());
    }

}