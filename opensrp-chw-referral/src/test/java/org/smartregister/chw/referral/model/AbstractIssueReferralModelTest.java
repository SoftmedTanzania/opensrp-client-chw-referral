package org.smartregister.chw.referral.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.domain.ReferralServiceIndicatorObject;
import org.smartregister.chw.referral.domain.ReferralServiceObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cozej4 on 2019-10-26.
 *
 * @cozej4 https://github.com/cozej4
 */
public class AbstractIssueReferralModelTest {
    @Spy
    protected BaseIssueReferralModel model;

    @Mock
    protected CommonPersonObjectClient client;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getReferralReason() {
        Assert.assertNotNull(model.getReferralReason().get());
        model.setReferralReason("Test reason");
        Assert.assertEquals(model.getReferralReason().get(), "Test reason");
    }

    @Test
    public void getAppointmentDate() {
        Assert.assertNotNull(model.getAppointmentDate().get());
        model.setAppointmentDate("2019-10-26");
        Assert.assertEquals(model.getAppointmentDate().get(), "2019-10-26");
    }

    @Test
    public void getIsEmergency() {
        Assert.assertEquals(model.getIsEmergency().get(), false);
        model.setIsEmergency(true);
        Assert.assertEquals(model.getIsEmergency().get(), true);
    }


    @Test
    public void saveDataToMemberObject() {

        model.saveDataToMemberObject();
        Assert.assertNull(model.memberObject);

        Assert.assertNotNull(client);

        MemberObject MEMBER_OBJECT = new MemberObject(client);
        Assert.assertNotNull(MEMBER_OBJECT);
        model.memberObject = MEMBER_OBJECT;
        model.setReferralReason("referral reason");
        model.setIsEmergency(false);

        ReferralServiceObject referralServiceObject = new ReferralServiceObject(client);
        referralServiceObject.setId("23");

        model.selectedReferralService.set(referralServiceObject);
        model.referralFacilityUuid = "testuuid";
        model.setAppointmentDate("2019-10-26");

        ReferralServiceIndicatorObject indicatorObject = new ReferralServiceIndicatorObject(client);
        indicatorObject.setId("test-indicator-uuid");
        indicatorObject.setChecked(true);

        List<ReferralServiceIndicatorObject> selectedIndicators = new ArrayList<>();
        selectedIndicators.add(indicatorObject);
        model.referralServiceIndicators = selectedIndicators;

        model.saveDataToMemberObject();
        Assert.assertEquals("2019-10-26", model.memberObject.getReferralAppointmentDate());
        Assert.assertFalse(model.memberObject.getIsEmergencyReferral());
        Assert.assertEquals("23", model.memberObject.getChwReferralServiceId());
        Assert.assertEquals("testuuid", model.memberObject.getChwReferralHf());
        Assert.assertEquals("[\"test-indicator-uuid\"]", model.memberObject.getProblemIds());
        Assert.assertEquals("referral reason", model.memberObject.getChwReferralReason());

    }
}