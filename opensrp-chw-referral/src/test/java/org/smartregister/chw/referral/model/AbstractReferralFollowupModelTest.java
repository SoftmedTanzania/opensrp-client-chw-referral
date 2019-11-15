package org.smartregister.chw.referral.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.smartregister.chw.referral.domain.FollowupFeedbackObject;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.domain.ReferralFollowupObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;

/**
 * Created by cozej4 on 2019-11-12.
 *
 * @cozej4 https://github.com/cozej4
 */
public class AbstractReferralFollowupModelTest {
    @Spy
    protected BaseReferralFollowupModel model;

    @Mock
    protected CommonPersonObjectClient client;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void setFollowupFeedback() {
        Assert.assertNull(model.getReferralFollowupFeedback().get());


        FollowupFeedbackObject feedbackObject = new FollowupFeedbackObject(client);
        feedbackObject.setNameEn("Client died");

        model.setReferralFollowupFeedback(feedbackObject);
        Assert.assertEquals(model.getReferralFollowupFeedback().get().getNameEn(), "Client died");
    }

    @Test
    public void setClientConditionDuringTheVisit() {
        Assert.assertNotNull(model.getClientConditionDuringTheVisit().get());
        model.setClientConditionDuringTheVisit("Doing very well");
        Assert.assertEquals(model.getClientConditionDuringTheVisit().get(), "Doing very well");
    }

    @Test
    public void saveDataToReferralFollowupObject() {

        ReferralFollowupObject referralFollowupObject = new ReferralFollowupObject();
        Assert.assertNotNull(referralFollowupObject);
        model.referralFollowupObject = referralFollowupObject;
        model.setClientConditionDuringTheVisit("he is doing well");


        FollowupFeedbackObject feedbackObject = new FollowupFeedbackObject(client);
        feedbackObject.setNameEn("he forgot");
        model.setReferralFollowupFeedback(feedbackObject);

        MemberObject MEMBER_OBJECT = new MemberObject(client);
        Assert.assertNotNull(MEMBER_OBJECT);
        model.memberObject = MEMBER_OBJECT;

        model.saveDataToReferralFollowupObject();
        Assert.assertEquals("he is doing well", model.referralFollowupObject.getOtherFollowupFeedbackInformation());
        Assert.assertEquals("he forgot", model.referralFollowupObject.getChwFollowupFeedback());
    }
}