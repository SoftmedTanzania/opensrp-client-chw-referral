package org.smartregister.chw.referral.custom_views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.fragment.BaseReferralCallDialogFragment;

public class BaseReferralFloatingMenu extends LinearLayout implements View.OnClickListener {
    private String phoneNumber;
    private String familyHeadName;
    private String familyHeadPhone;
    private String clientName;

    public BaseReferralFloatingMenu(Context context, String referralClientName, String malariaClientPhone, String clientFamilyHeadName, String clientFamilyHeadPhone) {
        super(context);
        initUi();
        clientName = referralClientName;
        phoneNumber = malariaClientPhone;
        familyHeadName = clientFamilyHeadName;
        familyHeadPhone = clientFamilyHeadPhone;
    }

    public BaseReferralFloatingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUi();
    }

    public BaseReferralFloatingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUi();
    }

    protected void initUi() {
        inflate(getContext(), R.layout.view_referral_call_floating_menu, this);
        FloatingActionButton fab = findViewById(R.id.referral_fab);
        if (fab != null)
            fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.referral_fab) {
            Activity activity = (Activity) getContext();
            BaseReferralCallDialogFragment.launchDialog(activity, clientName, phoneNumber, familyHeadName, familyHeadPhone);
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFamilyHeadName() {
        return familyHeadName;
    }

    public String getFamilyHeadPhone() {
        return familyHeadPhone;
    }

    public String getClientName() {
        return clientName;
    }
}