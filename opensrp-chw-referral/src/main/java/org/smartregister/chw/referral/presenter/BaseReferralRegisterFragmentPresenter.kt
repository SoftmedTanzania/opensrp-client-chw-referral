package org.smartregister.chw.referral.presenter

import org.apache.commons.lang3.StringUtils
import org.smartregister.chw.referral.contract.BaseReferralRegisterFragmentContract
import org.smartregister.chw.referral.util.Constants
import org.smartregister.chw.referral.util.DBConstants
import org.smartregister.configurableviews.model.Field
import org.smartregister.configurableviews.model.RegisterConfiguration
import org.smartregister.configurableviews.model.View
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*

open class BaseReferralRegisterFragmentPresenter(
    view: BaseReferralRegisterFragmentContract.View,
    protected var model: BaseReferralRegisterFragmentContract.Model,
    protected var viewConfigurationIdentifier: String?
) : BaseReferralRegisterFragmentContract.Presenter {

    protected var viewReference = WeakReference(view)
    protected var config: RegisterConfiguration
    protected var visibleColumns: Set<View> = TreeSet()

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
        try {
            if (config.searchBarText != null && getView() != null) {
                getView()?.updateSearchBarHint(config.searchBarText)
            }
        } catch (e: Exception) {
            Timber.e(e)
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

    override fun getView(): BaseReferralRegisterFragmentContract.View? {
        return viewReference.get()
    }

    override fun startSync() = Unit

    override fun searchGlobally(s: String) = Unit

    override fun getMainTable() = Constants.Tables.REFERRAL

    override fun getDueFilterCondition() =
        " (cast( julianday(STRFTIME('%Y-%m-%d', datetime('now'))) -  julianday(IFNULL(SUBSTR(chw_referral_date,7,4)|| '-' " +
                "|| SUBSTR(chw_referral_date,4,2) || '-' || SUBSTR(chw_referral_date,1,2),'')) as integer) between 7 and 14) "

    init {
        config = model.defaultRegisterConfiguration()!!
    }
}