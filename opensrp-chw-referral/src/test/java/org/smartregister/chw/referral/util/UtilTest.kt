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

    private val problemOther = "problem_other"
    private val problem = "problem"
    private val severeAbdominalPainKey = "Severe_abdominal_pain"
    private val fpMethodKey = "fp_method_accepted_referral"
    private val seriouslyIll = "Seriously ill"
    private val severeAbdominalPain = "Severe abdominal pain"
    private val highBloodPressure = "High blood pressure"
    private val highBloodPressureKey = "High_blood_pressure"
    private val nothingAtAll = "Nothing at all"

    private fun getValues(): HashMap<String, NFormViewData> {

        return hashMapOf<String, NFormViewData>().also { values ->
            values[problemOther] = NFormViewData().apply {
                type = EditTextNFormView::class.simpleName
                value = seriouslyIll
            }
            values[problem] = NFormViewData().apply {
                type = SpinnerNFormView::class.simpleName
                metadata = hashMapOf(
                    JsonFormUtils.OPENMRS_ENTITY to "1",
                    JsonFormUtils.OPENMRS_ENTITY_ID to "id1",
                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "p1"
                )
                value = hashMapOf(
                    severeAbdominalPainKey to
                            NFormViewData().apply {
                                type = null
                                value = severeAbdominalPain
                                metadata = hashMapOf(
                                    JsonFormUtils.OPENMRS_ENTITY to "op1",
                                    JsonFormUtils.OPENMRS_ENTITY_ID to "opd1",
                                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "ops1"
                                )
                            },
                    highBloodPressureKey to
                            NFormViewData().apply {
                                type = null
                                value = highBloodPressure
                                metadata = hashMapOf(
                                    JsonFormUtils.OPENMRS_ENTITY to "op1",
                                    JsonFormUtils.OPENMRS_ENTITY_ID to "opsid1l",
                                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "opp1"
                                )
                            })

            }

            values[fpMethodKey] = NFormViewData().apply {
                type = SpinnerNFormView::class.simpleName
                metadata = hashMapOf(
                    JsonFormUtils.OPENMRS_ENTITY to "1",
                    JsonFormUtils.OPENMRS_ENTITY_ID to "ied1",
                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "p1"
                )
                value = hashMapOf(
                    fpMethodKey to
                            NFormViewData().apply {
                                type = null
                                value = nothingAtAll
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
            values[fpMethodKey] = NFormViewData().apply {
                type = SpinnerNFormView::class.simpleName
                metadata = hashMapOf(
                    JsonFormUtils.OPENMRS_ENTITY to "1",
                    JsonFormUtils.OPENMRS_ENTITY_ID to "qid1",
                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "p1"
                )
                value = hashMapOf(
                    fpMethodKey to
                            NFormViewData().apply {
                                type = null
                                value = nothingAtAll
                                metadata = hashMapOf(
                                    JsonFormUtils.OPENMRS_ENTITY to "op3",
                                    JsonFormUtils.OPENMRS_ENTITY_ID to "opid3",
                                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "opp3"
                                )
                            })
            }
        }
        Assert.assertEquals(nothingAtAll, extractReferralProblems(map))
    }

    @Test
    fun `Testing extraction of problem without family planning method but with problems`() {
        val map = hashMapOf<String, NFormViewData>().also { values ->
            values[problem] = NFormViewData().apply {
                type = SpinnerNFormView::class.simpleName
                metadata = hashMapOf(
                    JsonFormUtils.OPENMRS_ENTITY to "1",
                    JsonFormUtils.OPENMRS_ENTITY_ID to "id1",
                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "p1"
                )
                value = hashMapOf(
                    severeAbdominalPainKey to
                            NFormViewData().apply {
                                type = null
                                value = severeAbdominalPain
                                metadata = hashMapOf(
                                    JsonFormUtils.OPENMRS_ENTITY to "op1",
                                    JsonFormUtils.OPENMRS_ENTITY_ID to "op1id1",
                                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "opp1"
                                )
                            },
                    highBloodPressureKey to
                            NFormViewData().apply {
                                type = null
                                value = highBloodPressure
                                metadata = hashMapOf(
                                    JsonFormUtils.OPENMRS_ENTITY to "op1",
                                    JsonFormUtils.OPENMRS_ENTITY_ID to "opxid1",
                                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "opp1"
                                )
                            })

            }
        }
        Assert.assertEquals(
            "Severe abdominal pain, High blood pressure",
            extractReferralProblems(map)
        )
    }

    @Test
    fun `Testing extraction of problem without family planning method but with other problems`() {
        val map = hashMapOf<String, NFormViewData>().also { values ->
            values[problemOther] = NFormViewData().apply {
                type = EditTextNFormView::class.simpleName
                value = seriouslyIll
            }
        }
        Assert.assertEquals("Other: Seriously ill", extractReferralProblems(map))
    }

    @Test
    fun `Testing extraction of problem without family planning method but with problems and other problems`() {
        val map = hashMapOf<String, NFormViewData>().also { values ->
            values[problemOther] = NFormViewData().apply {
                type = EditTextNFormView::class.simpleName
                value = seriouslyIll
            }
            values[problem] = NFormViewData().apply {
                type = SpinnerNFormView::class.simpleName
                metadata = hashMapOf(
                    JsonFormUtils.OPENMRS_ENTITY to "1",
                    JsonFormUtils.OPENMRS_ENTITY_ID to "ixd1",
                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "p1"
                )
                value = hashMapOf(
                    severeAbdominalPainKey to
                            NFormViewData().apply {
                                type = null
                                value = severeAbdominalPain
                                metadata = hashMapOf(
                                    JsonFormUtils.OPENMRS_ENTITY to "op1",
                                    JsonFormUtils.OPENMRS_ENTITY_ID to "opmid1",
                                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "opp1"
                                )
                            },
                    highBloodPressureKey to
                            NFormViewData().apply {
                                type = null
                                value = highBloodPressure
                                metadata = hashMapOf(
                                    JsonFormUtils.OPENMRS_ENTITY to "op1",
                                    JsonFormUtils.OPENMRS_ENTITY_ID to "opid1",
                                    JsonFormUtils.OPENMRS_ENTITY_PARENT to "opp1"
                                )
                            })

            }
        }
        Assert.assertEquals(
            "Severe abdominal pain, High blood pressure, Other: Seriously ill",
            extractReferralProblems(map)
        )
    }
}