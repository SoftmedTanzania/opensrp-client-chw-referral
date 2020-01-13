package org.smartregister.chw.referral.contract;

import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONObject;
import org.smartregister.view.contract.BaseRegisterContract;

import java.util.List;

public interface BaseReferralRegisterContract {

    interface View extends BaseRegisterContract.View {

        Presenter presenter();

    }

    interface Presenter extends BaseRegisterContract.Presenter {

        void saveLanguage(String language);

        void closeFamilyRecord(String jsonString);

    }

    interface Model {

        void registerViewConfigurations(List<String> viewIdentifiers);

        void unregisterViewConfiguration(List<String> viewIdentifiers);

        void saveLanguage(String language);

        String getLocationId(String locationName);

        JSONObject getFormAsJson(String formName, String entityId,
                                 String currentLocationId) throws Exception;

        String getInitials();

    }

    interface Interactor {

        void onDestroy(boolean isChangingConfiguration);

        void getNextUniqueId(Triple<String, String, String> triple, InteractorCallBack callBack);

        void removeFamilyFromRegister(String closeFormJsonString, String providerId);

    }

    interface InteractorCallBack {

        void onUniqueIdFetched(Triple<String, String, String> triple, String entityId);

        void onNoUniqueId();

        void onRegistrationSaved();

    }
}
