package org.smartregister.chw.referral.model;

import org.json.JSONObject;
import org.smartregister.chw.referral.contract.BaseReferralRegisterContract;
import org.smartregister.chw.referral.util.JsonFormUtils;

import java.util.List;

public class BaseReferralRegisterModel implements BaseReferralRegisterContract.Model {
    @Override
    public void registerViewConfigurations(List<String> viewIdentifiers) {
//        implement

    }

    @Override
    public void unregisterViewConfiguration(List<String> viewIdentifiers) {
//        implement

    }

    @Override
    public void saveLanguage(String language) {
//        implement

    }

    @Override
    public String getLocationId(String locationName) {
        return null;
    }

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        JSONObject jsonObject = JsonFormUtils.getFormAsJson(formName);
        JsonFormUtils.getRegistrationForm(jsonObject, entityId, currentLocationId);

        return jsonObject;
    }

    @Override
    public String getInitials() {
        return null;
    }
}
