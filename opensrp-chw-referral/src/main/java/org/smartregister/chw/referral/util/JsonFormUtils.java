package org.smartregister.chw.referral.util;

import com.nerdstone.neatformcore.domain.model.NFormViewData;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.domain.ReferralTask;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.util.FormUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

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
            e.printStackTrace();
        }
        return null;
    }

    public static ReferralTask processJsonForm(AllSharedPreferences allSharedPreferences, String entityId,
                                               HashMap<String, NFormViewData> valuesHashMap, JSONObject jsonForm, String encounter_type) {
        String bindType = null;
        if (Constants.EVENT_TYPE.REGISTRATION.equals(encounter_type)) {
            bindType = Constants.TABLES.REFERRAL;
        } else if (Constants.EVENT_TYPE.REFERRAL_FOLLOW_UP_VISIT.equals(encounter_type)) {
            bindType = Constants.TABLES.REFERRAL_FOLLOW_UP;
        }
        ReferralTask referralTask =  new ReferralTask();
        Event event = org.smartregister.util.JsonFormUtils.createEvent(
                new JSONArray(), getJSONObject(jsonForm, METADATA), formTag(allSharedPreferences),
                entityId, encounter_type, bindType);
        event.setObs(getObs(valuesHashMap));
        referralTask.setEvent(event);
        return referralTask;
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

    private static List<Obs> getObs(HashMap<String, NFormViewData> detailsHashmap) {
        List<Obs> obs = new ArrayList<>();
        for (String key : detailsHashmap.keySet()) {
            Obs ob = new Obs();
            ob.setFormSubmissionField(key);
            NFormViewData nFormViewData = detailsHashmap.get(key);
            if (nFormViewData != null) {
                if (nFormViewData.getMetadata() != null && nFormViewData.getMetadata().containsKey(OPENMRS_ENTITY))
                    ob.setFieldType(String.valueOf(nFormViewData.getMetadata().get(OPENMRS_ENTITY)));

                if (nFormViewData.getMetadata() != null && nFormViewData.getMetadata().containsKey(OPENMRS_ENTITY_ID))
                    ob.setFieldCode(String.valueOf(nFormViewData.getMetadata().get(OPENMRS_ENTITY_ID)));

                if (nFormViewData.getMetadata() != null && nFormViewData.getMetadata().containsKey(OPENMRS_ENTITY_PARENT))
                    ob.setParentCode(String.valueOf(nFormViewData.getMetadata().get(OPENMRS_ENTITY_PARENT)));


                if (nFormViewData.getValue() instanceof HashMap) {

                    List<Object> humanReadableValues = new ArrayList<>();
                    HashMap valuesHashMap = ((HashMap) nFormViewData.getValue());

                    for (Object optionsValues : valuesHashMap.keySet()) {
                        if (valuesHashMap.get(optionsValues) instanceof NFormViewData && valuesHashMap.get(optionsValues) != null) {
                            NFormViewData optionsNFormViewData = (NFormViewData) valuesHashMap.get(optionsValues);
                            if (optionsNFormViewData.getMetadata() != null) {
                                if (optionsNFormViewData.getMetadata().containsKey(OPENMRS_ENTITY_ID)) {
                                    ob.setValue(optionsNFormViewData.getMetadata().get(OPENMRS_ENTITY_ID));
                                    humanReadableValues.add(optionsNFormViewData.getValue());
                                }
                            } else {
                                ob.setValue(optionsNFormViewData.getValue());
                            }
                        } else {
                            ob.setValue(valuesHashMap.get(optionsValues));
                        }

                    }

                    if (!humanReadableValues.isEmpty()) {
                        ob.setHumanReadableValues(humanReadableValues);
                    }
                } else {
                    ob.setValue(nFormViewData.getValue());
                }

            }


            obs.add(ob);
        }

        return obs;
    }
}
