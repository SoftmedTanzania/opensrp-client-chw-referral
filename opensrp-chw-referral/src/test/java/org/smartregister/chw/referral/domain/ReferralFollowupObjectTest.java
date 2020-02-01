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
        followupObject.details = "details";
        Assert.assertEquals("details", followupObject.details);
    }

    @Test
    public void getBaseEntityId() {
        followupObject.baseEntityId = "23323";
        Assert.assertEquals("23323", followupObject.baseEntityId);
    }

    @Test
    public void getChwFollowupFeedback() {
        followupObject.chwFollowupFeedback = "He has relocated";
        Assert.assertEquals("He has relocated", followupObject.chwFollowupFeedback);
    }


    @Test
    public void getOtherFollowupFeedbackInformation() {
        followupObject.otherFollowupFeedbackInformation = "He is doing well";
        Assert.assertEquals("He is doing well", followupObject.otherFollowupFeedbackInformation);
    }

    @Test
    public void getIsClosed() {
        followupObject.isClosed = false;
        Assert.assertFalse(followupObject.isClosed);
    }

    @Test
    public void getRelationalId() {
        followupObject.relationalId = "45";
        Assert.assertEquals("45", followupObject.relationalId);
    }


    @Test
    public void getChwFollowupDate() {
        followupObject = new ReferralFollowupObject();
        followupObject.chwFollowupDate = "2019-12-01";
        Assert.assertEquals("2019-12-01", followupObject.chwFollowupDate);
    }

}