package org.smartregister.chw.referral.repository;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.domain.ReferralServiceIndicatorObject;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by cozej4 on 2019-10-20.
 *
 * @cozej4 https://github.com/cozej4
 */
public class ReferralServiceIndicatorRepository extends BaseRepository {

    private static final String TABLE_NAME = "ec_referral_service_indicator";
    private static final String SERVICE_NAME_EN = "name_en";
    private static final String SERVICE_NAME_SW = "name_sw";
    private static final String RELATIONAL_ID = "relationalid";
    private static final String IS_ACTIVE = "is_active";
    private static final String _ID = "id";
    private static final String DETAILS_COLUMN = "details";

    private static final String[] TABLE_COLUMNS = {_ID, RELATIONAL_ID, SERVICE_NAME_EN, SERVICE_NAME_SW, IS_ACTIVE};

    public ReferralServiceIndicatorRepository(Repository repository) {
        super(repository);
    }

    public ReferralServiceIndicatorObject getServiceIndicatorById(String _id) {
        Cursor cursor = null;
        SQLiteDatabase database = getReadableDatabase();
        try {
            if (database == null) {
                return null;
            }
            String selection = _ID + " = ? COLLATE NOCASE ";
            String[] selectionArgs = new String[]{_id};

            cursor = database.query(TABLE_NAME, TABLE_COLUMNS, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                // improve this query mechanism
                CommonPersonObjectClient commonPersonObjectClient = new CommonPersonObjectClient("", null, "");
                commonPersonObjectClient.setColumnmaps(ReferralLibrary.getInstance().context().commonrepository(TABLE_NAME).sqliteRowToMap(cursor));
                return new ReferralServiceIndicatorObject(commonPersonObjectClient);
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;

    }

    public List<ReferralServiceIndicatorObject> getServiceIndicatorsByServiceId(String referralServiceId) {
        SQLiteDatabase database = getReadableDatabase();
        List<ReferralServiceIndicatorObject> referralServiceIndicators = new ArrayList<>();
        Cursor cursor = null;
        try {
            if (database == null) {
                return null;
            }
            String selection = IS_ACTIVE + " = ? " + COLLATE_NOCASE + " AND " + RELATIONAL_ID + " = ?";
            String[] selectionArgs = new String[]{"1", referralServiceId};

            cursor = database.query(TABLE_NAME, TABLE_COLUMNS, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {

                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);

                    //TODO coze: improve this query mechanism
                    CommonPersonObjectClient commonPersonObjectClient = new CommonPersonObjectClient("", null, "");
                    commonPersonObjectClient.setColumnmaps(ReferralLibrary.getInstance().context().commonrepository(TABLE_NAME).sqliteRowToMap(cursor));

                    referralServiceIndicators.add(new ReferralServiceIndicatorObject(commonPersonObjectClient));
                }

                return referralServiceIndicators;
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;

    }

    private ContentValues createValuesFor(ReferralServiceIndicatorObject referralServiceIndicatorObject) {
        ContentValues values = new ContentValues();
        values.put(_ID, referralServiceIndicatorObject.getId());
        values.put(RELATIONAL_ID, referralServiceIndicatorObject.getRelationalId());
        values.put(SERVICE_NAME_EN, referralServiceIndicatorObject.getNameEn());
        values.put(SERVICE_NAME_SW, referralServiceIndicatorObject.getNameSw());
        values.put(IS_ACTIVE, referralServiceIndicatorObject.isActive());
        values.put(DETAILS_COLUMN, new Gson().toJson(referralServiceIndicatorObject));
        return values;
    }

    private void insertValues(ContentValues values) {
        this.getWritableDatabase().insert(TABLE_NAME, null, values);
    }

    public void saveReferralServiceIndicator(ReferralServiceIndicatorObject referralServiceIndicatorObject) {
        insertValues(createValuesFor(referralServiceIndicatorObject));
        Timber.i("Successfully saved Referral Service Indicator = %s", new Gson().toJson(referralServiceIndicatorObject));
    }

}
