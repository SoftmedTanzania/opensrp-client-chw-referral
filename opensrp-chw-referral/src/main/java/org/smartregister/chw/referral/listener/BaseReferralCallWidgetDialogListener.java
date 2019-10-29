package org.smartregister.chw.referral.listener;


import android.view.View;

import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.fragment.BaseReferralCallDialogFragment;
import org.smartregister.chw.referral.util.Util;

import timber.log.Timber;

public class BaseReferralCallWidgetDialogListener implements View.OnClickListener {

    private BaseReferralCallDialogFragment callDialogFragment;

    public BaseReferralCallWidgetDialogListener(BaseReferralCallDialogFragment dialogFragment) {
        callDialogFragment = dialogFragment;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.malaria_call_close) {
            callDialogFragment.dismiss();
        } else if (i == R.id.referral_call_head_phone) {
            try {
                String phoneNumber = (String) v.getTag();
                Util.launchDialer(callDialogFragment.getActivity(), callDialogFragment, phoneNumber);
                callDialogFragment.dismiss();
            } catch (Exception e) {
                Timber.e(e);
            }
        } else if (i == R.id.call_referral_client_phone) {
            try {
                String phoneNumber = (String) v.getTag();
                Util.launchDialer(callDialogFragment.getActivity(), callDialogFragment, phoneNumber);
                callDialogFragment.dismiss();
            } catch (Exception e) {
                Timber.e(e);
            }
        }
    }
}
