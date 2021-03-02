package org.smartregister.chw.referral.presenter

import org.apache.commons.lang3.StringUtils
import org.smartregister.chw.referral.contract.BaseReferralRegisterFragmentContract
import org.smartregister.chw.referral.util.Constants
import org.smartregister.chw.referral.util.DBConstants
import org.smartregister.configurableviews.model.Field
import org.smartregister.configurableviews.model.RegisterConfiguration
import org.smartregister.view.contract.BaseRegisterFragmentContract
import org.smartregister.view.contract.IView
import java.lang.ref.WeakReference
import java.util.*

open class BaseReferralRegisterFragmentPresenter(
        view: BaseRegisterFragmentContract.View,
        protected var model: BaseReferralRegisterFragmentContract.Model,
        protected var viewConfigurationIdentifier: String?
) : BaseReferralRegisterFragmentContract.Presenter {

    protected var viewReference = WeakReference(view)
    protected var config: RegisterConfiguration
    var visibleColumns: MutableSet<IView> = TreeSet()

    override fun updateSortAndFilter(filterList: List<Field>, sortField: Field) = Unit

    override fun getMainCondition() = ""

    override fun getDefaultSortQuery() =
        Constants.Tables.REFERRAL + "." + DBConstants.Key.REFERRAL_DATE + " DESC "

    override fun processViewConfigurations() {
        if (StringUtils.isBlank(viewConfigurationIdentifier)) {
            return
        }
        val viewConfiguration =
            model.getViewConfiguration(viewConfigurationIdentifier)
        if (viewConfiguration != null) {
            config = viewConfiguration.metadata as RegisterConfiguration
            visibleColumns = model.getRegisterActiveColumns(viewConfigurationIdentifier)!!
        }
        if (config.searchBarText != null && getView() != null) {
            getView()?.updateSearchBarHint(config.searchBarText)
        }
    }

    override fun initializeQueries(mainCondition: String) {
        val tableName = Constants.Tables.REFERRAL
        val condition =
            if (StringUtils.trim(getMainCondition()) == "") mainCondition else getMainCondition()
        val countSelect = model.countSelect(tableName, condition)
        val mainSelect = model.mainSelect(tableName, condition)
        getView()?.also {
            it.initializeQueryParams(tableName, countSelect, mainSelect)
            it.initializeAdapter(visibleColumns)
            it.countExecute()
            it.filterandSortInInitializeQueries()
        }
    }

    override fun getView(): BaseRegisterFragmentContract.View? {
        return viewReference.get()
    }

    override fun startSync() = Unit

    override fun searchGlobally(s: String) = Unit

    override fun getMainTable() = Constants.Tables.REFERRAL

    override fun getDueFilterCondition() =
        "task.business_status = '${Constants.BusinessStatus.REFERRED}'"

    init {
        config = model.defaultRegisterConfiguration()!!
    }
}