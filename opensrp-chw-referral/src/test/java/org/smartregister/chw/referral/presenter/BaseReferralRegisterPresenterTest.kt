package org.smartregister.chw.referral.presenter

import io.mockk.spyk
import org.junit.Test

import org.junit.Assert.*
import org.smartregister.chw.referral.contract.BaseReferralRegisterContract
import org.smartregister.chw.referral.model.BaseReferralRegisterModel

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