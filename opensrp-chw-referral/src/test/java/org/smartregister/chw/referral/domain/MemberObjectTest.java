package org.smartregister.chw.referral.domain;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.commonregistry.CommonPersonObjectClient;

public class MemberObjectTest {
    @Mock
    private CommonPersonObjectClient client = Mockito.mock(CommonPersonObjectClient.class);

    private MemberObject memberObject = new MemberObject(client);

    @Test
    public void getFirstName() {
        memberObject.setFirstName("Denis");
        Assert.assertEquals("Denis", memberObject.getFirstName());
    }

    @Test
    public void getMiddleName() {
        memberObject.setMiddleName("Talemwa");
        Assert.assertEquals("Talemwa", memberObject.getMiddleName());
    }

    @Test
    public void getLastName() {
        memberObject.setLastName("Rwelamila");
        Assert.assertEquals("Rwelamila", memberObject.getLastName());
    }

    @Test
    public void getAddress() {
        memberObject.setAddress("123 Rd");
        Assert.assertEquals("123 Rd", memberObject.getAddress());
    }

    @Test
    public void getGender() {
        memberObject.setGender("Male");
        Assert.assertEquals("Male", memberObject.getGender());
    }

    @Test
    public void getAge() {
        memberObject.setAge("123");
        Assert.assertEquals("123", memberObject.getAge());
    }

    @Test
    public void testIsClosed() {
        memberObject.setIsClosed(false);
        Assert.assertFalse(memberObject.getIsClosed());
    }

    @Test
    public void getUniqueId() {

        memberObject.setUniqueId("5649994");
        Assert.assertEquals("5649994", memberObject.getUniqueId());
    }

    @Test
    public void getRelationalid() {
        memberObject.setRelationalId("56");
        Assert.assertEquals("56", memberObject.getRelationalId());
    }

    @Test
    public void getDetails() {
        memberObject.setDetails("details");
        Assert.assertEquals("details", memberObject.getDetails());
    }


    @Test
    public void getFamilyHead() {
        memberObject.setFamilyHead("John Oliver");
        Assert.assertEquals("John Oliver", memberObject.getFamilyHead());
    }

    @Test
    public void getFamilyBaseEntityId() {
        memberObject.setFamilyBaseEntityId("909-99299-112f-111fsaf");
        Assert.assertEquals("909-99299-112f-111fsaf", memberObject.getFamilyBaseEntityId());
    }

    @Test
    public void getPhoneNumber() {
        memberObject.setPhoneNumber("0789998899");
        Assert.assertEquals("0789998899", memberObject.getPhoneNumber());
    }

    @Test
    public void getBaseEntityId() {
        memberObject.setBaseEntityId("6238d8aa-6632-47f9-8a88-26bfedd942c2");
        Assert.assertEquals("6238d8aa-6632-47f9-8a88-26bfedd942c2", memberObject.getBaseEntityId());
    }

    @Test
    public void getPrimaryCareGiver() {
        memberObject.setPrimaryCareGiver("John Oliver");
        Assert.assertEquals("John Oliver", memberObject.getPrimaryCareGiver());
    }

    @Test
    public void getFamilyName() {
        memberObject.setFamilyName("Oliver's Family");
        Assert.assertEquals("Oliver's Family", memberObject.getFamilyName());
    }
}

