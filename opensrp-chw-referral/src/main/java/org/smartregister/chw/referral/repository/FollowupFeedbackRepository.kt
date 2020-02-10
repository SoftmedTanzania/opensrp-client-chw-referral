package org.smartregister.chw.referral.repository

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import com.google.gson.Gson
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.smartregister.chw.referral.ReferralLibrary
import org.smartregister.chw.referral.domain.FollowupFeedbackObject
import org.smartregister.commonregistry.CommonPersonObjectClient
import org.smartregister.repository.BaseRepository
import timber.log.Timber
import java.util.*

/**
 * Created by cozej4 on 2019-10-20.
 *
 * @cozej4 https://github.com/cozej4
 */

private const val TABLE_NAME = "ec_followup_feedback"
private const val FEEDBACK_NAME_EN = "name_en"
private const val ID = "id"
private const val FEEDBACK_NAME_SW = "name_sw"
private const val IS_ACTIVE = "is_active"
private const val DETAILS_COLUMN = "details"
private val TABLE_COLUMNS = arrayOf(ID, FEEDBACK_NAME_EN, FEEDBACK_NAME_SW, IS_ACTIVE)

/**
 * repository interface for handling accessing and saving feedback referrals. It extends [BaseRepository] and implements [KoinComponent]
 */
class FollowupFeedbackRepository : BaseRepository(), KoinComponent {

    val referralLibrary by inject<ReferralLibrary>()

    /**
     * Returns [FollowupFeedbackObject] for the provided [_id]
     */
    fun getFeedbackById(_id: String): FollowupFeedbackObject? {
        var mCursor: Cursor? = null
        try {
            if (readableDatabase == null) {
                return null
            }
            mCursor = readableDatabase.query(
                TABLE_NAME, TABLE_COLUMNS, "$ID = ? $COLLATE_NOCASE", arrayOf(_id),
                null, null, null
            )
            if (mCursor != null && mCursor.count > 0 && mCursor.moveToFirst()) {
                return FollowupFeedbackObject(CommonPersonObjectClient("", null, "")
                    .apply {
                        columnmaps = referralLibrary.context.commonrepository(
                            TABLE_NAME
                        ).sqliteRowToMap(mCursor)
                    })
            }
        } catch (e: Exception) {
            Timber.e(e)
        } finally {
            mCursor?.close()
        }
        return null
    }

    /**
     * Returns a list of [FollowupFeedbackObject]
     */
    val followupFeedbacks: List<FollowupFeedbackObject>?
        get() {
            val followupFeedbackList = ArrayList<FollowupFeedbackObject>()
            val database = readableDatabase
            var cursor: Cursor? = null
            try {
                if (database == null) {
                    return null
                }

                cursor = database.query(
                    TABLE_NAME, TABLE_COLUMNS, "$IS_ACTIVE = ? $COLLATE_NOCASE", arrayOf("1"),
                    null, null, null
                )
                if (cursor != null && cursor.count > 0) {
                    for (i in 0 until cursor.count) {
                        cursor.moveToPosition(i)
                        followupFeedbackList.add(
                            FollowupFeedbackObject(
                                CommonPersonObjectClient("", null, "").apply {
                                    columnmaps = referralLibrary.context.commonrepository(
                                            TABLE_NAME
                                        ).sqliteRowToMap(cursor)
                                })
                        )
                    }
                }
                return followupFeedbackList
            } catch (e: SQLiteException) {
                Timber.e(e)
            } finally {
                cursor?.close()
            }
            return null
        }

    private fun createValuesFor(followupFeedbackObject: FollowupFeedbackObject) =
        ContentValues().apply {
            put(ID, followupFeedbackObject.id)
            put(FEEDBACK_NAME_EN, followupFeedbackObject.nameEn)
            put(FEEDBACK_NAME_SW, followupFeedbackObject.nameSw)
            put(IS_ACTIVE, followupFeedbackObject.isActive)
            put(DETAILS_COLUMN, Gson().toJson(followupFeedbackObject))
        }

    private fun insertValues(values: ContentValues) {
        this.writableDatabase.insert(TABLE_NAME, null, values)
    }

    /**
     * Saves [followupFeedbackObject] to the database
     */
    fun saveFollowupFeedback(followupFeedbackObject: FollowupFeedbackObject) {
        insertValues(createValuesFor(followupFeedbackObject))
        Timber.i("Successfully saved Feedback = %s", Gson().toJson(followupFeedbackObject))
    }
}