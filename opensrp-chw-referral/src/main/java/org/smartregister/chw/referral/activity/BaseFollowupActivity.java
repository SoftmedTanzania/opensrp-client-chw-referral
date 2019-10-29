package org.smartregister.chw.referral.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.contract.BaseFollowupContract;
import org.smartregister.chw.referral.custom_views.BaseReferralFloatingMenu;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.interactor.BaseFollowupInteractor;
import org.smartregister.chw.referral.presenter.BaseFollowupPresenter;
import org.smartregister.chw.referral.util.Constants;
import org.smartregister.view.activity.BaseProfileActivity;

public class BaseFollowupActivity extends BaseProfileActivity implements BaseFollowupContract.View, BaseFollowupContract.Interactor, BaseFollowupContract.InteractorCallBack {
    protected MemberObject MEMBER_OBJECT;
    protected BaseFollowupContract.Presenter profilePresenter;
    protected TextView textViewName;
    protected TextView textViewGender;
    protected TextView textViewLocation;
    protected TextView textViewUniqueID;
    protected View view_last_visit_row;
    protected View view_most_due_overdue_row;
    protected View view_family_row;
    protected BaseReferralFloatingMenu baseReferralFloatingMenu;
    private String clientName;
    private String familyHeadName;
    private String familyHeadPhoneNumber;

    public static void startProfileActivity(Activity activity, MemberObject memberObject) {
        Intent intent = new Intent(activity, BaseReferralProfileActivity.class);
        intent.putExtra(Constants.REFERRAL_MEMBER_OBJECT.MEMBER_OBJECT, memberObject);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreation() {
        setContentView(R.layout.activity_followup);
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }

        toolbar.setNavigationOnClickListener(v -> BaseFollowupActivity.this.finish());
        appBarLayout = this.findViewById(R.id.collapsing_toolbar_appbarlayout);
        if (Build.VERSION.SDK_INT >= 21) {
            appBarLayout.setOutlineProvider(null);
        }

        textViewName = findViewById(R.id.textview_name);
        textViewGender = findViewById(R.id.textview_gender);
        textViewLocation = findViewById(R.id.textview_address);
        textViewUniqueID = findViewById(R.id.textview_id);
        view_last_visit_row = findViewById(R.id.view_last_visit_row);
        view_most_due_overdue_row = findViewById(R.id.view_most_due_overdue_row);
        view_family_row = findViewById(R.id.view_family_row);


        MEMBER_OBJECT = (MemberObject) getIntent().getSerializableExtra(Constants.REFERRAL_MEMBER_OBJECT.MEMBER_OBJECT);

        initializePresenter();

        profilePresenter.fillProfileData(MEMBER_OBJECT);

        setupViews();
    }

    @Override
    protected void setupViews() {
        initializeFloatingMenu();
    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return null;
    }

    @Override
    protected void fetchProfileData() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.title_layout) {
            onBackPressed();
        }
    }

    @Override
    protected void initializePresenter() {
        profilePresenter = new BaseFollowupPresenter(this, new BaseFollowupInteractor(), MEMBER_OBJECT);
        fetchProfileData();
    }

    public void initializeFloatingMenu() {
        if (StringUtils.isNotBlank(MEMBER_OBJECT.getPhoneNumber()) || StringUtils.isNotBlank(familyHeadPhoneNumber)) {
            baseReferralFloatingMenu = new BaseReferralFloatingMenu(this, clientName, MEMBER_OBJECT.getPhoneNumber(), familyHeadName, familyHeadPhoneNumber);
            baseReferralFloatingMenu.setGravity(Gravity.BOTTOM | Gravity.END);
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            addContentView(baseReferralFloatingMenu, linearLayoutParams);
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void setProfileViewWithData() {
        int age = new Period(new DateTime(MEMBER_OBJECT.getAge()), new DateTime()).getYears();
        textViewName.setText(String.format("%s %s %s, %d", MEMBER_OBJECT.getFirstName(),
                MEMBER_OBJECT.getMiddleName(), MEMBER_OBJECT.getLastName(), age));
        textViewGender.setText(MEMBER_OBJECT.getGender());
        textViewLocation.setText(MEMBER_OBJECT.getAddress());
        textViewUniqueID.setText(MEMBER_OBJECT.getUniqueId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void saveFollowup(String jsonString, BaseFollowupContract.InteractorCallBack callBack) {

    }

    @Override
    public void onRegistrationSaved(String jsonString) {

    }
}
