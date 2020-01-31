package org.smartregister.chw.referral.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.MenuRes;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.Context;
import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.contract.BaseReferralRegisterContract;
import org.smartregister.chw.referral.fragment.BaseFollowupRegisterFragment;
import org.smartregister.chw.referral.fragment.BaseReferralRegisterFragment;
import org.smartregister.chw.referral.listener.ReferralBottomNavigationListener;
import org.smartregister.chw.referral.model.BaseReferralRegisterModel;
import org.smartregister.chw.referral.presenter.BaseReferralRegisterPresenter;
import org.smartregister.chw.referral.util.Constants;
import org.smartregister.helper.BottomNavigationHelper;
import org.smartregister.listener.BottomNavigationListener;
import org.smartregister.view.activity.BaseRegisterActivity;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.Arrays;
import java.util.List;

public class BaseReferralRegisterActivity extends BaseRegisterActivity implements BaseReferralRegisterContract.View {

    protected String BASE_ENTITY_ID;
    protected String ACTION;
    protected String FORM_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BASE_ENTITY_ID = getIntent().getStringExtra(Constants.ActivityPayload.BASE_ENTITY_ID);
        ACTION = getIntent().getStringExtra(Constants.ActivityPayload.ACTION);
        FORM_NAME = getIntent().getStringExtra(Constants.ActivityPayload.REFERRAL_FORM_NAME);
        onStartActivityWithAction();
    }

    /**
     * Process a payload when an activity is started with an action
     */
    protected void onStartActivityWithAction() {
        if (FORM_NAME != null && ACTION != null) {
            startFormActivity(FORM_NAME, BASE_ENTITY_ID, null);
        }
    }

    @Override
    public void startRegistration() {
        startFormActivity(FORM_NAME, null, null);
    }

    @Override
    public void startFormActivity(String formName, String entityId, String metaData) {
        //Implement
    }

    protected String getLocationID() {
        return Context.getInstance().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
    }

    @Override
    public void startFormActivity(JSONObject jsonForm) {
        Intent intent = new Intent(this, getFamilyFormActivity());
        intent.putExtra(Constants.JsonFormExtra.JSON, jsonForm.toString());

        startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    public Class getFamilyFormActivity() {
        return BaseReferralRegisterActivity.class;
    }


    @Override
    protected void onActivityResultExtended(int requestCode, int resultCode, Intent data) {
        //Implement
    }

    @Override
    public List<String> getViewIdentifiers() {
        return Arrays.asList(Constants.Configuration.ISSUE_REFERRAL);
    }


    /**
     * Override this to subscribe to bottom navigation
     */
    @Override
    protected void registerBottomNavigation() {
        bottomNavigationHelper = new BottomNavigationHelper();
        bottomNavigationView = findViewById(org.smartregister.R.id.bottom_navigation);

        if (bottomNavigationView != null) {
            bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
            bottomNavigationView.getMenu().removeItem(org.smartregister.R.id.action_clients);
            bottomNavigationView.getMenu().removeItem(R.id.action_register);
            bottomNavigationView.getMenu().removeItem(org.smartregister.R.id.action_search);
            bottomNavigationView.getMenu().removeItem(org.smartregister.R.id.action_library);

            bottomNavigationView.inflateMenu(getMenuResource());
            bottomNavigationHelper.disableShiftMode(bottomNavigationView);

            BottomNavigationListener referralBottomNavigationListener = getBottomNavigation(this);
            bottomNavigationView.setOnNavigationItemSelectedListener(referralBottomNavigationListener);

        }
    }

    @MenuRes
    public int getMenuResource() {
        return R.menu.bottom_nav_referral_menu;
    }

    public BottomNavigationListener getBottomNavigation(Activity activity) {
        return new ReferralBottomNavigationListener(activity);
    }

    @Override
    protected void initializePresenter() {
        presenter = new BaseReferralRegisterPresenter(this, new BaseReferralRegisterModel());
    }

    @Override
    protected BaseRegisterFragment getRegisterFragment() {
        return new BaseReferralRegisterFragment();
    }

    @Override
    protected Fragment[] getOtherFragments() {
        Fragment fg = new BaseFollowupRegisterFragment();
        return new Fragment[]{fg};
    }

    @Override
    public BaseReferralRegisterContract.Presenter presenter() {
        return (BaseReferralRegisterContract.Presenter) presenter;
    }
}
