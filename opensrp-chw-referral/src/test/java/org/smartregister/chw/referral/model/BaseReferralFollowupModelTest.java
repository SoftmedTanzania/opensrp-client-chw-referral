package org.smartregister.chw.referral.model;

import com.google.gson.Gson;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.smartregister.chw.referral.domain.FollowupFeedbackObject;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import timber.log.Timber;

/**
 * Created by cozej4 on 2019-11-12.
 *
 * @cozej4 https://github.com/cozej4
 */
public class BaseReferralFollowupModelTest {
    @Spy
    protected BaseReferralFollowupModel model;

    @Mock
    protected CommonPersonObjectClient client;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void setFormValues() {
        Assert.assertNotNull(client);

        model.memberObject = new MemberObject(client);

        FollowupFeedbackObject feedbackObject = new FollowupFeedbackObject(client);
        feedbackObject.setId("788");
        feedbackObject.setNameEn("he will come tomorrow");


        System.out.println(new Gson().toJson(feedbackObject));

        JSONObject form = null;
        try {
            form = new JSONObject("{\"count\":\"1\",\"encounter_type\":\"Referral Followup Registration\",\"entity_id\":\"\",\"relational_id\":\"\",\"metadata\":{\"start\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"start\",\"openmrs_entity_id\":\"163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"end\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"end\",\"openmrs_entity_id\":\"163138AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"today\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"encounter\",\"openmrs_entity_id\":\"encounter_date\"},\"deviceid\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"deviceid\",\"openmrs_entity_id\":\"163149AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"subscriberid\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"subscriberid\",\"openmrs_entity_id\":\"163150AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"simserial\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"simserial\",\"openmrs_entity_id\":\"163151AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"phonenumber\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"phonenumber\",\"openmrs_entity_id\":\"163152AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"encounter_location\":\"\",\"look_up\":{\"entity_id\":\"\",\"value\":\"\"}},\"step1\":{\"title\":\"Referral followup\",\"fields\":[{\"key\":\"chw_followup_feedback\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"chw_followup_feedback\",\"type\":\"edit_text\"},{\"key\":\"other_followup_feedback_information\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"other_followup_feedback_information\",\"type\":\"edit_text\"},{\"key\":\"chw_followup_date\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"chw_followup_date\",\"type\":\"edit_text\"}]}}");
        } catch (JSONException e) {
            Timber.e(e);
        }

        JSONObject formWithValues = model.setFormValues(form, JsonFormConstants.STEP1, model.referralFollowupObject);

        try {
            JSONArray fields = formWithValues.getJSONObject("step1").getJSONArray("fields");

            for (int i = 0; i < fields.length(); i++) {
                JSONObject jsonObject1 = fields.getJSONObject(i);
                switch (jsonObject1.optString("key")) {
                    case "chw_followup_feedback":
                        Assert.assertEquals(model.referralFollowupObject.getChwFollowupFeedback(), jsonObject1.optString("value"));
                        break;
                    case "other_followup_feedback_information":
                        Assert.assertEquals(model.referralFollowupObject.getOtherFollowupFeedbackInformation(), jsonObject1.optString("value"));
                        break;
                    case "chw_followup_date":
                        Assert.assertEquals(model.referralFollowupObject.getChwFollowupDate(), jsonObject1.optString("value"));
                        break;
                    default:
                        Timber.i("field not found");
                        break;
                }
            }

        } catch (JSONException e) {
            Timber.e(e);
        }
    }
}