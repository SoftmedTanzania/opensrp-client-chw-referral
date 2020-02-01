package org.smartregister.chw.referral.contract

import androidx.lifecycle.ViewModel
import com.nerdstone.neatformcore.domain.model.NFormViewData
import org.apache.commons.lang3.tuple.Triple
import org.json.JSONObject
import org.smartregister.chw.referral.domain.MemberObject
import org.smartregister.chw.referral.domain.ReferralServiceIndicatorObject
import org.smartregister.chw.referral.domain.ReferralServiceObject
import org.smartregister.domain.Location
import org.smartregister.view.contract.BaseRegisterContract
import java.util.*

interface BaseIssueReferralContract {

    interface View {

        fun presenter(): Presenter

        fun setProfileViewWithData()

    }

    interface Presenter : BaseRegisterContract.Presenter {

        fun getView(): View?

        fun <T> getViewModel(): Class<T> where T : ViewModel, T : Model

        fun getMainCondition(): String

        fun getMainTable(): String

        fun fillClientData(memberObject: MemberObject?)

        fun initializeMemberObject(memberObject: MemberObject?)

        fun saveForm(valuesHashMap: HashMap<String, NFormViewData>, jsonObject: JSONObject)
    }

    interface Model {

        fun getLocationId(locationName: String?): String?

        @Throws(Exception::class)
        fun getFormWithValuesAsJson(
            formName: String?, entityId: String?,
            currentLocationId: String?, memberObject: MemberObject?
        ): JSONObject?

        fun mainSelect(tableName: String, mainCondition: String): String

        val healthFacilities: List<Location>?

        fun getReferralServicesList(referralServiceId: String): ReferralServiceObject?

        fun getIndicatorsByServiceId(serviceId: String?): List<ReferralServiceIndicatorObject>?
    }

    interface Interactor {

        fun onDestroy(isChangingConfiguration: Boolean)

        fun saveRegistration(
            baseEntityId: String, valuesHashMap: HashMap<String, NFormViewData>,
            jsonObject: JSONObject, callBack: InteractorCallBack
        )
    }

    interface InteractorCallBack {

        fun onUniqueIdFetched(triple: Triple<String, String, String>, entityId: String)

        fun onNoUniqueId()

        fun onRegistrationSaved()
    }
}