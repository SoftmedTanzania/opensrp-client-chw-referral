package org.smartregister.chw.referral.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Menu;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.util.Constants;
import org.smartregister.chw.referral.util.JsonFormUtils;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.view.activity.SecuredActivity;
import org.smartregister.view.customcontrols.CustomFontTextView;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import timber.log.Timber;

public class ReferralDetailsViewActivity extends SecuredActivity {
    protected AppBarLayout appBarLayout;
    protected String startingActivity;
    private CommonPersonObjectClient personObjectClient;
    private CustomFontTextView clientName;
    private String clientAge;
    private CustomFontTextView careGiverName;
    private CustomFontTextView childName;
    private CustomFontTextView careGiverPhone;
    private CustomFontTextView clientReferralProblem;
    private CustomFontTextView referralDate;
    private CustomFontTextView referralFacility;
    private CustomFontTextView chwDetailsNames;
    private CustomFontTextView womanGa;
    private LinearLayout womanGaLayout;
    private LinearLayout careGiverLayout;
    private LinearLayout childNameLayout;
    private String name;
    private String baseEntityId;
    private MemberObject memberObject;
    private String familyHeadName;
    private String familyHeadPhoneNumber;

    public static void startReferralDetailsViewActivity(Activity activity, MemberObject memberObject) {
        Intent intent = new Intent(activity, ReferralDetailsViewActivity.class);
        intent.putExtra(Constants.REFERRAL_MEMBER_OBJECT.MEMBER_OBJECT, memberObject);
        activity.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    protected void onCreation() {
        setContentView(R.layout.referral_details_activity);
        inflateToolbar();
        memberObject = (MemberObject) getIntent().getSerializableExtra(Constants.REFERRAL_MEMBER_OBJECT.MEMBER_OBJECT);
        setUpViews();

    }

    @Override
    protected void onResumption() {
        //// TODO: 15/08/19
    }


    public CommonPersonObjectClient getPersonObjectClient() {
        return personObjectClient;
    }

    public MemberObject getMemberObject() {
        return memberObject;
    }

    public void setMemberObject(MemberObject memberObject) {
        this.memberObject = memberObject;
    }


    private void inflateToolbar() {
        Toolbar toolbar = findViewById(R.id.back_referrals_toolbar);
        CustomFontTextView toolBarTextView = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
            actionBar.setElevation(0);
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        toolBarTextView.setText(R.string.back_to_referrals);
        toolBarTextView.setOnClickListener(v -> finish());
        appBarLayout = findViewById(R.id.app_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout.setOutlineProvider(null);
        }

    }

    public void setUpViews() {
        clientName = findViewById(R.id.client_name);
        careGiverName = findViewById(R.id.care_giver_name);
        childName = findViewById(R.id.child_name);
        careGiverPhone = findViewById(R.id.care_giver_phone);
        clientReferralProblem = findViewById(R.id.client_referral_problem);
        chwDetailsNames = findViewById(R.id.chw_details_names);
        referralDate = findViewById(R.id.referral_date);
        referralFacility = findViewById(R.id.referral_facility);
        womanGaLayout = findViewById(R.id.woman_ga_layout);
        careGiverLayout = findViewById(R.id.care_giver_name_layout);
        childNameLayout = findViewById(R.id.child_name_layout);

        womanGa = findViewById(R.id.woman_ga);
        getReferralDetails();
    }

    private void getReferralDetails() {
        if (memberObject != null) {
            updateProblemDisplay();
            clientAge = String.valueOf(new Period(new DateTime(memberObject.getAge()), new DateTime()).getYears());
            clientName.setText(String.format(Locale.getDefault(), "%s %s %s, %s", memberObject.getFirstName(),
                    memberObject.getMiddleName(), memberObject.getLastName(), clientAge));


            DateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            BigDecimal dateTimestamp = new BigDecimal(memberObject.getChwReferralDate());

            Calendar referralDateCalendar = Calendar.getInstance();
            referralDateCalendar.setTimeInMillis(dateTimestamp.longValue());
            referralDate.setText(dateFormatter.format(referralDateCalendar.getTime()));


            String test = new Gson().toJson(memberObject.getChwReferralHf());
            Timber.e("Coze test = "+test);
            referralFacility.setText(memberObject.getChwReferralHf());


            careGiverName.setText(String.format("CG : %s", memberObject.getPrimaryCareGiver()));
            careGiverPhone.setText(getFamilyMemberContacts().isEmpty() || getFamilyMemberContacts() == null ? getString(R.string.phone_not_provided) : getFamilyMemberContacts());

            updateProblemDisplay();
        }
    }



    private void updateProblemDisplay() {
        JSONArray problemsArray;
        try {
            problemsArray = new JSONArray(memberObject.getProblem());
            StringBuilder problemNamesStringBuilder = new StringBuilder();
            for(int i=0;i<problemsArray.length();i++){
                problemNamesStringBuilder.append(problemsArray.getString(i));
                problemNamesStringBuilder.append(",");
            }
            String problemsStrings = problemNamesStringBuilder.toString();
            problemsStrings = problemsStrings.substring(0, problemsStrings.length() - 1);

            clientReferralProblem.setText(problemsStrings);
        } catch (JSONException e) {
            Timber.e(e);
            clientReferralProblem.setText(memberObject.getProblem());
        }

        if(!StringUtils.isEmpty(memberObject.getProblemOther())){
            clientReferralProblem.append(", "+memberObject.getProblemOther());
        }


    }

    private String getFamilyMemberContacts() {
        String phoneNumber = "";
        String familyPhoneNumber = memberObject.getPhoneNumber();
        String familyPhoneNumberOther = memberObject.getOtherPhoneNumber();
        if (StringUtils.isNoneEmpty(familyPhoneNumber)) {
            phoneNumber = familyPhoneNumber;
        } else if (StringUtils.isEmpty(familyPhoneNumber) && StringUtils.isNoneEmpty(familyPhoneNumberOther)) {
            phoneNumber = familyPhoneNumberOther;
        } else if (StringUtils.isNoneEmpty(familyPhoneNumber) && StringUtils.isNoneEmpty(familyPhoneNumberOther)) {
            phoneNumber = familyPhoneNumber + ", " + familyPhoneNumberOther;
        } else if (StringUtils.isNoneEmpty(memberObject.getFamilyHeadPhoneNumber())) {
            phoneNumber = memberObject.getFamilyHeadPhoneNumber();
        }

        return phoneNumber;
    }

    public String getBaseEntityId() {
        return baseEntityId;
    }

}