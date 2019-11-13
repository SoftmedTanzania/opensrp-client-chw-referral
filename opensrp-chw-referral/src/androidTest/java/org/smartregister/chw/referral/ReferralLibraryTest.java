package org.smartregister.chw.referral;


import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.repository.Repository;

import refutils.ReflectionHelper;
import timber.log.Timber;

/**
 * Created by cozej4 on 2019-10-27.
 *
 * @cozej4 https://github.com/cozej4
 */
public class ReferralLibraryTest extends BaseTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void initShouldCreateNewLibraryInstanceWhenInstanceIsNull() {


        ReflectionHelper helper = new ReflectionHelper(ReferralLibrary.class);

        try {
            Assert.assertNull(helper.getField("instance"));
        } catch (NoSuchFieldException e) {
            Timber.e(e);
        } catch (IllegalAccessException e) {
            Timber.e(e);
        }


        ReferralLibrary.init(Mockito.mock(Context.class), Mockito.mock(Repository.class), BuildConfig.VERSION_CODE, 1);
        Assert.assertNotNull(ReferralLibrary.getInstance());

    }

    @Test
    public void getInstanceShouldThrowIllegalStateException() throws Throwable {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage(" Instance does not exist!!! Call "
                + CoreLibrary.class.getName()
                + ".init method in the onCreate method of "
                + "your Application class ");

        ReferralLibrary.getInstance();
    }

    @After
    public void tearDown() {
        ReflectionHelper helper = new ReflectionHelper(ReferralLibrary.class);
        try {
            helper.setField("instance", null);
        } catch (NoSuchFieldException e) {
            Timber.e(e);
        } catch (IllegalAccessException e) {
            Timber.e(e);
        }
    }
}
