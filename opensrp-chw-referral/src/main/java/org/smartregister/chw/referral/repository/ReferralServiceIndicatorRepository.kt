package org.smartregister.chw.referral.repository

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import com.google.gson.Gson
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.smartregister.chw.referral.ReferralLibrary
import org.smartregister.chw.referral.domain.ReferralServiceIndicatorObject
import org.smartregister.commonregistry.CommonPersonObjectClient
import org.smartregister.repository.BaseRepository
import timber.log.Timber
import java.util.*

private const val TABLE_NAME = "ec_referral_service_indicator"
private const val SERVICE_NAME_EN = "name_en"
private const val SERVICE_NAME_SW = "name_sw"
private const val RELATIONAL_ID = "relationalid"
private const val IS_ACTIVE = "is_active"
private const val _ID = "id"
private const val DETAILS_COLUMN = "details"
private val TABLE_COLUMNS = arrayOf(
    _ID, RELATIONAL_ID, SERVICE_NAME_EN, SERVICE_NAME_SW, IS_ACTIVE
)

/**
 * Referral service indicator repository that interfaces how to interact with the database
 * and obtain all the service indicators, extends [BaseRepository] and implements [KoinComponent] for DI
 */
class ReferralServiceIndicatorRepository : BaseRepository(), KoinComponent {

    val referralLibrary by inject<ReferralLibrary>()

    /**
     * Returns a [ReferralServiceIndicatorObject] for the given [_id]
     */
    fun getServiceIndicatorById(_id: String): ReferralServiceIndicatorObject? {
        var cursor: Cursor? = null
        try {
            if (readableDatabase == null) {
                return null
            }
            cursor = readableDatabase.query(
                TABLE_NAME, TABLE_COLUMNS, "$_ID = ? COLLATE NOCASE ", arrayOf(_id),
                null, null, null
            )
            if (cursor != null && cursor.count > 0 && cursor.moveToFirst()) {

                return ReferralServiceIndicatorObject(CommonPersonObjectClient("", null, "").apply {
                    columnmaps = referralLibrary.context.commonrepository(
                        TABLE_NAME
                    ).sqliteRowToMap(cursor)
                })
            }
        } catch (e: SQLiteException) {
            Timber.e(e)
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * Returns a list of [ReferralServiceIndicatorObject] for the serves with the id matching [referralServiceId]
     */
    fun getServiceIndicatorsByServiceId(referralServiceId: String): List<ReferralServiceIndicatorObject>? {
        val referralServiceIndicators: MutableList<ReferralServiceIndicatorObject> =
            ArrayList()
        var cursor: Cursor? = null
        try {
            if (readableDatabase == null) {
                return null
            }
            cursor = readableDatabase.query(
                TABLE_NAME, TABLE_COLUMNS, "$IS_ACTIVE = ? $COLLATE_NOCASE AND $RELATIONAL_ID = ?",
                arrayOf("1", referralServiceId), null, null, null
            )
            if (cursor != null && cursor.count > 0) {
                for (i in 0 until cursor.count) {
                    cursor.moveToPosition(i)
                    referralServiceIndicators.add(
                        ReferralServiceIndicatorObject(
                            CommonPersonObjectClient("", null, "")
                                .apply {
                                    columnmaps = referralLibrary.context.commonrepository(
                                        TABLE_NAME
                                    ).sqliteRowToMap(cursor)
                                }
                        )
                    )
                }
                return referralServiceIndicators
            }
        } catch (e: SQLiteException) {
            Timber.e(e)
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun createValuesFor(referralServiceIndicatorObject: ReferralServiceIndicatorObject) =
        ContentValues().apply {
            put(_ID, referralServiceIndicatorObject.id)
            put(RELATIONAL_ID, referralServiceIndicatorObject.relationalId)
            put(SERVICE_NAME_EN, referralServiceIndicatorObject.nameEn)
            put(SERVICE_NAME_SW, referralServiceIndicatorObject.nameSw)
            put(IS_ACTIVE, referralServiceIndicatorObject.isActive)
            put(DETAILS_COLUMN, Gson().toJson(referralServiceIndicatorObject))
        }

    private fun insertValues(values: ContentValues) {
        this.writableDatabase.insert(TABLE_NAME, null, values)
    }

    /**
     * Saves [referralServiceIndicatorObject] to that referral service indicator table
     */
    fun saveReferralServiceIndicator(referralServiceIndicatorObject: ReferralServiceIndicatorObject) {
        insertValues(createValuesFor(referralServiceIndicatorObject))
        Timber.i(
            "Successfully saved Referral Service Indicator = %s",
            Gson().toJson(referralServiceIndicatorObject)
        )
    }
}