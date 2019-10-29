package org.smartregister.chw.referral.listener;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import org.smartregister.chw.referral.R;
import org.smartregister.listener.BottomNavigationListener;
import org.smartregister.view.activity.BaseRegisterActivity;

public class ReferralBottomNavigationListener extends BottomNavigationListener {
    private Activity context;

    public ReferralBottomNavigationListener(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        super.onNavigationItemSelected(item);

        BaseRegisterActivity baseRegisterActivity = (BaseRegisterActivity) context;

        if (item.getItemId() == R.id.action_sent_referrals) {
            baseRegisterActivity.switchToBaseFragment();
        } else if (item.getItemId() == R.id.action_received_referrals) {
            baseRegisterActivity.switchToFragment(1);
        } else if (item.getItemId() == R.id.action_scan_qr) {
            baseRegisterActivity.startQrCodeScanner();
        }

        return true;
    }
}