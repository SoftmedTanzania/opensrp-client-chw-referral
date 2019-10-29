package org.smartregister.chw.referral.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.smartregister.configurableviews.model.Field;
import org.smartregister.domain.Response;
import org.smartregister.domain.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

/**
 * Created by cozej4 on 2019-10-26.
 *
 * @cozej4 https://github.com/cozej4
 */
public class BaseReferralRegisterFragmentModelTest {

    @Spy
    protected BaseReferralRegisterFragmentModel model;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void countSelect() {
        Assert.assertEquals("SELECT COUNT(*) FROM testTable WHERE id=1 ", model.countSelect("testTable", "id=1"));

    }

    @Test
    public void mainSelect() {
        model.mainSelect("testTable", "id=1");
        verify(model).mainColumns("testTable");
    }

    @Test
    public void mainColumns() {
        Assert.assertArrayEquals(new String[]{"testTable.relationalid"}, model.mainColumns("testTable"));

    }

    @Test
    public void getFilterText() {
        Field field1 = new Field("name", "column_name");
        Field field2 = new Field("surname", "column_surname");

        List<Field> filterList = new ArrayList<>();
        filterList.add(field1);
        filterList.add(field2);

        Assert.assertEquals("<font color=#727272>name</font> <font color=#f0ab41>(2)</font>", model.getFilterText(filterList, "name"));
        Assert.assertNotNull(model.getFilterText(null, null));
    }

    @Test
    public void getSortText() {
        Field field = new Field("surname", "column_surname");
        Assert.assertNotNull(model.getSortText(null));
        Assert.assertEquals("(Sort: surname)", model.getSortText(field));


        Field field2 = new Field(null, "column_surname");
        Assert.assertEquals("(Sort: column_surname)", model.getSortText(field2));
    }

    @Test
    public void getJsonArray() {
        Response<String> response = new Response<>(ResponseStatus.success, "[\"sample payload\"]");
        Assert.assertEquals("[\"sample payload\"]", model.getJsonArray(response).toString());
        Assert.assertNull(model.getJsonArray(null));
    }
}