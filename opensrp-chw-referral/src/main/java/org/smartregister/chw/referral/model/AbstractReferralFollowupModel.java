package org.smartregister.chw.referral.model;

import androidx.lifecycle.ViewModel;

import org.smartregister.chw.referral.contract.BaseFollowupContract;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.domain.ReferralFollowupObject;

public abstract class AbstractReferralFollowupModel extends ViewModel implements BaseFollowupContract.Model {
    public MemberObject memberObject;
    public ReferralFollowupObject referralFollowupObject;
}
