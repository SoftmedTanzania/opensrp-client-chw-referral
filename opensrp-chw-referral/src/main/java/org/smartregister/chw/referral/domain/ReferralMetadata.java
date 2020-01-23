package org.smartregister.chw.referral.domain;

import java.util.Map;

public class ReferralMetadata {

    private Map<String, String> locationIdMap;

    public ReferralMetadata() {
    }

    public ReferralMetadata(Map<String, String> locationIdMap) {
        this.locationIdMap = locationIdMap;
    }

    public Map<String, String> getLocationIdMap() {
        return locationIdMap;
    }

    public void setLocationIdMap(Map<String, String> locationIdMap) {
        this.locationIdMap = locationIdMap;
    }
}
