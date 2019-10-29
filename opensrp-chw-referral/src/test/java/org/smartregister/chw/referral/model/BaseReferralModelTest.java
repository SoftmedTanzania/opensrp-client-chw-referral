package org.smartregister.chw.referral.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.mockito.Mockito.verify;

/**
 * Created by cozej4 on 2019-10-26.
 *
 * @cozej4 https://github.com/cozej4
 */
public class BaseReferralModelTest {

    @Spy
    protected BaseIssueReferralModel model;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void mainSelect() {

        Assert.assertEquals("Select testTable.id as _id , testTable.relationalid , testTable.base_entity_id , testTable.first_name , testTable.middle_name , testTable.last_name , testTable.unique_id , testTable.gender , testTable.dob , testTable.dod FROM testTable WHERE testCondition>45 ", model.mainSelect("testTable", "testCondition>45"));

        verify(model).mainColumns("testTable");
    }

    @Test
    public void mainColumns() {
        Assert.assertArrayEquals(new String[]{"testTable.relationalid", "testTable." + "base_entity_id", "testTable." + "first_name", "testTable." + "middle_name", "testTable." + "last_name", "testTable." + "unique_id", "testTable." + "gender", "testTable." + "dob", "testTable." + "dod"}, model.mainColumns("testTable"));

    }
}