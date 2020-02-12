package org.smartregister.chw.referral.presenter

import org.smartregister.chw.referral.contract.BaseReferralRegisterContract
import java.lang.ref.WeakReference

open class BaseReferralRegisterPresenter(
    view: BaseReferralRegisterContract.View,
    protected var model: BaseReferralRegisterContract.Model
) : BaseReferralRegisterContract.Presenter {

    private var viewReference = WeakReference(view)

    override fun getView() = viewReference.get()

    override fun registerViewConfigurations(list: List<String>) = Unit

    override fun unregisterViewConfiguration(list: List<String>) = Unit

    override fun onDestroy(b: Boolean) = Unit

    override fun updateInitials() = Unit

}