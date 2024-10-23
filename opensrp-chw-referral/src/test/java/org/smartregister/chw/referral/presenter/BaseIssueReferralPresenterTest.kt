package org.smartregister.chw.referral.presenter

import androidx.lifecycle.ViewModel
import com.nerdstone.neatformcore.domain.model.NFormViewData
import io.mockk.spyk
import io.mockk.verifyAll
import org.json.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.smartregister.chw.referral.contract.BaseIssueReferralContract
import org.smartregister.chw.referral.domain.MemberObject
import org.smartregister.chw.referral.model.AbstractIssueReferralModel
import org.smartregister.chw.referral.model.BaseIssueReferralModel
import org.smartregister.chw.referral.util.Constants
import org.smartregister.chw.referral.util.DBConstants
import org.smartregister.commonregistry.CommonPersonObjectClient

class BaseIssueReferralPresenterTest {

    private val issueReferralView: BaseIssueReferralContract.View = spyk()
    private val issueReferralInteractor: BaseIssueReferralContract.Interactor = spyk()
    private val sampleBaseEntityId = "5a5mple-b35eent"
    private val baseIssueReferralPresenter: BaseIssueReferralContract.Presenter =
        spyk(
            BaseIssueReferralPresenter(
                sampleBaseEntityId, issueReferralView, AbstractIssueReferralModel::class.java,
                issueReferralInteractor
            ),
            recordPrivateCalls = true
        )
    private lateinit var memberObject: MemberObject

    @Before
    fun `Just before tests`() {
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
    fun `Should return view`() {
        Assert.assertNotNull(baseIssueReferralPresenter.getView())
    }

    @Test
    fun `Should return the view model`() {
        Assert.assertNotNull(baseIssueReferralPresenter.getViewModel<AbstractIssueReferralModel>() is ViewModel)
    }

    @Test
    fun `Should return main condition with the base entity id`() {
        Assert.assertTrue(
            baseIssueReferralPresenter.getMainCondition() == "${Constants.Tables.FAMILY_MEMBER}.${DBConstants.Key.BASE_ENTITY_ID}  = '$sampleBaseEntityId'"
        )
    }

    @Test
    fun `Should return ec_family_member as the main table`() {
        Assert.assertTrue(baseIssueReferralPresenter.getMainTable() == Constants.Tables.FAMILY_MEMBER)
    }

    @Test
    fun `Should set profile with data  `() {
        baseIssueReferralPresenter.fillClientData(memberObject)
        verifyAll { issueReferralView.setProfileViewWithData() }
    }

    @Test
    fun `Should update the member object`() {
        baseIssueReferralPresenter.initializeMemberObject(memberObject)
        Assert.assertNotNull((baseIssueReferralPresenter as BaseIssueReferralPresenter).memberObject)
    }

    @Test
    fun `Should call save registration method of interactor`() {
        val valuesHashMap = hashMapOf<String, NFormViewData>()
        val jsonFormObject = JSONObject()
        baseIssueReferralPresenter.saveForm(valuesHashMap, jsonFormObject, false)
        verifyAll {
            issueReferralInteractor.saveRegistration(
                sampleBaseEntityId, valuesHashMap, jsonFormObject,
                baseIssueReferralPresenter as BaseIssueReferralPresenter,
                false
            )
        }
    }
}