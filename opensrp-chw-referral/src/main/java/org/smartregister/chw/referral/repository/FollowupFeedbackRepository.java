package org.smartregister.chw.referral.repository;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.domain.FollowupFeedbackObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.repository.BaseRepository;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by cozej4 on 2019-10-20.
 *
 * @cozej4 https://github.com/cozej4
 */
public class FollowupFeedbackRepository extends BaseRepository {

    private static final String TABLE_NAME = "ec_followup_feedback";
    private static final String FEEDBACK_NAME_EN = "name_en";
    private static final String ID = "id";
    private static final String FEEDBACK_NAME_SW = "name_sw";
    private static final String IS_ACTIVE = "is_active";
    private static final String DETAILS_COLUMN = "details";

    private static final String[] TABLE_COLUMNS = {ID, FEEDBACK_NAME_EN, FEEDBACK_NAME_SW, IS_ACTIVE};

    public FollowupFeedbackObject getFeedbackById(String _id) {
        Cursor mCursor = null;
        SQLiteDatabase database = getReadableDatabase();
        try {
            if (database == null) {
                return null;
            }
            String selection = ID + " = ? " + COLLATE_NOCASE;
            String[] selectionArgs = new String[]{_id};
            mCursor = database.query(TABLE_NAME, TABLE_COLUMNS, selection, selectionArgs, null, null, null);
            if (mCursor != null && mCursor.getCount() > 0 && mCursor.moveToFirst()) {

                //improve this query mechanism
                CommonPersonObjectClient commonPersonObjectClient = new CommonPersonObjectClient("", null, "");
                commonPersonObjectClient.setColumnmaps(ReferralLibrary.getInstance().context().commonrepository(TABLE_NAME).sqliteRowToMap(mCursor));
                return new FollowupFeedbackObject(commonPersonObjectClient);
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (mCursor != null) {
                mCursor.close();
            }
        }
        return null;
    }

    public List<FollowupFeedbackObject> getFollowupFeedbacks() {
        List<FollowupFeedbackObject> followupFeedbackList = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = null;
        try {
            if (database == null) {
                return null;
            }
            String selection = IS_ACTIVE + " = ? " + COLLATE_NOCASE;
            String[] selectionArgs = new String[]{"1"};

            cursor = database.query(TABLE_NAME, TABLE_COLUMNS, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {

                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    //TODO improve this query mechanism
                    CommonPersonObjectClient obj = new CommonPersonObjectClient("", null, "");
                    obj.setColumnmaps(ReferralLibrary.getInstance().context().commonrepository(TABLE_NAME).sqliteRowToMap(cursor));

                    followupFeedbackList.add(new FollowupFeedbackObject(obj));
                }
            }
            return followupFeedbackList;
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;

    }

    private ContentValues createValuesFor(FollowupFeedbackObject followupFeedbackObject) {
        ContentValues values = new ContentValues();
        values.put(ID, followupFeedbackObject.getId());
        values.put(FEEDBACK_NAME_EN, followupFeedbackObject.getNameEn());
        values.put(FEEDBACK_NAME_SW, followupFeedbackObject.getNameSw());
        values.put(IS_ACTIVE, followupFeedbackObject.isActive());
        values.put(DETAILS_COLUMN, new Gson().toJson(followupFeedbackObject));
        return values;
    }

    private void insertValues(ContentValues values) {
        this.getWritableDatabase().insert(TABLE_NAME, null, values);
    }

    public void saveFollowupFeedback(FollowupFeedbackObject followupFeedbackObject) {
        insertValues(createValuesFor(followupFeedbackObject));
        Timber.i("Successfully saved Feedback = %s", new Gson().toJson(followupFeedbackObject));
    }

}
