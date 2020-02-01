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
        memberObject.firstName = "Denis";
        Assert.assertEquals("Denis", memberObject.firstName);
    }

    @Test
    public void getMiddleName() {
        memberObject.middleName = "Talemwa";
        Assert.assertEquals("Talemwa", memberObject.middleName);
    }

    @Test
    public void getLastName() {
        memberObject.lastName = "Rwelamila";
        Assert.assertEquals("Rwelamila", memberObject.lastName);
    }

    @Test
    public void getAddress() {
        memberObject.address = "123 Rd";
        Assert.assertEquals("123 Rd", memberObject.address);
    }

    @Test
    public void getGender() {
        memberObject.gender = "Male";
        Assert.assertEquals("Male", memberObject.gender);
    }

    @Test
    public void getAge() {
        memberObject.age = "123";
        Assert.assertEquals("123", memberObject.age);
    }

    @Test
    public void testIsClosed() {
        memberObject.isClosed = false;
        Assert.assertFalse(memberObject.isClosed);
    }

    @Test
    public void getUniqueId() {

        memberObject.uniqueId = "5649994";
        Assert.assertEquals("5649994", memberObject.uniqueId);
    }

    @Test
    public void getRelationalid() {
        memberObject.relationalId = "56";
        Assert.assertEquals("56", memberObject.relationalId);
    }

    @Test
    public void getDetails() {
        memberObject.details = "details";
        Assert.assertEquals("details", memberObject.details);
    }


    @Test
    public void getFamilyHead() {
        memberObject.familyHead = "John Oliver";
        Assert.assertEquals("John Oliver", memberObject.familyHead);
    }

    @Test
    public void getFamilyBaseEntityId() {
        memberObject.familyBaseEntityId = "909-99299-112f-111fsaf";
        Assert.assertEquals("909-99299-112f-111fsaf", memberObject.familyBaseEntityId);
    }

    @Test
    public void getPhoneNumber() {
        memberObject.phoneNumber = "0789998899";
        Assert.assertEquals("0789998899", memberObject.phoneNumber);
    }

    @Test
    public void getBaseEntityId() {
        memberObject.baseEntityId = "6238d8aa-6632-47f9-8a88-26bfedd942c2";
        Assert.assertEquals("6238d8aa-6632-47f9-8a88-26bfedd942c2", memberObject.baseEntityId);
    }

    @Test
    public void getPrimaryCareGiver() {
        memberObject.primaryCareGiver = "John Oliver";
        Assert.assertEquals("John Oliver", memberObject.primaryCareGiver);
    }

    @Test
    public void getFamilyName() {
        memberObject.familyName = "Oliver's Family";
        Assert.assertEquals("Oliver's Family", memberObject.familyName);
    }
}

