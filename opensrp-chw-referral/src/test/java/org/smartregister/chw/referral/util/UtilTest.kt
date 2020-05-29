package org.smartregister.chw.referral.util

import com.nerdstone.neatformcore.domain.model.NFormViewData
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
    fun extractReferralProblemsTest() {
        val problemHashMap = hashMapOf("problem" to NFormViewData().apply {
            value =
                hashMapOf(
                    "value" to hashMapOf(
                        "Severe_abdominal_pain" to NFormViewData().apply {
                            value = "Severe abdominal pain"
                        },
                        "High_blood_pressure" to NFormViewData().apply {
                            value = "High blood pressure"
                        }
                    ))
        })

        Assert.assertEquals(
            "Severe abdominal pain, High blood pressure",
            extractReferralProblems(getValues())
        )

    }

    private fun getValues(): HashMap<String, NFormViewData> {
        return hashMapOf<String, NFormViewData>().also { values ->
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
    }
}