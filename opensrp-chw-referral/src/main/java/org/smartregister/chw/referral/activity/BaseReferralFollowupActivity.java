package org.smartregister.chw.referral.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.contract.BaseFollowupContract;
import org.smartregister.chw.referral.databinding.ActivityFollowupBinding;
import org.smartregister.chw.referral.domain.FollowupFeedbackObject;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.interactor.BaseReferralFollowupInteractor;
import org.smartregister.chw.referral.model.AbstractReferralFollowupModel;
import org.smartregister.chw.referral.model.BaseReferralFollowupModel;
import org.smartregister.chw.referral.presenter.BaseReferralFollowupPresenter;
import org.smartregister.chw.referral.provider.FollowupFeedbackProvider;
import org.smartregister.chw.referral.util.Constants;

import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class BaseReferralFollowupActivity extends AppCompatActivity implements BaseFollowupContract.View {
    protected MemberObject memberObject;
    protected String formName;
    protected BaseFollowupContract.Presenter presenter;
    protected TextView textViewName;
    protected TextView textViewGender;
    protected TextView textViewLocation;
    protected TextView textViewUniqueID;
    protected TextView textViewReferralDate;
    protected TextView textViewFollowUpReason;
    protected Button buttonSave;
    protected Spinner spinnerFeedback;
    protected View view_family_row;
    private AbstractReferralFollowupModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.memberObject = (MemberObject) this.getIntent().getSerializableExtra(Constants.REFERRAL_MEMBER_OBJECT.MEMBER_OBJECT);
        this.formName = this.getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.REFERRAL_FOLLOWUP_FORM_NAME);

        //initializing the presenter
        presenter = presenter();

        //initializing the viewModel obtained from presenter,
        // this viewModel must extend #AbstractIssueReferralModel and implements BaseIssueReferralContract.Model
        viewModel = ViewModelProviders.of(this).get(presenter.getViewModel());

        ActivityFollowupBinding mBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_followup);
        mBinding.setViewModel(viewModel);

        setContentView(mBinding.getRoot());

        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }

        toolbar.setNavigationOnClickListener(v -> BaseReferralFollowupActivity.this.finish());
        AppBarLayout appBarLayout = this.findViewById(R.id.collapsing_toolbar_appbarlayout);
        if (Build.VERSION.SDK_INT >= 21) {
            appBarLayout.setOutlineProvider(null);
        }

        viewModel.memberObject = memberObject;

        textViewGender = findViewById(R.id.textview_gender);
        textViewName = findViewById(R.id.textview_name);
        textViewLocation = findViewById(R.id.textview_address);
        textViewUniqueID = findViewById(R.id.textview_id);
        view_family_row = findViewById(R.id.view_family_row);
        textViewReferralDate = findViewById(R.id.referral_date);
        textViewFollowUpReason = findViewById(R.id.followUp_reason);
        spinnerFeedback = findViewById(R.id.spinnerClientAvailable);
        buttonSave = findViewById(R.id.save_button);

        memberObject = (MemberObject) getIntent().getSerializableExtra(Constants.REFERRAL_MEMBER_OBJECT.MEMBER_OBJECT);
        presenter.fillProfileData(memberObject);
    }


    @Override
    public BaseFollowupContract.Presenter presenter() {
        return new BaseReferralFollowupPresenter(this, BaseReferralFollowupModel.class, new BaseReferralFollowupInteractor());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void setProfileViewWithData() {
        int age = new Period(new DateTime(memberObject.getAge()), new DateTime()).getYears();
        textViewLocation.setText(memberObject.getAddress());
        textViewName.setText(String.format("%s %s %s, %d", memberObject.getFirstName(),
                memberObject.getMiddleName(), memberObject.getLastName(), age));
        textViewUniqueID.setText(memberObject.getUniqueId());
        textViewGender.setText(memberObject.getGender());
        textViewFollowUpReason.setText(memberObject.getChwReferralReason());
        textViewReferralDate.setText(memberObject.getChwReferralDate());

        final List<FollowupFeedbackObject> followupFeedbacks = viewModel.getFollowupFeedbackList();

        FollowupFeedbackProvider followupFeedbackProvider = new FollowupFeedbackProvider(BaseReferralFollowupActivity.this, Objects.requireNonNull(followupFeedbacks));
        spinnerFeedback.setAdapter(followupFeedbackProvider);

        spinnerFeedback.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    viewModel.setReferralFollowupFeedback(followupFeedbacks.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Implement
            }
        });

        buttonSave.setOnClickListener(view -> {
            TextView conditionDuringVisit = findViewById(R.id.client_condition);
            viewModel.setClientConditionDuringTheVisit(conditionDuringVisit.getText().toString());
            viewModel.saveDataToReferralFollowupObject();

            if (presenter.validateValues(viewModel.referralFollowupObject)) {
                JSONObject jsonForm = null;
                try {
                    jsonForm = viewModel.getFormWithValuesAsJson(formName, memberObject.getBaseEntityId(), getLocationID(), viewModel.referralFollowupObject);
                } catch (Exception e) {
                    Timber.e(e);
                }
                presenter.saveForm(Objects.requireNonNull(jsonForm).toString());
                Toast.makeText(this, getResources().getString(R.string.successful_issued_referral), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    public Context getCurrentContext() {
        return BaseReferralFollowupActivity.this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onFollowupSaved() {
        //Implement
    }

    protected String getLocationID() {
        return org.smartregister.Context.getInstance().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
    }
}
