package org.smartregister.chw.referral.presenter

import com.nerdstone.neatformcore.domain.model.NFormViewData
import io.mockk.spyk
import io.mockk.verifyAll
import org.json.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.smartregister.chw.referral.contract.BaseFollowupContract
import org.smartregister.chw.referral.domain.MemberObject
import org.smartregister.chw.referral.model.AbstractReferralFollowupModel
import org.smartregister.chw.referral.model.BaseIssueReferralModel
import org.smartregister.chw.referral.util.Constants
import org.smartregister.commonregistry.CommonPersonObjectClient

class BaseReferralFollowupPresenterTest {

    private val followupReferralView: BaseFollowupContract.View = spyk()
    private val followupReferralInteractor: BaseFollowupContract.Interactor = spyk()
    private val sampleBaseEntityId = "5a5mple-b35eent"
    private val followupReferralPresenter: BaseFollowupContract.Presenter =
        spyk(
            BaseReferralFollowupPresenter(
                followupReferralView,
                AbstractReferralFollowupModel::class.java,
                followupReferralInteractor
            ),
            recordPrivateCalls = true
        )
    private lateinit var memberObject: MemberObject

    @Before
    fun `Before test`() {
        val columnNames = BaseIssueReferralModel()
            .mainColumns(Constants.Tables.FAMILY_MEMBER).map {
                it.replace("${Constants.Tables.FAMILY_MEMBER}.", "")
            }.toTypedArray()

        val columnValues = arrayListOf(
            "10689167-07ca-45a4-91c0-1406c9ceb881", sampleBaseEntityId, "first_name", "middle_name",
            "last_name", "unique_id", "male", "1985-01-01T03:00:00.000+03:00", null
        )
        val details = columnNames.zip(columnValues).toMap()
        memberObject = MemberObject(
            CommonPersonObjectClient(sampleBaseEntityId, details, "Some Cool Name").apply {
                columnmaps = details
            }
        )
    }

    @Test
    fun `Should call save followup method of interactor`() {
        val valuesHashMap = hashMapOf<String, NFormViewData>()
        val jsonFormObject = JSONObject()
        followupReferralPresenter.initializeMemberObject(memberObject)
        followupReferralPresenter.saveForm(valuesHashMap, jsonFormObject)
        verifyAll {
            followupReferralInteractor.saveFollowup(
                sampleBaseEntityId, valuesHashMap, jsonFormObject,
                followupReferralPresenter as BaseReferralFollowupPresenter
            )
        }
    }

    @Test
    fun `Should return view`() {
        Assert.assertNotNull(followupReferralPresenter.getView())
    }

    @Test
    fun `Should call set profile view data`() {
        followupReferralPresenter.fillProfileData(spyk(memberObject))
        verifyAll { followupReferralView.setProfileViewWithData() }
    }

    @Test
    fun initializeMemberObject() {
        followupReferralPresenter.initializeMemberObject(memberObject)
        Assert.assertNotNull((followupReferralPresenter as BaseReferralFollowupPresenter).memberObject)
    }
}