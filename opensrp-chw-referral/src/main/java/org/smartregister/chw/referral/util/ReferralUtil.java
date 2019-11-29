package org.smartregister.chw.referral.util;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.utils.FormUtils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.Task;
import org.smartregister.location.helper.LocationHelper;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.BaseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import timber.log.Timber;

public class ReferralUtil {
    public static void createReferralEventAndTask(AllSharedPreferences allSharedPreferences, String jsonString, List<String> allowedLocationLevels) throws Exception {
        Event baseEvent = JsonFormUtils.processJsonForm(allSharedPreferences, jsonString);
        Objects.requireNonNull(baseEvent).setEventId(UUID.randomUUID().toString());
        Util.processEvent(allSharedPreferences, baseEvent);
        createReferralTask(baseEvent.getBaseEntityId(), allSharedPreferences, jsonString, allowedLocationLevels);
    }

    private static void createReferralTask(String baseEntityId, AllSharedPreferences allSharedPreferences, String jsonString, List<String> allowedLocationLevels) {
        Task task = new Task();
        task.setIdentifier(UUID.randomUUID().toString());
        //TODO Implement plans
      /*  Iterator<String> iterator = ChwApplication.getInstance().getPlanDefinitionRepository()
                .findAllPlanDefinitionIds().iterator();
        if (iterator.hasNext()) {
            task.setPlanIdentifier(iterator.next());
        } else {

            Timber.e("No plans exist in the server");
        }*/
        task.setPlanIdentifier(Constants.REFERRAL_TASK.REFERRAL_PLAN_ID);
        LocationHelper locationHelper = LocationHelper.getInstance();
        task.setGroupIdentifier(locationHelper.getOpenMrsLocationId(
                locationHelper.generateDefaultLocationHierarchy(new ArrayList<>(allowedLocationLevels)).get(0)));
        task.setStatus(Task.TaskStatus.READY);
        task.setBusinessStatus(Constants.REFERRAL_TASK.BUSINESS_STATUS.REFERRED);
        task.setPriority(3);
        task.setCode(Constants.REFERRAL_TASK.REFERRAL_CODE);
        task.setDescription("Convulsions, Testing Problems"); //TODO remove hard corded problems
        task.setFocus(getFocus(jsonString));
        task.setForEntity(baseEntityId);
        DateTime now = new DateTime();
        task.setExecutionStartDate(now);
        task.setAuthoredOn(now);
        task.setLastModified(now);
        task.setOwner(allSharedPreferences.fetchRegisteredANM());
        task.setSyncStatus(BaseRepository.TYPE_Created);
        task.setRequester(allSharedPreferences.getANMPreferredName(allSharedPreferences.fetchRegisteredANM()));
        task.setLocation(allSharedPreferences.fetchUserLocalityId(allSharedPreferences.fetchRegisteredANM()));
        ReferralLibrary.getInstance().getTaskRepository().addOrUpdate(task);
    }

    private static String getFocus(String jsonString) {
        try {
            return new JSONObject(jsonString).getString(Constants.FOCUS);
        } catch (JSONException e) {
            Timber.e(e);
        }
        return "";
    }

    private static String getReferralProblems(String jsonString) {
        String referralProblems = "";
        List<String> formValues = new ArrayList<>();
        try {
            JSONObject problemJson = new JSONObject(jsonString);
            JSONArray fields = FormUtils.getMultiStepFormFields(problemJson);
            for (int i = 0; i < fields.length(); i++) {
                JSONObject field = fields.getJSONObject(i);
                if (field.optBoolean(Constants.IS_PROBLEM, true)) {
                    if (field.has(JsonFormConstants.TYPE) && JsonFormConstants.CHECK_BOX.equals(field.getString(JsonFormConstants.TYPE))) {
                        if (field.has(JsonFormConstants.OPTIONS_FIELD_NAME)) {
                            JSONArray options = field.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME);
                            String values = getCheckBoxSelectedOptions(options);
                            if (StringUtils.isNotEmpty(values)) {
                                formValues.add(values);
                            }
                        }
                    } else if (field.has(JsonFormConstants.TYPE) && JsonFormConstants.RADIO_BUTTON.equals(field.getString(JsonFormConstants.TYPE))) {
                        if (field.has(JsonFormConstants.OPTIONS_FIELD_NAME) && field.has(JsonFormConstants.VALUE)) {
                            JSONArray options = field.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME);
                            String value = field.getString(JsonFormConstants.VALUE);
                            String values = getRadioButtonSelectedOptions(options, value);
                            if (StringUtils.isNotEmpty(values)) {
                                formValues.add(values);
                            }
                        }
                    } else {
                        String values = getOtherWidgetSelectedItems(field);
                        if (StringUtils.isNotEmpty(values)) {
                            formValues.add(values);
                        }
                    }
                }
            }

            referralProblems = StringUtils.join(formValues, ", ");
        } catch (JSONException e) {
            Timber.e(e, "ReferralUtils --> getReferralProblems");
        }
        return referralProblems;
    }

    private static String getCheckBoxSelectedOptions(@NotNull JSONArray options) {
        String selectedOptionValues = "";
        List<String> selectedValue = new ArrayList<>();
        try {
            for (int i = 0; i < options.length(); i++) {
                JSONObject option = options.getJSONObject(i);
                boolean useItem = true;

                if (option.optBoolean(Constants.IGNORE, false)) {
                    useItem = false;
                }

                if (option.has(JsonFormConstants.VALUE) && Boolean.valueOf(option.getString(JsonFormConstants.VALUE))
                        && useItem) { //Don't add values for  items with other
                    selectedValue.add(option.getString(JsonFormConstants.TEXT));
                }
            }
            selectedOptionValues = StringUtils.join(selectedValue, ", ");
        } catch (JSONException e) {
            Timber.e(e, "ReferralUtils --> getSelectedOptions");
        }

        return selectedOptionValues;
    }

    private static String getRadioButtonSelectedOptions(@NotNull JSONArray options, String value) {
        String selectedOptionValues = "";
        try {
            for (int i = 0; i < options.length(); i++) {
                JSONObject option = options.getJSONObject(i);
                if ((option.has(JsonFormConstants.KEY) && value.equals(option.getString(JsonFormConstants.KEY))) && (option.has(JsonFormConstants.TEXT) && StringUtils.isNotEmpty(option.getString(JsonFormConstants.VALUE)))) {
                    selectedOptionValues = option.getString(JsonFormConstants.TEXT);
                }
            }
        } catch (JSONException e) {
            Timber.e(e, "ReferralUtils --> getSelectedOptions");
        }

        return selectedOptionValues;
    }

    private static String getOtherWidgetSelectedItems(@NotNull JSONObject jsonObject) {
        String value = "";
        try {
            if (jsonObject.has(JsonFormConstants.VALUE) && StringUtils.isNotEmpty(jsonObject.getString(JsonFormConstants.VALUE))) {
                value = jsonObject.getString(JsonFormConstants.VALUE);
            }
        } catch (JSONException e) {
            Timber.e(e, "ReferralUtils --> getOtherWidgetSelectedItems");
        }

        return value;
    }

}
