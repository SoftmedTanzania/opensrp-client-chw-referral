package org.smartregister.chw.referral.util

import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.views.widgets.EditTextNFormView
import com.nerdstone.neatformcore.views.widgets.SpinnerNFormView
import org.junit.Assert
import org.junit.Test
import org.smartregister.chw.referral.util.Util.extractReferralProblems
import org.smartregister.util.JsonFormUtils

/**
 * Created by cozej4 on 5/29/20.
 *
 * @author cozej4 https://github.com/cozej4
 */
class UtilTest {

    @Test
    fun `Testing extraction of problem with other and family planning method`() {
        Assert.assertEquals(
            "Nothing at all: Severe abdominal pain, High blood pressure, Other: Seriously ill",
            extractReferralProblems(getValues())
        )
    }

    private fun getValues(): HashMap<String, NFormViewData> {
        return hashMapOf<String, NFormViewData>().also { values ->
            values["problem_other"] = NFormViewData().apply {
                type = EditTextNFormView::class.simpleName
                value = "Seriously ill"
            }
            values["problem"] = NFormViewData().apply {
                type = SpinnerNFormView::class.simpleName
                metadata = hashMapOf(
                    JsonFormUtils.OPENMRS_ENTITY to "1",
                    JsonFormUtils.OPENMRS_ENTITY_ID to "id1",
                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "p1"
                )
                value = hashMapOf(
                    "Severe_abdominal_pain" to
                            NFormViewData().apply {
                                type = null
                                value = "Severe abdominal pain"
                                metadata = hashMapOf(
                                    JsonFormUtils.OPENMRS_ENTITY to "op1",
                                    JsonFormUtils.OPENMRS_ENTITY_ID to "opid1",
                                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "opp1"
                                )
                            },
                    "High_blood_pressure" to
                            NFormViewData().apply {
                                type = null
                                value = "High blood pressure"
                                metadata = hashMapOf(
                                    JsonFormUtils.OPENMRS_ENTITY to "op1",
                                    JsonFormUtils.OPENMRS_ENTITY_ID to "opid1",
                                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "opp1"
                                )
                            })

            }
            values["fp_method_accepted_referral"] = NFormViewData().apply {
                type = SpinnerNFormView::class.simpleName
                metadata = hashMapOf(
                    JsonFormUtils.OPENMRS_ENTITY to "1",
                    JsonFormUtils.OPENMRS_ENTITY_ID to "id1",
                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "p1"
                )
                value = hashMapOf(
                    "fp_method_accepted_referral" to
                            NFormViewData().apply {
                                type = null
                                value = "Nothing at all"
                                metadata = hashMapOf(
                                    JsonFormUtils.OPENMRS_ENTITY to "op3",
                                    JsonFormUtils.OPENMRS_ENTITY_ID to "opid3",
                                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "opp3"
                                )
                            })
            }
        }
    }

    @Test
    fun `Testing extraction of problem with family planning method without problems`() {
        val map = hashMapOf<String, NFormViewData>().also { values ->
            values["fp_method_accepted_referral"] = NFormViewData().apply {
                type = SpinnerNFormView::class.simpleName
                metadata = hashMapOf(
                    JsonFormUtils.OPENMRS_ENTITY to "1",
                    JsonFormUtils.OPENMRS_ENTITY_ID to "id1",
                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "p1"
                )
                value = hashMapOf(
                    "fp_method_accepted_referral" to
                            NFormViewData().apply {
                                type = null
                                value = "Nothing at all"
                                metadata = hashMapOf(
                                    JsonFormUtils.OPENMRS_ENTITY to "op3",
                                    JsonFormUtils.OPENMRS_ENTITY_ID to "opid3",
                                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "opp3"
                                )
                            })
            }
        }
        Assert.assertEquals("Nothing at all", extractReferralProblems(map))
    }

    @Test
    fun `Testing extraction of problem without family planning method but with problems`() {
        val map = hashMapOf<String, NFormViewData>().also { values ->
            values["problem"] = NFormViewData().apply {
                type = SpinnerNFormView::class.simpleName
                metadata = hashMapOf(
                    JsonFormUtils.OPENMRS_ENTITY to "1",
                    JsonFormUtils.OPENMRS_ENTITY_ID to "id1",
                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "p1"
                )
                value = hashMapOf(
                    "Severe_abdominal_pain" to
                            NFormViewData().apply {
                                type = null
                                value = "Severe abdominal pain"
                                metadata = hashMapOf(
                                    JsonFormUtils.OPENMRS_ENTITY to "op1",
                                    JsonFormUtils.OPENMRS_ENTITY_ID to "opid1",
                                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "opp1"
                                )
                            },
                    "High_blood_pressure" to
                            NFormViewData().apply {
                                type = null
                                value = "High blood pressure"
                                metadata = hashMapOf(
                                    JsonFormUtils.OPENMRS_ENTITY to "op1",
                                    JsonFormUtils.OPENMRS_ENTITY_ID to "opid1",
                                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "opp1"
                                )
                            })

            }
        }
        Assert.assertEquals("Severe abdominal pain, High blood pressure", extractReferralProblems(map))
    }

    @Test
    fun `Testing extraction of problem without family planning method but with other problems`() {
        val map = hashMapOf<String, NFormViewData>().also { values ->
            values["problem_other"] = NFormViewData().apply {
                type = EditTextNFormView::class.simpleName
                value = "Seriously ill"
            }
        }
        Assert.assertEquals("Other: Seriously ill", extractReferralProblems(map))
    }
    @Test
    fun `Testing extraction of problem without family planning method but with problems and other problems`() {
        val map = hashMapOf<String, NFormViewData>().also { values ->
            values["problem_other"] = NFormViewData().apply {
                type = EditTextNFormView::class.simpleName
                value = "Seriously ill"
            }
            values["problem"] = NFormViewData().apply {
                type = SpinnerNFormView::class.simpleName
                metadata = hashMapOf(
                    JsonFormUtils.OPENMRS_ENTITY to "1",
                    JsonFormUtils.OPENMRS_ENTITY_ID to "id1",
                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "p1"
                )
                value = hashMapOf(
                    "Severe_abdominal_pain" to
                            NFormViewData().apply {
                                type = null
                                value = "Severe abdominal pain"
                                metadata = hashMapOf(
                                    JsonFormUtils.OPENMRS_ENTITY to "op1",
                                    JsonFormUtils.OPENMRS_ENTITY_ID to "opid1",
                                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "opp1"
                                )
                            },
                    "High_blood_pressure" to
                            NFormViewData().apply {
                                type = null
                                value = "High blood pressure"
                                metadata = hashMapOf(
                                    JsonFormUtils.OPENMRS_ENTITY to "op1",
                                    JsonFormUtils.OPENMRS_ENTITY_ID to "opid1",
                                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "opp1"
                                )
                            })

            }
        }
        Assert.assertEquals("Severe abdominal pain, High blood pressure, Other: Seriously ill", extractReferralProblems(map))
    }
}