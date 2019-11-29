package org.smartregister.chw.referral.contract;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import android.content.Context;

import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.domain.ReferralServiceIndicatorObject;
import org.smartregister.chw.referral.domain.ReferralServiceObject;
import org.smartregister.domain.Location;
import org.smartregister.view.contract.BaseRegisterContract;

import java.util.List;

public interface BaseIssueReferralContract {

    interface View {
        Presenter presenter();

        Context getCurrentContext();

        void setProfileViewWithData();
    }

    interface Presenter extends BaseRegisterContract.Presenter {
        <T extends ViewModel & Model> Class<T> getViewModel();

        boolean validateValues(MemberObject memberObject);

        String getMainCondition();

        String getMainTable();

        void fillClientData(@Nullable MemberObject memberObject);

        void initializeMemberObject(MemberObject memberObject);

        void saveForm(String jsonString);
    }

    interface Model {
        String getLocationId(String locationName);

        JSONObject getFormWithValuesAsJson(String formName, String entityId,
                                           String currentLocationId, MemberObject memberObject) throws Exception;

        String mainSelect(String tableName, String mainCondition);

        LiveData<List<Location>> getHealthFacilities();

        LiveData<List<ReferralServiceObject>> getReferralServicesList(List<String> referralServiceIds);

        List<ReferralServiceIndicatorObject> getIndicatorsByServiceId(String serviceId);

    }

    interface Interactor {

        void onDestroy(boolean isChangingConfiguration);

        void saveRegistration(String jsonString, final InteractorCallBack callBack);

    }

    interface InteractorCallBack {

        void onUniqueIdFetched(Triple<String, String, String> triple, String entityId);

        void onNoUniqueId();

        void onRegistrationSaved();

    }
}
