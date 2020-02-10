package org.smartregister.chw.referral.presenter

import io.mockk.spyk
import org.junit.Test

import org.junit.Assert.*
import org.smartregister.chw.referral.contract.BaseReferralHistoryContract
import org.smartregister.chw.referral.contract.BaseReferralRegisterContract
import org.smartregister.chw.referral.domain.MemberObject
import org.smartregister.chw.referral.model.BaseIssueReferralModel
import org.smartregister.chw.referral.model.BaseReferralRegisterModel
import org.smartregister.chw.referral.util.Constants
import org.smartregister.commonregistry.CommonPersonObjectClient

class BaseReferralRegisterPresenterTest {

    private val referralHistoryView: BaseReferralRegisterContract.View = spyk()
    private val referralHistoryPresenter: BaseReferralRegisterContract.Presenter =
        spyk(
            BaseReferralRegisterPresenter(referralHistoryView, BaseReferralRegisterModel()), recordPrivateCalls = true
        )

    @Test
    fun `Referral register view should not be null`() {
        assertNotNull(referralHistoryPresenter.getView())
    }
}