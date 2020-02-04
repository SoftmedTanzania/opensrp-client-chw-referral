package org.smartregister.chw.referral.repository

import android.content.ContentValues
import android.database.Cursor
import com.google.gson.Gson
import org.smartregister.chw.referral.ReferralLibrary
import org.smartregister.chw.referral.domain.ReferralServiceObject
import org.smartregister.commonregistry.CommonPersonObjectClient
import org.smartregister.repository.BaseRepository
import timber.log.Timber
import java.util.*

/**
 * Created by cozej4 on 2019-10-20.
 *
 * @cozej4 https://github.com/cozej4
 */
private const val TABLE_NAME = "ec_referral_service"
private const val SERVICE_NAME_EN = "name_en"
private const val SERVICE_NAME_SW = "name_sw"
private const val SERVICE_IDENTIFIER = "identifier"
private const val IS_ACTIVE = "is_active"
private const val ID = "id"
private const val DETAILS_COLUMN = "details"
private val TABLE_COLUMNS = arrayOf(
    ID, SERVICE_NAME_EN, SERVICE_NAME_SW, SERVICE_IDENTIFIER, IS_ACTIVE
)

class ReferralServiceRepository : BaseRepository() {

    fun getReferralServiceById(_id: String): ReferralServiceObject? {
        var cursor: Cursor? = null
        try {
            if (readableDatabase == null) {
                return null
            }
            cursor = readableDatabase.query(
                TABLE_NAME, TABLE_COLUMNS, "$ID = ? $COLLATE_NOCASE", arrayOf(_id),
                null, null, null
            )
            if (cursor != null && cursor.count > 0 && cursor.moveToFirst()) {
                return ReferralServiceObject(CommonPersonObjectClient("", null, "")
                    .apply {
                        columnmaps =
                            ReferralLibrary.getInstance().context.commonrepository(
                                TABLE_NAME
                            ).sqliteRowToMap(cursor)
                    })
            }
        } catch (e: Exception) {
            Timber.e(e)
        } finally {
            cursor?.close()
        }
        return null
    }

    //improve this query mechanism
    val referralServices: List<ReferralServiceObject>?
        get() {
            val referralServices = ArrayList<ReferralServiceObject>()
            var cursor: Cursor? = null
            try {
                if (readableDatabase == null) {
                    return null
                }

                cursor = readableDatabase.query(
                    TABLE_NAME, TABLE_COLUMNS, "$IS_ACTIVE = ? $COLLATE_NOCASE", arrayOf("1"),
                    null, null, null
                )
                if (cursor != null && cursor.count > 0) {
                    for (i in 0 until cursor.count) {
                        cursor.moveToPosition(i)
                        referralServices.add(
                            ReferralServiceObject(CommonPersonObjectClient("", null, "")
                                .apply {
                                    columnmaps =
                                        ReferralLibrary.getInstance().context.commonrepository(
                                            TABLE_NAME
                                        ).sqliteRowToMap(cursor)
                                })
                        )
                    }
                    return referralServices
                }
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                cursor?.close()
            }
            return null
        }

    private fun createValuesFor(referralServiceObject: ReferralServiceObject) =
        ContentValues().apply {
            put(ID, referralServiceObject.id)
            put(SERVICE_NAME_EN, referralServiceObject.nameEn)
            put(SERVICE_NAME_SW, referralServiceObject.nameSw)
            put(SERVICE_IDENTIFIER, referralServiceObject.identifier)
            put(IS_ACTIVE, referralServiceObject.isActive)
            put(DETAILS_COLUMN, Gson().toJson(referralServiceObject))
        }

    private fun insertValues(values: ContentValues) {
        this.writableDatabase.insert(TABLE_NAME, null, values)
    }

    fun saveReferralService(referralServiceObject: ReferralServiceObject) {
        insertValues(createValuesFor(referralServiceObject))
        Timber.i("Successfully saved Referral Service = %s", Gson().toJson(referralServiceObject))
    }
}