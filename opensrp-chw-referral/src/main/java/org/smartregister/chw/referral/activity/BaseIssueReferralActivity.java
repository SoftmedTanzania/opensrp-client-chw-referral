package org.smartregister.chw.referral.activity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.nerdstone.neatandroidstepper.core.domain.StepperActions;
import com.nerdstone.neatandroidstepper.core.model.StepperModel;
import com.nerdstone.neatandroidstepper.core.stepper.Step;
import com.nerdstone.neatandroidstepper.core.stepper.StepVerificationState;
import com.nerdstone.neatformcore.domain.builders.FormBuilder;
import com.nerdstone.neatformcore.domain.model.JsonFormStepBuilderModel;
import com.nerdstone.neatformcore.form.json.JsonFormBuilder;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.contract.BaseIssueReferralContract;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.domain.NeatFormMetaData;
import org.smartregister.chw.referral.domain.NeatFormOption;
import org.smartregister.chw.referral.domain.ReferralServiceIndicatorObject;
import org.smartregister.chw.referral.interactor.BaseIssueReferralInteractor;
import org.smartregister.chw.referral.model.AbstractIssueReferralModel;
import org.smartregister.chw.referral.model.BaseIssueReferralModel;
import org.smartregister.chw.referral.presenter.BaseIssueReferralPresenter;
import org.smartregister.chw.referral.util.Constants;
import org.smartregister.chw.referral.util.JsonFormUtils;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.domain.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

/**
 * Created by cozej4 on 2019-10-07.
 *
 * @cozej4 https://github.com/cozej4
 */

public class BaseIssueReferralActivity extends AppCompatActivity implements BaseIssueReferralContract.View, StepperActions {
    protected BaseIssueReferralContract.Presenter presenter;
    protected String baseEntityId;
    protected String serviceId;
    protected String action;
    protected String formName;
    protected boolean injectValuesFromDb;
    private AbstractIssueReferralModel viewModel;
    private FormBuilder formBuilder;
    private JSONObject jsonForm = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.baseEntityId = this.getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID);
        this.serviceId = this.getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.REFERRAL_SERVICE_IDS);
        this.action = this.getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.ACTION);
        this.formName = this.getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.REFERRAL_FORM_NAME);
        this.injectValuesFromDb = this.getIntent().getBooleanExtra(Constants.ACTIVITY_PAYLOAD.INJECT_VALUES_FROM_DB, false);

        try {
            this.jsonForm = new JSONObject(this.getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.JSON_FORM));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //initializing the presenter
        presenter = presenter();

        //initializing the viewModel obtained from presenter
        viewModel = ViewModelProviders.of(this).get(presenter.getViewModel());

        setContentView(R.layout.activity_referral_registration);

        getMemberObject();

        try {
            presenter.initializeMemberObject(viewModel.memberObject);
        } catch (Exception e) {
            Timber.e(e);
        }

        presenter.fillClientData(viewModel.memberObject);

        //Initialize the form using the form name
        if(jsonForm==null) {
            try {
                jsonForm = JsonFormUtils.getFormAsJson(formName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            JsonFormUtils.addFormMetadata(jsonForm, baseEntityId, getLocationID());
            int age = new Period(new DateTime(viewModel.memberObject.getAge()), new DateTime()).getYears();
            jsonForm.put("form", String.format(Locale.getDefault(), "%s %s %s, %d", viewModel.memberObject.getFirstName(),
                    viewModel.memberObject.getMiddleName(), viewModel.memberObject.getLastName(), age));
        } catch (Exception e) {
            Timber.e(e);
        }

        initializeHealthFacilitiesList(jsonForm);
        if (injectValuesFromDb) {
            if (jsonForm != null) {
                injectReferralProblems(jsonForm);
            }
        }
        Timber.i("Form with injected values = %s", jsonForm);

        LinearLayout formLayout = findViewById(R.id.formLayout);
        StepperModel stepperModel = new StepperModel.Builder()
                .exitButtonDrawableResource(R.drawable.ic_clear_white)
                .indicatorType(StepperModel.IndicatorType.DOT_INDICATOR)
                .toolbarColorResource(R.color.primary)
                .build();

        JsonFormStepBuilderModel jsonFormStepBuilderModel = new JsonFormStepBuilderModel.Builder(this, stepperModel).build();

        JsonFormBuilder jsonFormBuilder = null;
        if (jsonForm != null) {
            jsonFormBuilder = new JsonFormBuilder(jsonForm.toString(), this, formLayout);
        }
        formBuilder = jsonFormBuilder.buildForm(jsonFormStepBuilderModel, null);
        formLayout.addView(formBuilder.getNeatStepperLayout());
    }


    @Override
    public BaseIssueReferralContract.Presenter presenter() {
        return new BaseIssueReferralPresenter(baseEntityId, this, BaseIssueReferralModel.class, new BaseIssueReferralInteractor());
    }

    @Override
    public Context getCurrentContext() {
        return BaseIssueReferralActivity.this;
    }

    @Override
    public void setProfileViewWithData() {
        //Implement
    }

    protected String getLocationID() {
        return org.smartregister.Context.getInstance().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
    }


    private void injectReferralProblems(JSONObject form) {
        JSONArray fields = null;
        try {
            fields = form.getJSONArray("steps").getJSONObject(0).getJSONArray("fields");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject problems = null;
        for (int i = 0; i < (fields != null ? fields.length() : 0); i++) {
            try {
                if (fields.getJSONObject(i).getString("name").equals("problem")) {
                    problems = fields.getJSONObject(i);
                    break;
                }
            } catch (Exception e) {
                Timber.e(e);
            }
        }
        List<NeatFormOption> problemsOptions = new ArrayList<>();
        viewModel.referralService = viewModel.getReferralServicesList(serviceId);
        List<ReferralServiceIndicatorObject> indicatorsByServiceId = viewModel.getIndicatorsByServiceId(serviceId);
        Timber.i("referral problems from DB = %s", new Gson().toJson(indicatorsByServiceId));
        for (ReferralServiceIndicatorObject referralServiceIndicatorObject : indicatorsByServiceId) {
            NeatFormOption option = new NeatFormOption();
            option.name = referralServiceIndicatorObject.getNameEn();
            option.text = referralServiceIndicatorObject.getNameEn();

            NeatFormMetaData metaData = new NeatFormMetaData();
            metaData.openmrsEntity = "concept";
            metaData.openmrsEntityId = referralServiceIndicatorObject.getId();
            metaData.openmrsEntityParent = "";
            option.neatFormMetaData = metaData;

            problemsOptions.add(option);
        }
        try {
            if (problems != null) {
                JSONArray optionsArray = new JSONArray(new Gson().toJson(problemsOptions));

                for (int i = 0; i < problems.getJSONArray("options").length(); i++) {
                    optionsArray.put(problems.getJSONArray("options").get(i));
                }
                //JSONArray//
                problems.put("options", optionsArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeHealthFacilitiesList(JSONObject form) {
        List<Location> locations = viewModel.getHealthFacilities();
        if (locations != null) {

            JSONArray fields = null;
            try {
                fields = form.getJSONArray("steps").getJSONObject(0).getJSONArray("fields");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject referralHealthFacilities = null;
            for (int i = 0; i < (fields != null ? fields.length() : 0); i++) {
                try {
                    if (fields.getJSONObject(i).getString("name").equals("chw_referral_hf")) {
                        referralHealthFacilities = fields.getJSONObject(i);
                        break;
                    }
                } catch (Exception e) {
                    Timber.e(e);
                }
            }


            Timber.i("Referral facilities --> %s", new Gson().toJson(locations));
            List<NeatFormOption> healthFacilitiesOptions = new ArrayList<>();
            NeatFormOption noneOption = new NeatFormOption();
            noneOption.name = "none";
            noneOption.text = "Select referral facility";

            healthFacilitiesOptions.add(noneOption);
            for (Location location : locations) {
                NeatFormOption healthFacilityOption = new NeatFormOption();
                healthFacilityOption.name = location.getId();
                healthFacilityOption.text = location.getProperties().getName();

                healthFacilitiesOptions.add(healthFacilityOption);
            }

            try {
                if (referralHealthFacilities != null) {
                    JSONArray optionsArray = new JSONArray();
                    for (int i = 0; i < referralHealthFacilities.getJSONArray("options").length(); i++) {
                        optionsArray.put(referralHealthFacilities.getJSONArray("options").get(i));
                    }
                    referralHealthFacilities.put("options", new JSONArray(new Gson().toJson(healthFacilitiesOptions)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void getMemberObject() {
        String query = viewModel.mainSelect(presenter.getMainTable(), presenter.getMainCondition());
        Timber.d("Query for the family member = %s", query);
        Cursor cursor = null;
        try {
            CommonRepository commonRepository = ReferralLibrary.getInstance().context().commonrepository(presenter.getMainTable());
            cursor = commonRepository.rawCustomQueryForAdapter(query);
            if (cursor != null && cursor.moveToFirst()) {
                CommonPersonObject personObject = commonRepository.readAllcommonforCursorAdapter(cursor);
                CommonPersonObjectClient commonPersonObjectClient = new CommonPersonObjectClient(personObject.getCaseId(), personObject.getDetails(), "");
                commonPersonObjectClient.setColumnmaps(personObject.getColumnmaps());
                viewModel.memberObject = new MemberObject(commonPersonObjectClient);
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }
    }

    @Override
    public void onButtonNextClick(@NotNull Step step) {
//        implement
    }

    @Override
    public void onButtonPreviousClick(@NotNull Step step) {
//        implement
    }

    @Override
    public void onCompleteStepper() {
        presenter.saveForm(formBuilder.getFormData(),jsonForm);
        Timber.e("Saved data = " + new Gson().toJson(formBuilder.getFormData()));
    }

    @Override
    public void onExitStepper() {
//        implement
    }

    @Override
    public void onStepComplete(@NotNull Step step) {
        //        implement
    }

    @Override
    public void onStepError(@NotNull StepVerificationState stepVerificationState) {
        //        implement
    }
}
