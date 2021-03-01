package org.smartregister.chw.referral.contract

import org.json.JSONArray
import org.koin.core.KoinComponent
import org.smartregister.configurableviews.model.Field
import org.smartregister.configurableviews.model.RegisterConfiguration
import org.smartregister.configurableviews.model.ViewConfiguration
import org.smartregister.domain.Response
import org.smartregister.view.contract.BaseRegisterFragmentContract
import org.smartregister.view.contract.IView

interface BaseReferralRegisterFragmentContract {

    interface View : BaseRegisterFragmentContract.View {

        override fun initializeAdapter(visibleColumns: Set<IView>?)

        override fun presenter(): Presenter?
    }

    interface Presenter : BaseRegisterFragmentContract.Presenter {

        fun getView() : View?

        fun updateSortAndFilter(filterList: List<Field>, sortField: Field)

        override fun getMainCondition(): String

        override fun getDefaultSortQuery(): String

        fun getMainTable(): String

        override fun getDueFilterCondition(): String
    }

    interface Model: KoinComponent {

        fun defaultRegisterConfiguration(): RegisterConfiguration?

        fun getViewConfiguration(viewConfigurationIdentifier: String?): ViewConfiguration?

        fun getRegisterActiveColumns(viewConfigurationIdentifier: String?): Set<IView>?

        fun countSelect(tableName: String?, mainCondition: String?): String?

        fun mainSelect(tableName: String?, mainCondition: String?): String?

        fun getFilterText(filterList: List<Field?>?, filter: String?): String?

        fun getSortText(sortField: Field?): String?

        fun getJsonArray(response: Response<String?>?): JSONArray?
    }
}