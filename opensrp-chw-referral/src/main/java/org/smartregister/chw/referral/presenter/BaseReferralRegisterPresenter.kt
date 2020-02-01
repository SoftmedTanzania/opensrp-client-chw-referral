package org.smartregister.chw.referral.presenter

import org.apache.commons.lang3.tuple.Triple
import org.smartregister.chw.referral.contract.BaseReferralRegisterContract
import java.lang.ref.WeakReference

open class BaseReferralRegisterPresenter(
    view: BaseReferralRegisterContract.View,
    protected var model: BaseReferralRegisterContract.Model
) : BaseReferralRegisterContract.Presenter, BaseReferralRegisterContract.InteractorCallBack {

    private var viewReference = WeakReference(view)

    override fun saveLanguage(language: String) = Unit

    override fun closeFamilyRecord(jsonString: String) = Unit

    override fun getView() = viewReference.get()

    override fun onUniqueIdFetched(triple: Triple<String, String, String>, entityId: String) = Unit

    override fun onNoUniqueId() = Unit

    override fun onRegistrationSaved() {
        getView()?.hideProgressDialog()
    }

    override fun registerViewConfigurations(list: List<String>) = Unit

    override fun unregisterViewConfiguration(list: List<String>) = Unit

    override fun onDestroy(b: Boolean) = Unit

    override fun updateInitials() = Unit

}