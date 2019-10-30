package org.smartregister.chw.referral.repository;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.domain.ReferralServiceObject;
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
public class ReferralServiceRepository extends BaseRepository {

    private static final String TABLE_NAME = "ec_referral_service";
    private static final String SERVICE_NAME_EN = "name_en";
    private static final String SERVICE_NAME_SW = "name_sw";
    private static final String SERVICE_IDENTIFIER = "identifier";
    private static final String IS_ACTIVE = "is_active";
    private static final String ID = "id";
    private static final String DETAILS_COLUMN = "details";

    private static final String[] TABLE_COLUMNS = {ID, SERVICE_NAME_EN, SERVICE_NAME_SW, SERVICE_IDENTIFIER, IS_ACTIVE};


    public ReferralServiceRepository(Repository repository) {
        super(repository);
    }

    public ReferralServiceObject getReferralServiceById(String _id) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = null;
        try {
            if (database == null) {
                return null;
            }
            String selection = ID + " = ? " + COLLATE_NOCASE;
            String[] selectionArgs = new String[]{_id};
            cursor = database.query(TABLE_NAME, TABLE_COLUMNS, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                //improve this query mechanism
                CommonPersonObjectClient commonPersonObjectClient = new CommonPersonObjectClient("", null, "");
                commonPersonObjectClient.setColumnmaps(ReferralLibrary.getInstance().context().commonrepository(TABLE_NAME).sqliteRowToMap(cursor));
                return new ReferralServiceObject(commonPersonObjectClient);
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

    public List<ReferralServiceObject> getReferralServices() {
        SQLiteDatabase database = getReadableDatabase();
        List<ReferralServiceObject> referralServices = new ArrayList<>();
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

                    //improve this query mechanism
                    CommonPersonObjectClient commonPersonObjectClient = new CommonPersonObjectClient("", null, "");
                    commonPersonObjectClient.setColumnmaps(ReferralLibrary.getInstance().context().commonrepository(TABLE_NAME).sqliteRowToMap(cursor));

                    referralServices.add(new ReferralServiceObject(commonPersonObjectClient));
                }

                return referralServices;
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

    private ContentValues createValuesFor(ReferralServiceObject referralServiceObject) {
        ContentValues values = new ContentValues();
        values.put(ID, referralServiceObject.getId());
        values.put(SERVICE_NAME_EN, referralServiceObject.getNameEn());
        values.put(SERVICE_NAME_SW, referralServiceObject.getNameSw());
        values.put(SERVICE_IDENTIFIER, referralServiceObject.getIdentifier());
        values.put(IS_ACTIVE, referralServiceObject.isActive());
        values.put(DETAILS_COLUMN, new Gson().toJson(referralServiceObject));
        return values;
    }

    private void insertValues(ContentValues values) {
        this.getWritableDatabase().insert(TABLE_NAME, null, values);
    }

    public void saveReferralService(ReferralServiceObject referralServiceObject) {
        insertValues(createValuesFor(referralServiceObject));
        Timber.i("Successfully saved Referral Service = %s", new Gson().toJson(referralServiceObject));
    }

}
