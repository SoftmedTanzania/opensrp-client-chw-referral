package org.smartregister.chw.referral.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.VisibleForTesting;

import com.google.gson.Gson;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.domain.ReferralServiceIndicatorObject;
import org.smartregister.chw.referral.domain.ReferralServiceObject;
import org.smartregister.chw.referral.repository.ReferralServiceIndicatorRepository;
import org.smartregister.chw.referral.repository.ReferralServiceRepository;
import org.smartregister.chw.referral.util.JsonFormUtils;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;
import org.smartregister.domain.Location;
import org.smartregister.repository.LocationRepository;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class BaseIssueReferralModel extends AbstractIssueReferralModel {
    @Override
    public String getLocationId(String locationName) {
        return null;
    }


    @Override
    public LiveData<List<Location>> getHealthFacilities() {
        try {
            LocationRepository locationRepository = new LocationRepository(ReferralLibrary.getInstance().getRepository());

            MutableLiveData<List<Location>> liveData = new MutableLiveData<>();
            liveData.setValue(locationRepository.getAllLocations());
            return liveData;
        } catch (Exception e) {
            Timber.e(e);
            return null;
        }
    }

    @Override
    public LiveData<List<ReferralServiceObject>> getReferralServicesList(List<String> referralServiceIds) {
        try {
            ReferralServiceRepository referralServiceRepository = new ReferralServiceRepository(ReferralLibrary.getInstance().getRepository());

            List<ReferralServiceObject> servicesList = new ArrayList<>();
            if (referralServiceIds != null) {
                for (String serviceId : referralServiceIds) {
                    try {
                        servicesList.add(referralServiceRepository.getReferralServiceById(serviceId));
                    } catch (Exception e) {
                        Timber.e(e);
                    }
                }
            } else {
                servicesList = referralServiceRepository.getReferralServices();
            }

            MutableLiveData<List<ReferralServiceObject>> liveData = new MutableLiveData<>();
            liveData.setValue(servicesList);

            return liveData;
        } catch (Exception e) {
            Timber.e(e);
            return null;
        }
    }

    @Override
    public List<ReferralServiceIndicatorObject> getIndicatorsByServiceId(String serviceId) {
        try {
            ReferralServiceIndicatorRepository indicatorRepository = new ReferralServiceIndicatorRepository(ReferralLibrary.getInstance().getRepository());
            return indicatorRepository.getServiceIndicatorsByServiceId(serviceId);
        } catch (Exception e) {
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
        return new String[]{tableName + ".relationalid", tableName + "." + "base_entity_id", tableName + "." + "first_name", tableName + "." + "middle_name", tableName + "." + "last_name", tableName + "." + "unique_id", tableName + "." + "gender", tableName + "." + "dob", tableName + "." + "dod"};
    }

    @Override
    public JSONObject getFormWithValuesAsJson(String formName, String entityId, String currentLocationId, MemberObject memberObject) throws Exception {
        JSONObject jsonForm = JsonFormUtils.getFormAsJson(formName);
        JsonFormUtils.addFormMetadata(jsonForm, entityId, currentLocationId);

        return setFormValues(jsonForm, memberObject);
    }

    @VisibleForTesting
    public JSONObject setFormValues(JSONObject form, MemberObject memberObject) {
        try {
            JSONArray fieldsArray = form.getJSONObject(JsonFormConstants.STEP1).getJSONArray("fields");
            setFieldValues(fieldsArray, memberObject);
            setValuesForFollowingStepsIfTheyExist(form, memberObject, 2);

            Timber.i("Form JSON = %s", form.toString());
        } catch (Exception e) {
            Timber.e(e);
        }
        return form;

    }

    //This method checks if the form has more steps and sets values to them
    private void setValuesForFollowingStepsIfTheyExist(JSONObject form, MemberObject memberObject, int number) {

        int stepNumber  = number;
        String key = "step" + stepNumber;
        if (form.has(key)) {
            JSONArray fieldsArray;
            try {
                fieldsArray = form.getJSONObject(key).getJSONArray("fields");
                setFieldValues(fieldsArray, memberObject);
                setValuesForFollowingStepsIfTheyExist(form, memberObject, ++stepNumber);
            } catch (JSONException e) {
                Timber.e(e);
            }
        }
    }

    private void setFieldValues(JSONArray fieldsArray, MemberObject memberObject) {

        JSONObject memberJSONObject = null;
        try {
            memberJSONObject = new JSONObject(new Gson().toJson(memberObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < fieldsArray.length(); i++) {
            JSONObject fieldObject;
            try {
                fieldObject = fieldsArray.getJSONObject(i);
                String key = fieldObject.getString("key");
                if (memberJSONObject != null) {
                    fieldObject.put("value", memberJSONObject.getString(key));
                }
            } catch (JSONException e) {
                Timber.e(e);
            }
        }
    }

}
