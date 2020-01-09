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

import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.util.Constants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.view.activity.SecuredActivity;
import org.smartregister.view.customcontrols.CustomFontTextView;

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
        setUpViews();
        memberObject = (MemberObject) getIntent().getSerializableExtra(Constants.REFERRAL_MEMBER_OBJECT.MEMBER_OBJECT);

    }

    @Override
    protected void onResumption() {
        //// TODO: 15/08/19
    }


    public CommonPersonObjectClient getPersonObjectClient() {
        return personObjectClient;
    }

    public void setPersonObjectClient(CommonPersonObjectClient personObjectClient) {
        this.personObjectClient = personObjectClient;
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

        womanGaLayout = findViewById(R.id.woman_ga_layout);
        careGiverLayout = findViewById(R.id.care_giver_name_layout);
        childNameLayout = findViewById(R.id.child_name_layout);

        womanGa = findViewById(R.id.woman_ga);
//        getReferralDetails();
    }

    public String getStartingActivity() {
        return startingActivity;
    }

    public void setStartingActivity(String startingActivity) {
        this.startingActivity = startingActivity;
    }

//    private void getReferralDetails() {
//        if (getPersonObjectClient() != null && getTask() != null) {
//            updateProblemDisplay();
//            clientAge = (Utils.getTranslatedDate(Utils.getDuration(Utils.getValue(getPersonObjectClient().getColumnmaps(), DBConstants.KEY.DOB, false)), getBaseContext()));
//            clientName.setText(getString(R.string.client_name_age_suffix, name, clientAge));
//            referralDate.setText(org.smartregister.chw.core.utils.Utils.dd_MMM_yyyy.format(task.getExecutionStartDate().toDate()));
//
//            String parentFirstName = Utils.getValue(getPersonObjectClient().getColumnmaps(), ChildDBConstants.KEY.FAMILY_FIRST_NAME, true);
//            String parentLastName = Utils.getValue(getPersonObjectClient().getColumnmaps(), ChildDBConstants.KEY.FAMILY_LAST_NAME, true);
//            String parentMiddleName = Utils.getValue(getPersonObjectClient().getColumnmaps(), ChildDBConstants.KEY.FAMILY_MIDDLE_NAME, true);
//            String parentName = getString(R.string.care_giver_prefix, org.smartregister.util.Utils.getName(parentFirstName, parentMiddleName + " " + parentLastName));
//
//            //For PNC get children belonging to the woman
//            String childrenForPncWoman = getChildrenForPncWoman(getPersonObjectClient().entityId());
//            if (getTask().getFocus().equalsIgnoreCase(CoreConstants.TASKS_FOCUS.PNC_DANGER_SIGNS) &&
//                    StringUtils.isNoneEmpty(childrenForPncWoman)) {
//                childName.setText(childrenForPncWoman);
//                childNameLayout.setVisibility(View.VISIBLE);
//            }
//
//            //Hide Care giver for ANC referral
//            if (getTask().getFocus().equalsIgnoreCase(CoreConstants.TASKS_FOCUS.ANC_DANGER_SIGNS) ||
//                    getTask().getFocus().equalsIgnoreCase(CoreConstants.TASKS_FOCUS.PNC_DANGER_SIGNS)) {
//                careGiverLayout.setVisibility(View.GONE);
//            }
//
//            careGiverName.setText(parentName);
//            careGiverPhone.setText(getFamilyMemberContacts().isEmpty() || getFamilyMemberContacts() == null ? getString(R.string.phone_not_provided) : getFamilyMemberContacts());
//
//            chwDetailsNames.setText(getTask().getRequester());
//
//            addGaDisplay();
//        }
//    }
//
//
//
//    private void updateProblemDisplay() {
//        if (CoreConstants.TASKS_FOCUS.ANC_DANGER_SIGNS.equals(getTask().getFocus())) {
//            clientReferralProblem.setText(getString(R.string.anc_danger_sign_prefix, getTask().getDescription()));
//        } else {
//            clientReferralProblem.setText(getTask().getDescription());
//        }
//    }
//
//    private String getFamilyMemberContacts() {
//        String phoneNumber = "";
//        String familyPhoneNumber = Utils.getValue(getPersonObjectClient().getColumnmaps(), ChildDBConstants.KEY.FAMILY_MEMBER_PHONENUMBER, true);
//        String familyPhoneNumberOther = Utils.getValue(getPersonObjectClient().getColumnmaps(), ChildDBConstants.KEY.FAMILY_MEMBER_PHONENUMBER_OTHER, true);
//        if (StringUtils.isNoneEmpty(familyPhoneNumber)) {
//            phoneNumber = familyPhoneNumber;
//        } else if (StringUtils.isEmpty(familyPhoneNumber) && StringUtils.isNoneEmpty(familyPhoneNumberOther)) {
//            phoneNumber = familyPhoneNumberOther;
//        } else if (StringUtils.isNoneEmpty(familyPhoneNumber) && StringUtils.isNoneEmpty(familyPhoneNumberOther)) {
//            phoneNumber = familyPhoneNumber + ", " + familyPhoneNumberOther;
//        } else if (StringUtils.isNoneEmpty(getFamilyHeadPhoneNumber())) {
//            phoneNumber = getFamilyHeadPhoneNumber();
//        }
//
//        return phoneNumber;
//    }

    public String getBaseEntityId() {
        return baseEntityId;
    }

}