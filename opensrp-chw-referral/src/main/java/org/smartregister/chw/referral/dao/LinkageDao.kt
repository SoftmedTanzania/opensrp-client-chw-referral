package org.smartregister.chw.referral.dao

import android.database.Cursor
import org.smartregister.dao.AbstractDao
import org.smartregister.dao.AbstractDao.DataMap

/**
 * Author issyzac on 09/05/2024
 */
object LinkageDao : AbstractDao() {

    /**
     * Returns follow up tasks that have the current linkage task ID in their reason reference column
     *
     * Reason reference for a follow up task is the initial task that triggered the follow up
     *
     */

    fun getTaskIdByReasonReference(referenceId: String?): String? {
        val dataMap = DataMap { cursor: Cursor? ->
            getCursorValue(
                cursor,
                "_id"
            )
        }
        val sql = String.format(
            "SELECT _id FROM %s WHERE reason_reference = '%s' ",
            "task",
            referenceId
        )
        val res = readData(sql, dataMap)
        return if (res.size > 0) res[0] else ""
    }

}