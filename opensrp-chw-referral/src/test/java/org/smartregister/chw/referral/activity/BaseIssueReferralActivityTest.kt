package org.smartregister.chw.referral.activity

import android.content.Intent
import android.database.MatrixCursor
import androidx.test.core.app.ApplicationProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.smartregister.Context
import org.smartregister.chw.referral.model.BaseIssueReferralModel
import org.smartregister.chw.referral.util.Constants
import org.smartregister.chw.referral.util.DBConstants
import org.smartregister.chw.referral.util.JsonFormUtils
import org.smartregister.commonregistry.CommonPersonObject
import org.smartregister.commonregistry.CommonRepository

@RunWith(RobolectricTestRunner::class)
class BaseIssueReferralActivityTest {

    private val sampleForm = "sample_form"
    private val baseEntityId = "50me-ent1t1"
    private val relationalId = "10689167-07ca-45a4-91c0-1406c9ceb881"
    private val context = spyk<Context>()
    private val commonRepository = mockk<CommonRepository>()
    private val issueReferralModel = BaseIssueReferralModel()

    @Before
    fun `Before everything else`() {
        every { context.commonrepository(Constants.Tables.FAMILY_MEMBER) } returns commonRepository
        every { context.applicationContext() } returns ApplicationProvider.getApplicationContext()
        val matrixCursor =
            MatrixCursor(
                issueReferralModel.mainColumns(Constants.Tables.FAMILY_MEMBER).map {
                    it.replace("${Constants.Tables.FAMILY_MEMBER}.", "")
                }.toTypedArray()
            )
        val details = arrayListOf(
            relationalId, baseEntityId, "first_name", "middle_name",
            "last_name", "unique_id", "male", "1985-01-01T03:00:00.000+03:00", null
        )
        matrixCursor.addRow(details)

        every {
            commonRepository.rawCustomQueryForAdapter(
                issueReferralModel.mainSelect(
                    Constants.Tables.FAMILY_MEMBER,
                    "${Constants.Tables.FAMILY_MEMBER}.${DBConstants.Key.BASE_ENTITY_ID}  = '$baseEntityId'"
                )
            )
        } returns matrixCursor

        val detailsMap = matrixCursor.columnNames.zip(details).toMap()
        every { commonRepository.readAllcommonforCursorAdapter(matrixCursor) } returns
                CommonPersonObject(baseEntityId, relationalId, detailsMap, null).apply {
                    columnmaps = detailsMap
                }

        mockkObject(JsonFormUtils::class)
        every { JsonFormUtils.getFormAsJson(sampleForm, context.applicationContext()) } returns spyk()
    }

    @Test
    fun `Should create json form`() {
        val intent = spyk<Intent>().apply {
            putExtra(Constants.ActivityPayload.BASE_ENTITY_ID, baseEntityId)
            putExtra(Constants.ActivityPayload.REFERRAL_SERVICE_IDS, "1")
            putExtra(Constants.ActivityPayload.ACTION, "")
            putExtra(Constants.ActivityPayload.REFERRAL_FORM_NAME, sampleForm)
        }

        val controller =
            buildActivity(BaseIssueReferralActivity::class.java, intent).setup()
        controller.create(null)
    }
}