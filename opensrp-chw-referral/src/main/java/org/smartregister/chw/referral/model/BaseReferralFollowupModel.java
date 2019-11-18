package org.smartregister.chw.referral.model;

import android.support.annotation.VisibleForTesting;

import com.google.gson.Gson;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.domain.FollowupFeedbackObject;
import org.smartregister.chw.referral.domain.ReferralFollowupObject;
import org.smartregister.chw.referral.repository.FollowupFeedbackRepository;
import org.smartregister.chw.referral.util.JsonFormUtils;

import java.util.List;

import timber.log.Timber;

public class BaseReferralFollowupModel extends AbstractReferralFollowupModel {
    @Override
    public JSONObject getFormWithValuesAsJson(String formName, String entityId, String currentLocationId, ReferralFollowupObject referralFollowupObject) throws Exception {
        JSONObject jsonForm = JsonFormUtils.getFormAsJson(formName);
        JsonFormUtils.addFormMetadata(jsonForm, entityId, currentLocationId);

        JSONObject formWithValues = setFormValues(jsonForm, JsonFormConstants.STEP1, referralFollowupObject);

        return formWithValues;
    }

    @Override
    public List<FollowupFeedbackObject> getFollowupFeedbackList() {
        try {
            FollowupFeedbackRepository followupFeedbackRepository = new FollowupFeedbackRepository(ReferralLibrary.getInstance().getRepository());
            return followupFeedbackRepository.getFollowupFeedbacks();
        } catch (Exception e) {
            Timber.e(e);
            return null;
        }
    }

    @VisibleForTesting
    public JSONObject setFormValues(JSONObject form, String step, ReferralFollowupObject referralFollowupObject) {
        try {
            JSONArray fieldsArray = form.getJSONObject(step).getJSONArray(JsonFormConstants.FIELDS);
            setFieldValues(fieldsArray, referralFollowupObject);

            Timber.i("Form JSON = %s", form.toString());
        } catch (Exception e) {
            Timber.e(e);
        }
        return form;

    }

    private void setFieldValues(JSONArray fieldsArray, ReferralFollowupObject referralFollowupObject) {
        JSONObject followupJSONObject = null;
        try {
            followupJSONObject = new JSONObject(new Gson().toJson(referralFollowupObject));
        } catch (JSONException e) {
            Timber.e(e);
        }
        for (int i = 0; i < fieldsArray.length(); i++) {
            JSONObject fieldObject;
            try {
                fieldObject = fieldsArray.getJSONObject(i);
                String key = fieldObject.getString(JsonFormConstants.KEY);
                if (followupJSONObject != null) {
                    fieldObject.put(JsonFormConstants.VALUE, followupJSONObject.getString(key));
                }
            } catch (JSONException e) {
                Timber.e(e);
            }
        }
    }

}
