package org.smartregister.chw.referral.util

import android.content.Context
import android.content.res.Resources
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.smartregister.chw.referral.R

/**
 *
 * Referral Utils test class
 * to increase coverage
 *
 */
class ReferralUtilTest {

    /**
     *
     * initial setup
     *
     */
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    /**
     *
     * Get gender method test
     *
     */
    @Test
    fun getGenderTest() {
        val resources = Mockito.mock(Resources::class.java)
        val context: Context = Mockito.mock(Context::class.java)
        val utils: ReferralUtil = Mockito.mock(ReferralUtil::class.java)
        Mockito.doReturn(resources).`when`(context).resources
        Mockito.doReturn("Male").`when`(resources).getString(R.string.male)
        Mockito.doReturn("Female").`when`(resources).getString(R.string.female)

        Assert.assertEquals(context.resources.getString(R.string.male), utils.getTranslatedGenderString(context, "Male"))
        Assert.assertEquals(context.resources.getString(R.string.female), utils.getTranslatedGenderString(context, "Female"))
    }

    /**
     *
     * Get referral type test
     *
     */
    @Test
    fun getReferralTypeTest() {
        val context: Context = Mockito.mock(Context::class.java)
        val utils: ReferralUtil = Mockito.mock(ReferralUtil::class.java)
        Mockito.doReturn("Sick Child").`when`(context).getString(R.string.sick_child)
        Mockito.doReturn("ANC Danger Signs").`when`(context).getString(R.string.anc_danger_signs)
        Mockito.doReturn("PNC Danger Signs").`when`(context).getString(R.string.pnc_danger_signs)
        Mockito.doReturn("FP Initiation").`when`(context).getString(R.string.fp_initiation)
        Mockito.doReturn("Suspected Malaria").`when`(context).getString(R.string.suspected_malaria)
        Mockito.doReturn("Suspected HIV").`when`(context).getString(R.string.suspected_hiv)
        Mockito.doReturn("Suspected TB").`when`(context).getString(R.string.suspected_tb)
        Mockito.doReturn("Suspected GBV").`when`(context).getString(R.string.suspected_gbv)
        Mockito.doReturn("Suspected Child GBV").`when`(context).getString(R.string.suspected_child_gbv)

        Assert.assertEquals(context.getString(R.string.sick_child), utils.getTranslatedReferralServiceType(context, "Sick Child"))
        Assert.assertEquals(context.getString(R.string.anc_danger_signs), utils.getTranslatedReferralServiceType(context, "ANC Danger Signs"))
        Assert.assertEquals(context.getString(R.string.pnc_danger_signs), utils.getTranslatedReferralServiceType(context, "PNC Danger Signs"))
        Assert.assertEquals(context.getString(R.string.fp_initiation), utils.getTranslatedReferralServiceType(context, "FP Initiation"))
        Assert.assertEquals(context.getString(R.string.suspected_malaria), utils.getTranslatedReferralServiceType(context, "Suspected Malaria"))
        Assert.assertEquals(context.getString(R.string.suspected_hiv), utils.getTranslatedReferralServiceType(context, "Suspected HIV"))
        Assert.assertEquals(context.getString(R.string.suspected_tb), utils.getTranslatedReferralServiceType(context, "Suspected TB"))
        Assert.assertEquals(context.getString(R.string.suspected_gbv), utils.getTranslatedReferralServiceType(context, "Suspected GBV"))
        Assert.assertEquals(context.getString(R.string.suspected_child_gbv), utils.getTranslatedReferralServiceType(context, "Suspected Child GBV"))
    }
}