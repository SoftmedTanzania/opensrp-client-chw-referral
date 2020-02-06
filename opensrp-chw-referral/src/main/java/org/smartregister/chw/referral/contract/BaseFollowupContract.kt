package org.smartregister.chw.referral.contract

import androidx.lifecycle.ViewModel
import com.nerdstone.neatformcore.domain.model.NFormViewData
import org.json.JSONObject
import org.koin.core.KoinComponent
import org.smartregister.chw.referral.domain.FollowupFeedbackObject
import org.smartregister.chw.referral.domain.MemberObject
import java.util.*

interface BaseFollowupContract {

    interface View : InteractorCallBack {

        fun presenter(): Presenter

        fun setProfileViewWithData()
    }

    interface Presenter {

        fun <T> getViewModel(): Class<T> where T : ViewModel, T : Model

        fun fillProfileData(memberObject: MemberObject?)

        fun getView(): View?

        fun initializeMemberObject(memberObject: MemberObject)

        fun saveForm(valuesHashMap: HashMap<String, NFormViewData>, jsonObject: JSONObject)
    }

    interface Model {

        fun followupFeedbackList(): List<FollowupFeedbackObject>?
    }

    interface Interactor: KoinComponent {

        fun saveFollowup(
            baseEntityId: String, valuesHashMap: HashMap<String, NFormViewData>,
            jsonObject: JSONObject, callBack: InteractorCallBack
        )

    }

    interface InteractorCallBack {
        fun onFollowupSaved()
    }
}