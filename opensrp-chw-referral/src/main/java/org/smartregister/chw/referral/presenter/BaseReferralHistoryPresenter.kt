package org.smartregister.chw.referral.presenter

import org.smartregister.chw.referral.contract.BaseReferralHistoryContract
import org.smartregister.chw.referral.domain.MemberObject
import org.smartregister.chw.referral.util.Constants
import java.lang.ref.WeakReference

open class BaseReferralHistoryPresenter(
    private val memberObject: MemberObject, view: BaseReferralHistoryContract.View
) : BaseReferralHistoryContract.Presenter {

    private val viewReference = WeakReference(view)

    override fun getMainCondition() =
        """ec_referral.base_entity_id = '${memberObject.baseEntityId}'"""

    override fun getMainTable() =  Constants.Tables.REFERRAL

    override fun getView() = viewReference.get()

    override fun fillClientData(memberObject: MemberObject) {
        getView()?.setProfileViewWithData(memberObject)
    }

}