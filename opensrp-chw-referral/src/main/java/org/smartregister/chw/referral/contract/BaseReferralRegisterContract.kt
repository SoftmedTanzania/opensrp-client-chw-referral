package org.smartregister.chw.referral.contract

import org.apache.commons.lang3.tuple.Triple
import org.smartregister.view.contract.BaseRegisterContract

interface BaseReferralRegisterContract {

    interface View : BaseRegisterContract.View {
        fun presenter(): Presenter?
    }

    interface Presenter : BaseRegisterContract.Presenter {

        fun saveLanguage(language: String)

        fun closeFamilyRecord(jsonString: String)

        fun getView(): View?

    }

    interface Model {

        fun registerViewConfigurations(viewIdentifiers: List<String?>?)

        fun unregisterViewConfiguration(viewIdentifiers: List<String?>?)

        fun saveLanguage(language: String?)

        fun getLocationId(locationName: String?): String?

        val initials: String?
    }

    interface Interactor {

        fun onDestroy(isChangingConfiguration: Boolean)

        fun getNextUniqueId(
            triple: Triple<String?, String?, String?>?, callBack: InteractorCallBack?
        )

        fun removeFamilyFromRegister(closeFormJsonString: String?, providerId: String?)
    }

    interface InteractorCallBack {

        fun onUniqueIdFetched(triple: Triple<String, String, String>, entityId: String)

        fun onNoUniqueId()

        fun onRegistrationSaved()
    }
}