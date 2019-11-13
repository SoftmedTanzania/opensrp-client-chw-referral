package org.smartregister.chw.referral.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.domain.ReferralServiceObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import timber.log.Timber;

import static org.mockito.Mockito.verify;

/**
 * Created by cozej4 on 2019-10-26.
 *
 * @cozej4 https://github.com/cozej4
 */
public class BaseIssueReferralModelTest {
    @Spy
    protected BaseIssueReferralModel model;

    @Mock
    protected CommonPersonObjectClient client;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getHealthFacilities() {
        Assert.assertNull(model.getHealthFacilities());
    }

    @Test
    public void getReferralServicesList() {
        Assert.assertNull(model.getReferralServicesList(null));
    }

    @Test
    public void getIndicatorsByServiceId() {
        Assert.assertNull(model.getIndicatorsByServiceId(null));
    }

    @Test
    public void mainSelect() {
        model.mainSelect("table2", "id=13");
        verify(model).mainColumns("table2");
    }

    @Test
    public void mainColumns() {
        Assert.assertArrayEquals(new String[]{"testTable.relationalid", "testTable." + "base_entity_id", "testTable." + "first_name", "testTable." + "middle_name", "testTable." + "last_name", "testTable." + "unique_id", "testTable." + "gender", "testTable." + "dob", "testTable." + "dod"}, model.mainColumns("testTable"));

    }

    @Test
    public void setValues() {
        Assert.assertNotNull(client);

        model.memberObject = new MemberObject(client);
        model.setAppointmentDate("2019-10-26");
        model.referralFacilityUuid = "test-facility-uuid";
        model.setReferralReason("sample reason");
        model.setIsEmergency(false);

        ReferralServiceObject referralServiceObject = new ReferralServiceObject(client);
        referralServiceObject.setId("23");
        model.selectedReferralService.set(referralServiceObject);

        model.saveDataToMemberObject();


        JSONObject form = null;
        try {
            form = new JSONObject("{\"count\":\"1\",\"encounter_type\":\"Referral Registration\",\"entity_id\":\"\",\"relational_id\":\"\",\"metadata\":{\"start\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"start\",\"openmrs_entity_id\":\"163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"end\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"end\",\"openmrs_entity_id\":\"163138AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"today\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"encounter\",\"openmrs_entity_id\":\"encounter_date\"},\"deviceid\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"deviceid\",\"openmrs_entity_id\":\"163149AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"subscriberid\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"subscriberid\",\"openmrs_entity_id\":\"163150AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"simserial\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"simserial\",\"openmrs_entity_id\":\"163151AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"phonenumber\":{\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_data_type\":\"phonenumber\",\"openmrs_entity_id\":\"163152AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"},\"encounter_location\":\"\",\"look_up\":{\"entity_id\":\"\",\"value\":\"\"}},\"step1\":{\"title\":\"Referral form\",\"fields\":[{\"key\":\"chw_referral_hf\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"chw_referral_hf\",\"type\":\"edit_text\"},{\"key\":\"chw_referral_reason\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"chw_referral_reason\",\"type\":\"edit_text\"}]},\"step2\":{\"title\":\"Referral form\",\"fields\":[{\"key\":\"chw_referral_service\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"chw_referral_service\",\"type\":\"edit_text\"},{\"key\":\"chw_referral_date\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"chw_referral_date\",\"type\":\"edit_text\"}]},\"step3\":{\"title\":\"Referral form\",\"fields\":[{\"key\":\"is_emergency_referral\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"is_emergency_referral\",\"type\":\"edit_text\"},{\"key\":\"danger_signs_indicator_ids\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"danger_signs_indicator_ids\",\"type\":\"edit_text\"},{\"key\":\"referral_type\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"referral_type\",\"type\":\"edit_text\"},{\"key\":\"referral_status\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"referral_status\",\"type\":\"edit_text\"},{\"key\":\"referral_appointment_date\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"referral_appointment_date\",\"type\":\"edit_text\"}]}}");
        } catch (JSONException e) {
            Timber.e(e);
        }

        JSONObject formWithValues = model.setFormValues(form, model.memberObject);

        try {
            for(int j=1;j<=3;j++){
                JSONArray fields = formWithValues.getJSONObject("step"+j).getJSONArray("fields");

                for (int i = 0; i < fields.length(); i++) {
                    JSONObject jsonObject1 = fields.getJSONObject(i);
                    switch (jsonObject1.optString("key")) {
                        case "chw_referral_hf":
                            Assert.assertEquals(model.memberObject.getChwReferralHf(), jsonObject1.optString("value"));
                            break;
                        case "chw_referral_reason":
                            Assert.assertEquals(model.memberObject.getChwReferralReason(), jsonObject1.optString("value"));
                            break;
                        case "chw_referral_service":
                            Assert.assertEquals(model.memberObject.getChwReferralService(), jsonObject1.optString("value"));
                            break;
                        case "chw_referral_date":
                            Assert.assertEquals(model.memberObject.getChwReferralDate(), jsonObject1.optString("value"));
                            break;
                        case "is_emergency_referral":
                            Assert.assertEquals(model.memberObject.getIsEmergencyReferral(), jsonObject1.optBoolean("value"));
                            break;
                        case "danger_signs_indicator_ids":
                            Assert.assertEquals(model.memberObject.getDangerSignsIndicatorIds(), jsonObject1.optString("value"));
                            break;
                        case "referral_type":
                            Assert.assertEquals(model.memberObject.getReferralType(), jsonObject1.optString("value"));
                            break;
                        case "referral_status":
                            Assert.assertEquals(model.memberObject.getReferralStatus(), jsonObject1.optString("value"));
                            break;
                        case "referral_appointment_date":
                            Assert.assertEquals(model.memberObject.getReferralAppointmentDate(), jsonObject1.optString("value"));
                            break;
                        default:
                            Timber.i("field not found");
                            break;
                    }
                }
            }

        } catch (JSONException e) {
            Timber.e(e);
        }
    }
}