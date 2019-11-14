package org.smartregister.chw.referral.contract;

import android.content.Context;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.smartregister.chw.referral.domain.MemberObject;

import java.util.Collection;
import java.util.List;

public interface BaseReferralHistoryContract {
    interface View {
        Presenter presenter();

        Context getCurrentContext();

        void setProfileViewWithData(MemberObject memberObject);

        void setReferralHistoryData(@NotNull Collection<Object> referrals);
    }

    interface Presenter {
        void fillClientData(@Nullable MemberObject memberObject);

        @Nullable
        BaseReferralHistoryContract.View getView();

        String getMainTable();

        String getMainCondition();
    }

    interface Model {
        List<Object> getReferrals(String baseEntityId);
    }


}