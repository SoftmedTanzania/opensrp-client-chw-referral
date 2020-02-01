package org.smartregister.chw.referral.presenter

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nerdstone.neatformcore.domain.model.NFormViewData
import org.json.JSONObject
import org.smartregister.chw.referral.contract.BaseFollowupContract
import org.smartregister.chw.referral.domain.MemberObject
import org.smartregister.chw.referral.model.AbstractReferralFollowupModel
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*

open class BaseReferralFollowupPresenter(
    view: BaseFollowupContract.View,
    private val viewModelClass: Class<out AbstractReferralFollowupModel?>,
    protected var interactor: BaseFollowupContract.Interactor
) : BaseFollowupContract.Presenter, BaseFollowupContract.InteractorCallBack {

    private val viewReference =  WeakReference(view)
    private var memberObject: MemberObject? = null

    override fun saveForm(valuesHashMap: HashMap<String, NFormViewData>, jsonObject: JSONObject) {
        try {
            interactor.saveFollowup(memberObject!!.baseEntityId!!, valuesHashMap, jsonObject, this)
        } catch (e: Exception) {
            Timber.e(Log.getStackTraceString(e))
        }
    }

    override fun getView(): BaseFollowupContract.View? = viewReference?.get()

    override fun <T> getViewModel(): Class<T> where T : ViewModel, T : BaseFollowupContract.Model {
        return viewModelClass as Class<T>

    }

    override fun fillProfileData(memberObject: MemberObject?) {
        if (memberObject != null && getView() != null) {
            getView()?.setProfileViewWithData()
        }
    }

    override fun onFollowupSaved() = Unit

    override fun initializeMemberObject(memberObject: MemberObject) {
        this.memberObject = memberObject
    }

}