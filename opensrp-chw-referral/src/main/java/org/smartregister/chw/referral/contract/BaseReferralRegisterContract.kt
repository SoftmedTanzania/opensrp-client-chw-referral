package org.smartregister.chw.referral.contract

import org.smartregister.view.contract.BaseRegisterContract

interface BaseReferralRegisterContract {

    interface View : BaseRegisterContract.View {
        override fun presenter(): Presenter?
    }

    interface Presenter: BaseRegisterContract.Presenter {

        fun getView(): View?
    }

    interface Model {

        fun registerViewConfigurations(viewIdentifiers: List<String?>?)

        fun unregisterViewConfiguration(viewIdentifiers: List<String?>?)

        fun saveLanguage(language: String?)

        fun getLocationId(locationName: String?): String?

        val initials: String?
    }

}