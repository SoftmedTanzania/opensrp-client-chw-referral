package org.smartregister.chw.referral.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.json.JSONArray;
import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.contract.BaseReferralProfileContract;
import org.smartregister.chw.referral.custom_views.BaseReferralFloatingMenu;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.interactor.BaseReferralProfileInteractor;
import org.smartregister.chw.referral.presenter.BaseReferralProfilePresenter;
import org.smartregister.chw.referral.util.Constants;
import org.smartregister.chw.referral.util.Util;
import org.smartregister.domain.AlertStatus;
import org.smartregister.view.activity.BaseProfileActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;


public class BaseReferralProfileActivity extends BaseProfileActivity implements BaseReferralProfileContract.View, BaseReferralProfileContract.InteractorCallBack {
    protected MemberObject memberObject;
    protected BaseReferralProfileContract.Presenter profilePresenter;
    protected TextView textViewName;
    protected TextView textViewFacilityName;
    protected TextView textViewReferralDate;
    protected TextView textViewReasonForReferral;
    protected TextView textViewAppointmentDate;
    protected TextView textViewIndicators;
    protected TextView textViewGender;
    protected TextView textViewLocation;
    protected TextView textViewUniqueID;
    protected TextView textViewRecordIssueReferral;
    protected TextView textViewRecordAnc;
    protected TextView textViewAncVisitNotDone;
    protected TextView textViewReferralServiceName;
    protected View view_last_visit_row;
    protected View view_most_due_overdue_row;
    protected View view_family_row;
    protected RelativeLayout rlLastVisit;
    protected RelativeLayout rlUpcomingServices;
    protected RelativeLayout rlFamilyServicesDue;
    protected BaseReferralFloatingMenu baseReferralFloatingMenu;
    private TextView tvUpComingServices;
    private TextView tvFamilyStatus;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
    private ProgressBar progressBar;
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
        setContentView(R.layout.activity_referral_profile);
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }

        toolbar.setNavigationOnClickListener(v -> BaseReferralProfileActivity.this.finish());
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
        tvUpComingServices = findViewById(R.id.textview_name_due);
        tvFamilyStatus = findViewById(R.id.textview_family_has);
        rlLastVisit = findViewById(R.id.rlLastVisit);
        rlUpcomingServices = findViewById(R.id.rlUpcomingServices);
        rlFamilyServicesDue = findViewById(R.id.rlFamilyServicesDue);

        textViewFacilityName = findViewById(R.id.facility_name);
        textViewReferralDate = findViewById(R.id.referral_date);
        textViewReasonForReferral = findViewById(R.id.reason_for_referral);
        textViewAppointmentDate = findViewById(R.id.appointment_date);
        textViewIndicators = findViewById(R.id.indicators);
        textViewReferralServiceName = findViewById(R.id.service_background);

        progressBar = findViewById(R.id.progress_bar);

        findViewById(R.id.rlLastReferral).setOnClickListener(this);
        findViewById(R.id.rlLastVisit).setOnClickListener(this);
        findViewById(R.id.rlUpcomingServices).setOnClickListener(this);
        findViewById(R.id.rlFamilyServicesDue).setOnClickListener(this);

        textViewRecordIssueReferral = findViewById(R.id.textview_issue_referral);
        textViewRecordIssueReferral.setOnClickListener(this);

        textViewRecordAnc = findViewById(R.id.textview_record_anc);
        textViewRecordAnc.setOnClickListener(this);

        textViewAncVisitNotDone = findViewById(R.id.textview_anc_visit_not_done);
        textViewAncVisitNotDone.setOnClickListener(this);

        memberObject = (MemberObject) getIntent().getSerializableExtra(Constants.REFERRAL_MEMBER_OBJECT.MEMBER_OBJECT);

        initializePresenter();

        profilePresenter.fillProfileData(memberObject);


        setupViews();
    }

    @Override
    protected void setupViews() {
        initializeFloatingMenu();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.title_layout) {
            onBackPressed();
        } else if (id == R.id.rlLastVisit) {
            this.openMedicalHistory();
        } else if (id == R.id.rlUpcomingServices) {
            this.openUpcomingService();
        } else if (id == R.id.rlFamilyServicesDue) {
            this.openFamilyDueServices();
        } else if (id == R.id.rlLastReferral) {
            this.openReferralHistory();
        }
    }

    @Override
    protected void initializePresenter() {
        showProgressBar(true);
        profilePresenter = new BaseReferralProfilePresenter(this, new BaseReferralProfileInteractor(), memberObject);
        fetchProfileData();
        profilePresenter.refreshProfileBottom();
    }

    public void initializeFloatingMenu() {
        if (StringUtils.isNotBlank(memberObject.getPhoneNumber()) || StringUtils.isNotBlank(familyHeadPhoneNumber)) {
            baseReferralFloatingMenu = new BaseReferralFloatingMenu(this, clientName, memberObject.getPhoneNumber(), familyHeadName, familyHeadPhoneNumber);
            baseReferralFloatingMenu.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            addContentView(baseReferralFloatingMenu, linearLayoutParams);
        }
    }

    @Override
    public void setProfileViewWithData() {
        int age = new Period(new DateTime(memberObject.getAge()), new DateTime()).getYears();
        textViewName.setText(String.format(Locale.getDefault(), "%s %s %s, %d", memberObject.getFirstName(),
                memberObject.getMiddleName(), memberObject.getLastName(), age));
        textViewGender.setText(memberObject.getGender());
        textViewLocation.setText(memberObject.getAddress());
        textViewUniqueID.setText(memberObject.getUniqueId());

        textViewFacilityName.setText(memberObject.getChwReferralHf());
        textViewReferralDate.setText(memberObject.getChwReferralDate());
        //TODO fix this area
//        textViewReasonForReferral.setText(memberObject.getChwReferralReason());
        textViewAppointmentDate.setText(memberObject.getReferralAppointmentDate());

        if (StringUtils.isNotBlank(memberObject.getFamilyHead()) && memberObject.getFamilyHead().equals(memberObject.getBaseEntityId())) {
            findViewById(R.id.family_head).setVisibility(View.VISIBLE);
        }
        if (StringUtils.isNotBlank(memberObject.getPrimaryCareGiver()) && memberObject.getPrimaryCareGiver().equals(memberObject.getBaseEntityId())) {
            findViewById(R.id.primary_caregiver).setVisibility(View.VISIBLE);
        }
//TODO fix this area
//        try {
//            JSONArray idsArray = new JSONArray(memberObject.getProblemIds());
//
//            StringBuilder referralIndicatorsStringBuilder = new StringBuilder();
//            for (int i = 0; i < idsArray.length(); i++) {
//                referralIndicatorsStringBuilder.append(" - ");
//                referralIndicatorsStringBuilder.append(profilePresenter.getIndicatorNameById(idsArray.getString(i)));
//                referralIndicatorsStringBuilder.append("\n");
//            }
//            textViewIndicators.setText(referralIndicatorsStringBuilder.toString());
//
//        } catch (Exception e) {
//            Timber.e(e);
//        }

        textViewReferralServiceName.setText(memberObject.getChwReferralService());
    }

    @Override
    public void hideView() {
        textViewRecordIssueReferral.setVisibility(View.GONE);
    }

    @Override
    public void setDueColor() {
        textViewRecordIssueReferral.setBackground(getResources().getDrawable(R.drawable.record_btn_selector));
    }

    @Override
    public void setOverDueColor() {
        textViewRecordIssueReferral.setBackground(getResources().getDrawable(R.drawable.record_btn_selector_overdue));
    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return null;
    }

    @Override
    protected void fetchProfileData() {
        //fetch profile data
    }

    @Override
    public void showProgressBar(boolean status) {
        progressBar.setVisibility(status ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void refreshMedicalHistory(boolean hasHistory) {
        showProgressBar(false);
        rlLastVisit.setVisibility(hasHistory ? View.VISIBLE : View.GONE);
    }

    @Override
    public void refreshUpComingServicesStatus(String service, AlertStatus status, Date date) {
        showProgressBar(false);
        if (status == AlertStatus.complete)
            return;

        view_most_due_overdue_row.setVisibility(View.VISIBLE);
        rlUpcomingServices.setVisibility(View.VISIBLE);

        if (status == AlertStatus.upcoming) {
            tvUpComingServices.setText(Util.fromHtml(getString(R.string.vaccine_service_upcoming, service, dateFormat.format(date))));
        } else {
            tvUpComingServices.setText(Util.fromHtml(getString(R.string.vaccine_service_due, service, dateFormat.format(date))));
        }
    }

    @Override
    public void refreshFamilyStatus(AlertStatus status) {
        showProgressBar(false);
        view_family_row.setVisibility(View.VISIBLE);
        rlFamilyServicesDue.setVisibility(View.VISIBLE);

        if (status == AlertStatus.complete) {
            tvFamilyStatus.setText(getString(R.string.family_has_nothing_due));
        } else if (status == AlertStatus.normal) {
            tvFamilyStatus.setText(getString(R.string.family_has_services_due));
        } else if (status == AlertStatus.urgent) {
            tvFamilyStatus.setText(Util.fromHtml(getString(R.string.family_has_service_overdue)));
        }
    }

    @Override
    public void openMedicalHistory() {
        //implement
    }

    @Override
    public void openReferralHistory() {
        //implement
    }

    @Override
    public void openUpcomingService() {
        //implement
    }

    @Override
    public void openFamilyDueServices() {
        //implement
    }
}
