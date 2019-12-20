package org.smartregister.chw.referral.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.nerdstone.neatformcore.domain.builders.FormBuilder;
import com.nerdstone.neatformcore.form.json.JsonFormBuilder;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.contract.BaseFollowupContract;
import org.smartregister.chw.referral.databinding.ActivityFollowupBinding;
import org.smartregister.chw.referral.domain.FollowupFeedbackObject;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.domain.NeatFormMetaData;
import org.smartregister.chw.referral.domain.NeatFormOption;
import org.smartregister.chw.referral.interactor.BaseReferralFollowupInteractor;
import org.smartregister.chw.referral.model.AbstractReferralFollowupModel;
import org.smartregister.chw.referral.model.BaseReferralFollowupModel;
import org.smartregister.chw.referral.presenter.BaseReferralFollowupPresenter;
import org.smartregister.chw.referral.util.Constants;
import org.smartregister.chw.referral.util.JsonFormUtils;

import java.util.ArrayList;
import java.util.List;

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
    protected View view_family_row;
    protected boolean injectValuesFromDb;
    private AbstractReferralFollowupModel viewModel;
    private JSONObject jsonForm = null;
    private FormBuilder formBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.memberObject = (MemberObject) this.getIntent().getSerializableExtra(Constants.REFERRAL_MEMBER_OBJECT.MEMBER_OBJECT);
        this.formName = this.getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.REFERRAL_FOLLOWUP_FORM_NAME);
        this.injectValuesFromDb = this.getIntent().getBooleanExtra(Constants.ACTIVITY_PAYLOAD.INJECT_VALUES_FROM_DB, true);

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
        buttonSave = findViewById(R.id.save_button);

        memberObject = (MemberObject) getIntent().getSerializableExtra(Constants.REFERRAL_MEMBER_OBJECT.MEMBER_OBJECT);
        presenter.fillProfileData(memberObject);

        try {
            presenter.initializeMemberObject(viewModel.memberObject);
        } catch (Exception e) {
            Timber.e(e);
        }

        LinearLayout formLayout = findViewById(R.id.formLayout);

        Timber.i("Form name  = %s", formName);
        try {
            jsonForm = JsonFormUtils.getFormAsJson(formName);
            JsonFormUtils.addFormMetadata(jsonForm, memberObject.getBaseEntityId(), getLocationID());
        } catch (Exception e) {
            Timber.e(e);
        }

        if (injectValuesFromDb) {
            if (jsonForm != null) {
                injectReferralFeedback(jsonForm);
                Timber.i("Form with injected values = %s", jsonForm);
            }
        }


        JsonFormBuilder jsonFormBuilder = null;
        if (jsonForm != null) {
            jsonFormBuilder = new JsonFormBuilder(jsonForm.toString(), this, formLayout);
        }

        formBuilder = jsonFormBuilder.buildForm(null, null);
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

        buttonSave.setOnClickListener(view -> {
            presenter.saveForm(formBuilder.getFormData(),jsonForm);
            Timber.e("Saved data = " + new Gson().toJson(formBuilder.getFormData()));
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

    private void injectReferralFeedback(JSONObject form) {
        JSONArray fields = null;
        try {
            fields = form.getJSONArray("steps").getJSONObject(0).getJSONArray("fields");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject feedbackField = null;
        for (int i = 0; i < (fields != null ? fields.length() : 0); i++) {
            try {
                if (fields.getJSONObject(i).getString("name").equals("chw_followup_feedback")) {
                    feedbackField = fields.getJSONObject(i);
                    break;
                }
            } catch (Exception e) {
                Timber.e(e);
            }
        }
        List<NeatFormOption> followupFeedbackNeatFormOptions = new ArrayList<>();
        final List<FollowupFeedbackObject> followupFeedbacks = viewModel.getFollowupFeedbackList();

        for (FollowupFeedbackObject followupFeedbackObject : followupFeedbacks) {
            NeatFormOption option = new NeatFormOption();
            option.name = followupFeedbackObject.getNameEn();
            option.text = followupFeedbackObject.getNameEn();

            NeatFormMetaData metaData = new NeatFormMetaData();
            metaData.openmrsEntity = "concept";
            metaData.openmrsEntityId = followupFeedbackObject.getId();
            metaData.openmrsEntityParent = "";
            option.neatFormMetaData = metaData;

            followupFeedbackNeatFormOptions.add(option);
        }
        try {
            if (feedbackField != null) {
                JSONArray optionsArray = new JSONArray(new Gson().toJson(followupFeedbackNeatFormOptions));

                Timber.e("Feedback options = %s", new Gson().toJson(followupFeedbacks));

                for (int i = 0; i < feedbackField.getJSONArray("options").length(); i++) {
                    optionsArray.put(feedbackField.getJSONArray("options").get(i));
                }
                feedbackField.put("options", optionsArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
