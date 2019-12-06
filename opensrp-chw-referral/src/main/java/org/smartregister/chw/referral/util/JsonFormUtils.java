package org.smartregister.chw.referral.util;

import com.google.gson.Gson;
import com.nerdstone.neatformcore.domain.model.NFormViewData;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.domain.ReferralServiceObject;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.util.FormUtils;

import java.util.HashMap;

import timber.log.Timber;

import static org.smartregister.chw.referral.util.Constants.ENCOUNTER_TYPE;
import static org.smartregister.chw.referral.util.Constants.STEP_ONE;

public class JsonFormUtils extends org.smartregister.util.JsonFormUtils {
    public static final String METADATA = "metadata";

    public static Triple<Boolean, JSONObject, JSONArray> validateParameters(String jsonString) {

        JSONObject jsonForm = toJSONObject(jsonString);
        JSONArray fields = referralFormFields(jsonForm);

        Triple<Boolean, JSONObject, JSONArray> registrationFormParams = Triple.of(jsonForm != null && fields != null, jsonForm, fields);
        return registrationFormParams;
    }

    public static JSONArray referralFormFields(JSONObject jsonForm) {
        try {
            JSONArray fieldsOne = fields(jsonForm, STEP_ONE);
            return fieldsOne;

        } catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }

    public static JSONArray fields(JSONObject jsonForm, String step) {
        try {

            JSONObject step1 = jsonForm.has(step) ? jsonForm.getJSONObject(step) : null;
            if (step1 == null) {
                return null;
            }

            return step1.has(FIELDS) ? step1.getJSONArray(FIELDS) : null;

        } catch (JSONException e) {
            Timber.e(e);
        }
        return null;
    }

    public static Event processJsonForm(AllSharedPreferences allSharedPreferences,String entityId, HashMap<String, NFormViewData> valuesHashMap, JSONObject jsonForm, String encounter_type) {


        JSONArray fields = null;
        try {
            fields = new JSONArray(new Gson().toJson(valuesHashMap));
        }catch (Exception e){
            Timber.e(e);
        }
        if (fields==null) {
            return null;
        }

        String bindType=null;
        if (Constants.EVENT_TYPE.REGISTRATION.equals(encounter_type)) {
            bindType = Constants.TABLES.REFERRAL;
        } else if (Constants.EVENT_TYPE.REFERRAL_FOLLOW_UP_VISIT.equals(encounter_type)) {
            bindType = Constants.TABLES.REFERRAL_FOLLOW_UP;
        }


        return org.smartregister.util.JsonFormUtils.createEvent(fields, getJSONObject(jsonForm, METADATA), formTag(allSharedPreferences), entityId, encounter_type, bindType);
    }


    protected static FormTag formTag(AllSharedPreferences allSharedPreferences) {
        FormTag formTag = new FormTag();
        formTag.providerId = allSharedPreferences.fetchRegisteredANM();
        formTag.appVersion = ReferralLibrary.getInstance().getApplicationVersion();
        formTag.databaseVersion = ReferralLibrary.getInstance().getDatabaseVersion();
        return formTag;
    }


    public static void tagEvent(AllSharedPreferences allSharedPreferences, Event event) {
        String providerId = allSharedPreferences.fetchRegisteredANM();
        event.setProviderId(providerId);
        event.setLocationId(locationId(allSharedPreferences));
        event.setChildLocationId(allSharedPreferences.fetchCurrentLocality());
        event.setTeam(allSharedPreferences.fetchDefaultTeam(providerId));
        event.setTeamId(allSharedPreferences.fetchDefaultTeamId(providerId));

        event.setClientApplicationVersion(ReferralLibrary.getInstance().getApplicationVersion());
        event.setClientDatabaseVersion(ReferralLibrary.getInstance().getDatabaseVersion());
    }

    private static String locationId(AllSharedPreferences allSharedPreferences) {
        String providerId = allSharedPreferences.fetchRegisteredANM();
        String userLocationId = allSharedPreferences.fetchUserLocalityId(providerId);
        if (StringUtils.isBlank(userLocationId)) {
            userLocationId = allSharedPreferences.fetchDefaultLocalityId(providerId);
        }

        return userLocationId;
    }

    public static void addFormMetadata(JSONObject jsonObject, String entityId, String
            currentLocationId) throws JSONException {
        jsonObject.getJSONObject(METADATA).put(ENCOUNTER_LOCATION, currentLocationId);
        jsonObject.put(org.smartregister.util.JsonFormUtils.ENTITY_ID, entityId);
    }

    public static JSONObject getFormAsJson(String formName) throws Exception {
        return FormUtils.getInstance(ReferralLibrary.getInstance().context().applicationContext()).getFormJson(formName);
    }

}
