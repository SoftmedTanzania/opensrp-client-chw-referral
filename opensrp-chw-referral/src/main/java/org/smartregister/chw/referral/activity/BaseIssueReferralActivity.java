package org.smartregister.chw.referral.activity;

import android.app.AlertDialog;
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
import com.nerdstone.neatformcore.domain.model.NFormViewData;
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
import org.smartregister.chw.referral.util.JsonFormConstant;
import org.smartregister.chw.referral.util.JsonFormUtils;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.domain.Location;
import org.smartregister.util.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

        try {
            this.jsonForm = new JSONObject(this.getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.JSON_FORM));
        } catch (Exception e) {
            Timber.e(e);
        }

        presenter = presenter();

        viewModel = ViewModelProviders.of(this).get(presenter.getViewModel());

        setContentView(R.layout.activity_referral_registration);

        getMemberObject();

        try {
            presenter.initializeMemberObject(viewModel.memberObject);
        } catch (Exception e) {
            Timber.e(e);
        }

        presenter.fillClientData(viewModel.memberObject);

        if (jsonForm == null) {
            try {
                jsonForm = JsonFormUtils.getFormAsJson(formName);
            } catch (Exception e) {
                Timber.e(e);
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
        try {
            initializeHealthFacilitiesList(jsonForm);
            if (serviceId!=null && jsonForm != null) {
                injectReferralProblems(jsonForm);
            }
        } catch (JSONException e) {
            Timber.e(e);
        }
        Timber.i("Form with injected values = %s", jsonForm);

        LinearLayout formLayout = findViewById(R.id.formLayout);
        StepperModel stepperModel = new StepperModel.Builder()
                .exitButtonDrawableResource(R.drawable.ic_arrow_back_white_24dp)
                .indicatorType(StepperModel.IndicatorType.DOT_INDICATOR)
                .toolbarColorResource(R.color.family_actionbar)
                .build();

        JsonFormStepBuilderModel jsonFormStepBuilderModel = new JsonFormStepBuilderModel.Builder(this, stepperModel).build();

        JsonFormBuilder jsonFormBuilder;
        if (jsonForm != null) {
            jsonFormBuilder = new JsonFormBuilder(jsonForm.toString(), this, formLayout);
            formBuilder = jsonFormBuilder.buildForm(jsonFormStepBuilderModel, null);
            formLayout.addView(formBuilder.getNeatStepperLayout());
        }
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


    private void injectReferralProblems(JSONObject form) throws JSONException {
        JSONObject stepsJsonObject = form.getJSONArray(JsonFormConstant.STEPS)
                .getJSONObject(0);
        JSONArray fields = stepsJsonObject.getJSONArray(JsonFormConstant.FIELDS);

        JSONObject problems = null;
        for (int i = 0; i < (fields != null ? fields.length() : 0); i++) {
            if (fields.getJSONObject(i).getString(JsonFormConstant.NAME).equals(JsonFormConstant.PROBLEM)) {
                problems = fields.getJSONObject(i);
                break;
            }
        }
        List<NeatFormOption> problemsOptions = new ArrayList<>();
        viewModel.referralService = viewModel.getReferralServicesList(serviceId);



        if(getResources().getConfiguration().locale.getLanguage().equals("en")) {
            stepsJsonObject.put(JsonFormConstant.TITLE,viewModel.referralService.getNameEn());
        }else  if(getResources().getConfiguration().locale.getLanguage().equals("sw")){
            stepsJsonObject.put(JsonFormConstant.TITLE,viewModel.referralService.getNameSw());
        }

        List<ReferralServiceIndicatorObject> indicatorsByServiceId = viewModel.getIndicatorsByServiceId(serviceId);
        Timber.i("referral problems from DB = %s", new Gson().toJson(indicatorsByServiceId));
        if(indicatorsByServiceId!=null) {
            for (ReferralServiceIndicatorObject referralServiceIndicatorObject : indicatorsByServiceId) {
                NeatFormOption option = new NeatFormOption();

                if (getResources().getConfiguration().locale.getLanguage().equals("en")) {
                    option.name = referralServiceIndicatorObject.getNameEn();
                    option.text = referralServiceIndicatorObject.getNameEn();
                } else if (getResources().getConfiguration().locale.getLanguage().equals("sw")) {
                    option.name = referralServiceIndicatorObject.getNameSw();
                    option.text = referralServiceIndicatorObject.getNameSw();
                }

                NeatFormMetaData metaData = new NeatFormMetaData();
                metaData.openmrsEntity = JsonFormConstant.CONCEPT;
                metaData.openmrsEntityId = referralServiceIndicatorObject.getId();
                metaData.openmrsEntityParent = "";
                option.neatFormMetaData = metaData;

                problemsOptions.add(option);
            }
        }

        if (problems != null) {
            JSONArray optionsArray = new JSONArray(new Gson().toJson(problemsOptions));
            for (int i = 0; i < problems.getJSONArray(JsonFormConstant.OPTIONS).length(); i++) {
                optionsArray.put(problems.getJSONArray(JsonFormConstant.OPTIONS).get(i));
            }
            problems.put(JsonFormConstant.OPTIONS, optionsArray);
        }
    }

    private void initializeHealthFacilitiesList(JSONObject form) throws JSONException {
        List<Location> locations = viewModel.getHealthFacilities();
        if (locations != null) {

            JSONArray fields = form.getJSONArray(JsonFormConstant.STEPS)
                    .getJSONObject(0).getJSONArray(JsonFormConstant.FIELDS);

            JSONObject referralHealthFacilities = null;
            for (int i = 0; i < (fields != null ? fields.length() : 0); i++) {
                if (fields.getJSONObject(i).getString(JsonFormConstant.NAME)
                        .equals(JsonFormConstant.CHW_REFERRAL_HF)) {
                    referralHealthFacilities = fields.getJSONObject(i);
                    break;
                }
            }

            Timber.i("Referral facilities --> %s", new Gson().toJson(locations));
            List<NeatFormOption> healthFacilitiesOptions = new ArrayList<>();
            for (Location location : locations) {
                NeatFormOption healthFacilityOption = new NeatFormOption();
                healthFacilityOption.name = location.getProperties().getName();
                healthFacilityOption.text = location.getProperties().getName();

                NeatFormMetaData metaData = new NeatFormMetaData();
                metaData.openmrsEntity = "location_uuid";
                metaData.openmrsEntityId = location.getProperties().getUid();

                healthFacilityOption.neatFormMetaData = metaData;

                healthFacilitiesOptions.add(healthFacilityOption);
            }

            if (referralHealthFacilities != null) {
                JSONArray optionsArray = new JSONArray();
                for (int i = 0; i < referralHealthFacilities.getJSONArray(JsonFormConstant.OPTIONS).length(); i++) {
                    optionsArray.put(referralHealthFacilities.getJSONArray(JsonFormConstant.OPTIONS).get(i));
                }
                referralHealthFacilities.put(JsonFormConstant.OPTIONS, new JSONArray(new Gson().toJson(healthFacilitiesOptions)));
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
        // Overridden: Not required for now
    }

    @Override
    public void onButtonPreviousClick(@NotNull Step step) {
        // Overridden: Not required for now
    }

    @Override
    public void onCompleteStepper() {
        if (!formBuilder.getFormData().isEmpty()) {

            //Saving referral service
            NFormViewData referralServiceValue = new NFormViewData();
            String referralTaskFocus = "";
            try {
                referralTaskFocus = jsonForm.getString(JsonFormConstant.REFERRAL_TASK_FOCUS);
            } catch (JSONException e) {
                Timber.e(e);
            }
            referralServiceValue.setValue(referralTaskFocus);
            formBuilder.getFormData().put(JsonFormConstant.CHW_REFERRAL_SERVICE, referralServiceValue);

            //Saving referral Date
            NFormViewData dateValue = new NFormViewData();
            dateValue.setValue(Calendar.getInstance().getTimeInMillis());
            formBuilder.getFormData().put(JsonFormConstant.REFERRAL_DATE, dateValue);

            //Saving referral time
            NFormViewData timeValue = new NFormViewData();
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
            Date date = new Date();
            timeValue.setValue(dateFormat.format(date));
            formBuilder.getFormData().put(JsonFormConstant.REFERRAL_TIME, timeValue);

            //Saving referral type
            NFormViewData referralType = new NFormViewData();
            referralType.setValue(Constants.REFERRAL_TYPE.COMMUNITY_TO_FACILITY_REFERRAL);
            formBuilder.getFormData().put(JsonFormConstant.REFERRAL_TYPE, referralType);

            //Saving referral status
            NFormViewData referralStatus = new NFormViewData();
            referralStatus.setValue(Constants.REFERRAL_STATUS.PENDING);
            formBuilder.getFormData().put(JsonFormConstant.REFERRAL_STATUS, referralStatus);

            presenter.saveForm(formBuilder.getFormData(), jsonForm);
            Timber.i("Saved data = %s", new Gson().toJson(formBuilder.getFormData()));
            Utils.showToast(this, this.getCurrentContext().getString(R.string.referral_submitted));
            finish();
        }
    }

    @Override
    public void onExitStepper() {
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setTitle(getString(R.string.confirm_form_close))
                .setMessage(getString(R.string.confirm_form_close_explanation))
                .setNegativeButton(R.string.yes, (dialogInterface, which) -> finish())
                .setPositiveButton(R.string.no, (dialogInterface, which) ->
                        Timber.d("Do Nothing exit confirm dialog"))
                .create();

        dialog.show();
    }

    @Override
    public void onStepComplete(@NotNull Step step) {
        // Overridden: Not necessary for single step
    }

    @Override
    public void onStepError(@NotNull StepVerificationState stepVerificationState) {
        // Overridden: Already handled by neat form builder
    }

}
