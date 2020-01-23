package org.smartregister.chw.referral.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import static org.mockito.Mockito.verify;

/**
 * Created by cozej4 on 2019-10-26.
 *
 * @cozej4 https://github.com/cozej4
 */
public class BaseIssueReferralModelTest {
    @Spy
    protected BaseIssueReferralModel model;

    @Mock
    protected CommonPersonObjectClient client;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getReferralServicesList() {
        Assert.assertNull(model.getReferralServicesList(null));
    }

    @Test
    public void getIndicatorsByServiceId() {
        Assert.assertNull(model.getIndicatorsByServiceId(null));
    }

    @Test
    public void mainSelect() {
        model.mainSelect("table2", "id=13");
        verify(model).mainColumns("table2");
    }

    @Test
    public void mainColumns() {
        Assert.assertArrayEquals(new String[]{"testTable.relationalid", "testTable." + "base_entity_id", "testTable." + "first_name", "testTable." + "middle_name", "testTable." + "last_name", "testTable." + "unique_id", "testTable." + "gender", "testTable." + "dob", "testTable." + "dod"}, model.mainColumns("testTable"));

    }
}