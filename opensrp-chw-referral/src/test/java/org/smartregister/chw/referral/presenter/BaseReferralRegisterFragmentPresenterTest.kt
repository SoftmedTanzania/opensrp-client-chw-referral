package org.smartregister.chw.referral.presenter

import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyAll
import io.mockk.verifySequence
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.smartregister.chw.referral.TestReferralApp
import org.smartregister.chw.referral.contract.BaseReferralRegisterFragmentContract
import org.smartregister.chw.referral.model.BaseReferralRegisterFragmentModel
import org.smartregister.chw.referral.util.Constants
import org.smartregister.chw.referral.util.DBConstants

@RunWith(RobolectricTestRunner::class)
@Config(application = TestReferralApp::class)
class BaseReferralRegisterFragmentPresenterTest {

    private val referralRegisterFragmentView: BaseReferralRegisterFragmentContract.View = spyk()

    private val referralRegisterFragmentPresenter: BaseReferralRegisterFragmentContract.Presenter =
        spyk(
            BaseReferralRegisterFragmentPresenter(
                referralRegisterFragmentView, BaseReferralRegisterFragmentModel(), null
            ),
            recordPrivateCalls = true
        )


    @Test
    fun `Should initialize the queries on the view`() {
        referralRegisterFragmentPresenter.initializeQueries("ec_referral.referral_status")
        val visibleColumns =
            (referralRegisterFragmentPresenter as BaseReferralRegisterFragmentPresenter).visibleColumns
        verifySequence {
            referralRegisterFragmentView.initializeQueryParams(
                "ec_referral",
                "SELECT COUNT(*) FROM ec_referral WHERE ec_referral.referral_status ",
                "Select ec_referral.id as _id , ec_referral.relationalid FROM ec_referral WHERE ec_referral.referral_status "
            )
            referralRegisterFragmentView.initializeAdapter(visibleColumns)
            referralRegisterFragmentView.countExecute()
            referralRegisterFragmentView.filterandSortInInitializeQueries()
        }
    }

    @Test
    fun `Main condition should be initialize by empty string`() {
        assertTrue(referralRegisterFragmentPresenter.getMainCondition().isEmpty())
    }

    @Test
    fun `Should return the right table name`() {
        assertTrue(referralRegisterFragmentPresenter.getMainTable() == Constants.Tables.REFERRAL)
    }

    @Test
    fun `Should return the due filter query`() {
        assertEquals(
            referralRegisterFragmentPresenter.getDueFilterCondition(),
            "referral_status = ' ${Constants.ReferralStatus.PENDING}'"
        )
    }

    @Test
    fun `Should return default sort query`() {
        assertEquals(
            referralRegisterFragmentPresenter.getDefaultSortQuery(),
            Constants.Tables.REFERRAL + "." + DBConstants.Key.REFERRAL_DATE + " DESC "
        )
    }

    @Test
    fun `View should not be null`() {
        assertNotNull(referralRegisterFragmentPresenter.getView())
    }
}