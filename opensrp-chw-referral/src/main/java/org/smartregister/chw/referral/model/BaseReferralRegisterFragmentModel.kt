package org.smartregister.chw.referral.model

import org.apache.commons.lang3.StringUtils
import org.json.JSONArray
import org.koin.core.inject
import org.smartregister.chw.referral.ReferralLibrary
import org.smartregister.chw.referral.contract.BaseReferralRegisterFragmentContract
import org.smartregister.chw.referral.util.ConfigHelper.defaultRegisterConfiguration
import org.smartregister.configurableviews.ConfigurableViewsLibrary
import org.smartregister.configurableviews.model.Field
import org.smartregister.configurableviews.model.RegisterConfiguration
import org.smartregister.configurableviews.model.View
import org.smartregister.configurableviews.model.ViewConfiguration
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder
import org.smartregister.domain.Response
import org.smartregister.domain.ResponseStatus
import timber.log.Timber
import java.util.*

open class BaseReferralRegisterFragmentModel :
    BaseReferralRegisterFragmentContract.Model {

    val referralLibrary by inject<ReferralLibrary>()

    override fun defaultRegisterConfiguration(): RegisterConfiguration? {
        return defaultRegisterConfiguration(referralLibrary.context.applicationContext())
    }

    override fun getViewConfiguration(viewConfigurationIdentifier: String?): ViewConfiguration? =
        ConfigurableViewsLibrary.getInstance().configurableViewsHelper
            .getViewConfiguration(viewConfigurationIdentifier)

    override fun getRegisterActiveColumns(viewConfigurationIdentifier: String?): Set<View>? {
        return ConfigurableViewsLibrary.getInstance().configurableViewsHelper
            .getRegisterActiveColumns(viewConfigurationIdentifier)
    }

    override fun countSelect(tableName: String?, mainCondition: String?): String? {
        val countQueryBuilder = SmartRegisterQueryBuilder()
        countQueryBuilder.SelectInitiateMainTableCounts(tableName)
        return countQueryBuilder.mainCondition(mainCondition)
    }

    override fun mainSelect(tableName: String?, mainCondition: String?): String? {
        val queryBUilder = SmartRegisterQueryBuilder()
        queryBUilder.SelectInitiateMainTable(tableName, mainColumns(tableName))
        return queryBUilder.mainCondition(mainCondition)
    }

    protected open fun mainColumns(tableName: String?) = arrayOf("$tableName.relationalid")

    override fun getFilterText(
        list: List<Field?>?,
        filterTitle: String?
    ): String? {
        var filterList =
            list
        if (filterList == null) {
            filterList = ArrayList()
        }
        var filter = filterTitle
        if (filter == null) {
            filter = ""
        }
        return "<font color=#727272>" + filter + "</font> <font color=#f0ab41>(" + filterList.size + ")</font>"
    }

    override fun getSortText(sortField: Field?): String? {
        var sortText = ""
        if (sortField != null) {
            if (StringUtils.isNotBlank(sortField.displayName)) {
                sortText = "(Sort: " + sortField.displayName + ")"
            } else if (StringUtils.isNotBlank(sortField.dbAlias)) {
                sortText = "(Sort: " + sortField.dbAlias + ")"
            }
        }
        return sortText
    }

    override fun getJsonArray(response: Response<String?>?): JSONArray? {
        try {
            if (response!!.status() == ResponseStatus.success) {
                return JSONArray(response.payload())
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return null
    }
}