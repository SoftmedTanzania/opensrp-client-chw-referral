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

        model.MEMBER_OBJECT = new MemberObject(client);
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
            form = new JSONObject("{\n" +
                    "  \"count\": \"1\",\n" +
                    "  \"encounter_type\": \"Referral Registration\",\n" +
                    "  \"entity_id\": \"\",\n" +
                    "  \"relational_id\": \"\",\n" +
                    "  \"metadata\": {\n" +
                    "    \"start\": {\n" +
                    "      \"openmrs_entity_parent\": \"\",\n" +
                    "      \"openmrs_entity\": \"concept\",\n" +
                    "      \"openmrs_data_type\": \"start\",\n" +
                    "      \"openmrs_entity_id\": \"163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
                    "    },\n" +
                    "    \"end\": {\n" +
                    "      \"openmrs_entity_parent\": \"\",\n" +
                    "      \"openmrs_entity\": \"concept\",\n" +
                    "      \"openmrs_data_type\": \"end\",\n" +
                    "      \"openmrs_entity_id\": \"163138AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
                    "    },\n" +
                    "    \"today\": {\n" +
                    "      \"openmrs_entity_parent\": \"\",\n" +
                    "      \"openmrs_entity\": \"encounter\",\n" +
                    "      \"openmrs_entity_id\": \"encounter_date\"\n" +
                    "    },\n" +
                    "    \"deviceid\": {\n" +
                    "      \"openmrs_entity_parent\": \"\",\n" +
                    "      \"openmrs_entity\": \"concept\",\n" +
                    "      \"openmrs_data_type\": \"deviceid\",\n" +
                    "      \"openmrs_entity_id\": \"163149AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
                    "    },\n" +
                    "    \"subscriberid\": {\n" +
                    "      \"openmrs_entity_parent\": \"\",\n" +
                    "      \"openmrs_entity\": \"concept\",\n" +
                    "      \"openmrs_data_type\": \"subscriberid\",\n" +
                    "      \"openmrs_entity_id\": \"163150AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
                    "    },\n" +
                    "    \"simserial\": {\n" +
                    "      \"openmrs_entity_parent\": \"\",\n" +
                    "      \"openmrs_entity\": \"concept\",\n" +
                    "      \"openmrs_data_type\": \"simserial\",\n" +
                    "      \"openmrs_entity_id\": \"163151AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
                    "    },\n" +
                    "    \"phonenumber\": {\n" +
                    "      \"openmrs_entity_parent\": \"\",\n" +
                    "      \"openmrs_entity\": \"concept\",\n" +
                    "      \"openmrs_data_type\": \"phonenumber\",\n" +
                    "      \"openmrs_entity_id\": \"163152AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
                    "    },\n" +
                    "    \"encounter_location\": \"\",\n" +
                    "    \"look_up\": {\n" +
                    "      \"entity_id\": \"\",\n" +
                    "      \"value\": \"\"\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"step1\": {\n" +
                    "    \"title\": \"Referral form\",\n" +
                    "    \"fields\": [\n" +
                    "      {\n" +
                    "        \"key\": \"chw_referral_hf\",\n" +
                    "        \"openmrs_entity_parent\": \"\",\n" +
                    "        \"openmrs_entity\": \"concept\",\n" +
                    "        \"openmrs_entity_id\": \"chw_referral_hf\",\n" +
                    "        \"type\": \"edit_text\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"key\": \"chw_referral_reason\",\n" +
                    "        \"openmrs_entity_parent\": \"\",\n" +
                    "        \"openmrs_entity\": \"concept\",\n" +
                    "        \"openmrs_entity_id\": \"chw_referral_reason\",\n" +
                    "        \"type\": \"edit_text\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"key\": \"chw_referral_service\",\n" +
                    "        \"openmrs_entity_parent\": \"\",\n" +
                    "        \"openmrs_entity\": \"concept\",\n" +
                    "        \"openmrs_entity_id\": \"chw_referral_service\",\n" +
                    "        \"type\": \"edit_text\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"key\": \"chw_referral_date\",\n" +
                    "        \"openmrs_entity_parent\": \"\",\n" +
                    "        \"openmrs_entity\": \"concept\",\n" +
                    "        \"openmrs_entity_id\": \"chw_referral_date\",\n" +
                    "        \"type\": \"edit_text\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"key\": \"is_emergency_referral\",\n" +
                    "        \"openmrs_entity_parent\": \"\",\n" +
                    "        \"openmrs_entity\": \"concept\",\n" +
                    "        \"openmrs_entity_id\": \"is_emergency_referral\",\n" +
                    "        \"type\": \"edit_text\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"key\": \"danger_signs_indicator_ids\",\n" +
                    "        \"openmrs_entity_parent\": \"\",\n" +
                    "        \"openmrs_entity\": \"concept\",\n" +
                    "        \"openmrs_entity_id\": \"danger_signs_indicator_ids\",\n" +
                    "        \"type\": \"edit_text\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"key\": \"referral_type\",\n" +
                    "        \"openmrs_entity_parent\": \"\",\n" +
                    "        \"openmrs_entity\": \"concept\",\n" +
                    "        \"openmrs_entity_id\": \"referral_type\",\n" +
                    "        \"type\": \"edit_text\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"key\": \"referral_status\",\n" +
                    "        \"openmrs_entity_parent\": \"\",\n" +
                    "        \"openmrs_entity\": \"concept\",\n" +
                    "        \"openmrs_entity_id\": \"referral_status\",\n" +
                    "        \"type\": \"edit_text\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"key\": \"referral_appointment_date\",\n" +
                    "        \"openmrs_entity_parent\": \"\",\n" +
                    "        \"openmrs_entity\": \"concept\",\n" +
                    "        \"openmrs_entity_id\": \"referral_appointment_date\",\n" +
                    "        \"type\": \"edit_text\"\n" +
                    "      }\n" +
                    "\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject formWithValues = model.setFormValues(form, model.MEMBER_OBJECT);

        try {
            JSONArray fields = formWithValues.getJSONObject("step1").getJSONArray("fields");

            for (int i = 0; i < fields.length(); i++) {
                JSONObject jsonObject1 = fields.getJSONObject(i);
                switch (jsonObject1.optString("key")) {
                    case "chw_referral_hf":
                        Assert.assertEquals(model.MEMBER_OBJECT.getChwReferralHf(), jsonObject1.optString("value"));
                        break;
                    case "chw_referral_reason":
                        Assert.assertEquals(model.MEMBER_OBJECT.getChwReferralReason(), jsonObject1.optString("value"));
                        break;
                    case "chw_referral_service":
                        Assert.assertEquals(model.MEMBER_OBJECT.getChwReferralService(), jsonObject1.optString("value"));
                        break;
                    case "chw_referral_date":
                        Assert.assertEquals(model.MEMBER_OBJECT.getChwReferralDate(), jsonObject1.optString("value"));
                        break;
                    case "is_emergency_referral":
                        Assert.assertEquals(model.MEMBER_OBJECT.getIsEmergencyReferral(), jsonObject1.optBoolean("value"));
                        break;
                    case "danger_signs_indicator_ids":
                        Assert.assertEquals(model.MEMBER_OBJECT.getDangerSignsIndicatorIds(), jsonObject1.optString("value"));
                        break;
                    case "referral_type":
                        Assert.assertEquals(model.MEMBER_OBJECT.getReferralType(), jsonObject1.optString("value"));
                        break;
                    case "referral_status":
                        Assert.assertEquals(model.MEMBER_OBJECT.getReferralStatus(), jsonObject1.optString("value"));
                        break;
                    case "referral_appointment_date":
                        Assert.assertEquals(model.MEMBER_OBJECT.getReferralAppointmentDate(), jsonObject1.optString("value"));
                        break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}