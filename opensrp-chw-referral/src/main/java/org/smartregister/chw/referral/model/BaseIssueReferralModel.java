package org.smartregister.chw.referral.model;

import androidx.annotation.VisibleForTesting;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.domain.ReferralServiceIndicatorObject;
import org.smartregister.chw.referral.domain.ReferralServiceObject;
import org.smartregister.chw.referral.repository.ReferralServiceIndicatorRepository;
import org.smartregister.chw.referral.repository.ReferralServiceRepository;
import org.smartregister.chw.referral.util.DBConstants;
import org.smartregister.chw.referral.util.JsonFormConstants;
import org.smartregister.chw.referral.util.JsonFormUtils;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;
import org.smartregister.domain.Location;
import org.smartregister.repository.LocationRepository;

import java.util.List;

import timber.log.Timber;

public class BaseIssueReferralModel extends AbstractIssueReferralModel {

    @Override
    public String getLocationId(String locationName) {
        return null;
    }


    @Override
    public List<Location> getHealthFacilities() {
        try {
            LocationRepository locationRepository = new LocationRepository();
            return locationRepository.getAllLocations();
        } catch (Exception e) {
            Timber.e(e);
            return null;
        }
    }

    @Override
    public ReferralServiceObject getReferralServicesList(String referralServiceId) {
        try {
            ReferralServiceRepository referralServiceRepository = new ReferralServiceRepository();

            ReferralServiceObject referralServiceObject = null;
            if (referralServiceId != null) {
                try {
                    referralServiceObject = referralServiceRepository.getReferralServiceById(referralServiceId);
                } catch (Exception e) {
                    Timber.e(e);
                }
            }

            return referralServiceObject;
        } catch (Exception e) {
            Timber.e(e);
            return null;
        }
    }

    @Override
    public List<ReferralServiceIndicatorObject> getIndicatorsByServiceId(String serviceId) {
        try {
            ReferralServiceIndicatorRepository indicatorRepository = new ReferralServiceIndicatorRepository();
            return indicatorRepository.getServiceIndicatorsByServiceId(serviceId);
        } catch (Exception e) {
            Timber.e(e);
            return null;
        }
    }

    @Override
    public String mainSelect(String tableName, String mainCondition) {
        SmartRegisterQueryBuilder queryBuilder = new SmartRegisterQueryBuilder();
        queryBuilder.SelectInitiateMainTable(tableName, this.mainColumns(tableName));
        return queryBuilder.mainCondition(mainCondition);
    }

    protected String[] mainColumns(String tableName) {
        return new String[]{tableName + "." + DBConstants.Key.RELATIONAL_ID, tableName + "." + DBConstants.Key.BASE_ENTITY_ID, tableName + "." + DBConstants.Key.FIRST_NAME, tableName + "." + DBConstants.Key.MIDDLE_NAME, tableName + "." + DBConstants.Key.LAST_NAME, tableName + "." + DBConstants.Key.UNIQUE_ID, tableName + "." + DBConstants.Key.GENDER, tableName + "." + DBConstants.Key.DOB, tableName + "." + DBConstants.Key.DOD};
    }

    @Override
    public JSONObject getFormWithValuesAsJson(String formName, String entityId, String currentLocationId, MemberObject memberObject) throws Exception {
        JSONObject jsonForm = JsonFormUtils.getFormAsJson(formName);
        JsonFormUtils.addFormMetadata(jsonForm, entityId, currentLocationId);

        return setFormValues(jsonForm, JsonFormConstants.STEP1, memberObject);
    }

    @VisibleForTesting
    public JSONObject setFormValues(JSONObject form, String step, MemberObject memberObject) {
        try {
            JSONArray fieldsArray = form.getJSONObject(step).getJSONArray(JsonFormConstants.FIELDS);
            setFieldValues(fieldsArray, memberObject);

            Timber.i("Form JSON = %s", form.toString());
        } catch (Exception e) {
            Timber.e(e);
        }
        return form;

    }

    private void setFieldValues(JSONArray fieldsArray, MemberObject memberObject) {

        JSONObject memberJSONObject = null;
        try {
            memberJSONObject = new JSONObject(new Gson().toJson(memberObject));
        } catch (JSONException e) {
            Timber.e(e);
        }
        for (int i = 0; i < fieldsArray.length(); i++) {
            JSONObject fieldObject;
            try {
                fieldObject = fieldsArray.getJSONObject(i);
                String key = fieldObject.getString(JsonFormConstants.KEY);
                if (memberJSONObject != null) {
                    fieldObject.put(JsonFormConstants.VALUE, memberJSONObject.getString(key));
                }
            } catch (JSONException e) {
                Timber.e(e);
            }
        }
    }

}
