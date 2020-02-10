package org.smartregister.chw.referral.presenter

import io.mockk.spyk
import io.mockk.verifyAll
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.smartregister.chw.referral.contract.BaseReferralHistoryContract
import org.smartregister.chw.referral.domain.MemberObject
import org.smartregister.chw.referral.model.BaseIssueReferralModel
import org.smartregister.chw.referral.util.Constants
import org.smartregister.commonregistry.CommonPersonObjectClient

class BaseReferralHistoryPresenterTest {

    private val referralHistoryView: BaseReferralHistoryContract.View = spyk()
    private val sampleBaseEntityId = "5a5mple-b35eent"
    private val columnNames = BaseIssueReferralModel()
        .mainColumns(Constants.Tables.FAMILY_MEMBER).map {
            it.replace("${Constants.Tables.FAMILY_MEMBER}.", "")
        }.toTypedArray()

    private val columnValues = arrayListOf(
        "10689167-07ca-45a4-91c0-1406c9ceb881", sampleBaseEntityId, "first_name", "middle_name",
        "last_name", "unique_id", "male", "1985-01-01T03:00:00.000+03:00", null
    )
    private val details = columnNames.zip(columnValues).toMap()
    private val memberObject: MemberObject by lazy {
        MemberObject(
            CommonPersonObjectClient(sampleBaseEntityId, details, "Some Cool Name").apply {
                columnmaps = details
            }
        )
    }
    private val referralHistoryPresenter: BaseReferralHistoryContract.Presenter =
        spyk(
            BaseReferralHistoryPresenter(memberObject, referralHistoryView),
            recordPrivateCalls = true
        )


    @Test
    fun `Should return the main condition querying`() {
        assertEquals(
            referralHistoryPresenter.getMainCondition(),
            """ec_referral.base_entity_id = '${sampleBaseEntityId}'"""
        )
    }

    @Test
    fun `Should return table name ec_referral`() {
        assertEquals(referralHistoryPresenter.getMainTable(), "ec_referral")
    }

    @Test
    fun `Referral history view should not be null`() {
        assertNotNull(referralHistoryPresenter.getView())
    }

    @Test
    fun `Fill data should call pass data to the view profile`() {
        referralHistoryPresenter.fillClientData(memberObject)
        verifyAll { referralHistoryView.setProfileViewWithData(memberObject) }
    }
}