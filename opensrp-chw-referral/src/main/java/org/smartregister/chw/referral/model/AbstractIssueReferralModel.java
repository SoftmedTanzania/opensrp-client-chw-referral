package org.smartregister.chw.referral.model;

import androidx.lifecycle.ViewModel;

import org.smartregister.chw.referral.contract.BaseIssueReferralContract;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.domain.ReferralServiceObject;

public abstract class AbstractIssueReferralModel extends ViewModel implements BaseIssueReferralContract.Model {
    public MemberObject memberObject;
    public ReferralServiceObject referralService;
    public ReferralServiceObject selectedReferralService = null;
}
