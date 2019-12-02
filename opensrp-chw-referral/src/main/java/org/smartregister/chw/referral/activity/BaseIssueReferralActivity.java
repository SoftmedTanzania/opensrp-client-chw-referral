package org.smartregister.chw.referral.activity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.contract.BaseIssueReferralContract;
import org.smartregister.chw.referral.databinding.ActivityReferralRegistrationBinding;
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
import java.util.Objects;

import timber.log.Timber;

/**
 * Created by cozej4 on 2019-10-07.
 *
 * @cozej4 https://github.com/cozej4
 */

public class BaseIssueReferralActivity extends AppCompatActivity implements BaseIssueReferralContract.View {
    protected BaseIssueReferralContract.Presenter presenter;
    protected String baseEntityId;
    protected String serviceId;
    protected String action;
    protected String formName;
    protected TextView textViewName;
    protected TextView textViewGender;
    protected TextView textViewLocation;
    protected TextView textViewUniqueID;
    private AbstractIssueReferralModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.baseEntityId = this.getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID);
        this.serviceId = this.getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.REFERRAL_SERVICE_IDS);
        this.action = this.getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.ACTION);
        this.formName = this.getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.REFERRAL_FORM_NAME);

        //initializing the presenter
        presenter = presenter();

        //initializing the viewModel obtained from presenter,
        // this viewModel must extend #AbstractIssueReferralModel and implements BaseIssueReferralContract.Model
        viewModel = ViewModelProviders.of(this).get(presenter.getViewModel());

        ActivityReferralRegistrationBinding mBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_referral_registration);
        mBinding.setViewModel(viewModel);

        setContentView(mBinding.getRoot());

        getMemberObject();

        try {
            presenter.initializeMemberObject(viewModel.memberObject);
        } catch (Exception e) {
            Timber.e(e);
        }

        setupViews();
        presenter.fillClientData(viewModel.memberObject);

        JSONObject jsonForm = null;
        try {
            jsonForm = JsonFormUtils.getFormAsJson(formName);
            JsonFormUtils.addFormMetadata(jsonForm, baseEntityId, getLocationID());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonForm != null) {
            injectReferralProblems(jsonForm);
            initializeHealthFacilitiesList(jsonForm);

            Timber.i("Form with injected values = "+jsonForm);
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
        int age = new Period(new DateTime(viewModel.memberObject.getAge()), new DateTime()).getYears();
        textViewName.setText(String.format(Locale.getDefault(), "%s %s %s, %d", viewModel.memberObject.getFirstName(),
                viewModel.memberObject.getMiddleName(), viewModel.memberObject.getLastName(), age));
        textViewGender.setText(viewModel.memberObject.getGender());
        textViewLocation.setText(viewModel.memberObject.getAddress());
        textViewUniqueID.setText(viewModel.memberObject.getUniqueId());
    }

    protected String getLocationID() {
        return org.smartregister.Context.getInstance().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
    }

    public void setupViews() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(view -> finish());

        Button buttonSave = findViewById(R.id.referal_button);
        buttonSave.setOnClickListener(view -> {
            try {
                viewModel.saveDataToMemberObject();
                if (presenter.validateValues(viewModel.memberObject)) {
                    JSONObject jsonForm = viewModel.getFormWithValuesAsJson(formName, baseEntityId, getLocationID(), viewModel.memberObject);
                    presenter.saveForm(jsonForm.toString());
                    Toast.makeText(this, getResources().getString(R.string.successful_issued_referral), Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (Exception e) {
                Timber.e(e);
            }
        });

    }

    private void injectReferralProblems(JSONObject form) {
        Timber.i("Setup Services Problems Called");

        //observing viewModel to obtain referral services list, this allows the ui to be updated if any changes are to be made to the viewModel live data
        viewModel.getReferralServicesList(serviceId).observe(this, referralServiceObjects -> {
            //Initializing providers/adapters using the observed data
            JSONArray fields = null;
            try {
                fields = form.getJSONObject("steps").getJSONArray("fields");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject problems = null;
            for (int i = 0; i < fields.length(); i++) {
                try {
                    if (fields.getJSONObject(i).getString("name").equals("problems")) {
                        problems = fields.getJSONObject(i);
                        return;
                    }
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
            List<NeatFormOption> problemsOptions = new ArrayList<>();

            List<ReferralServiceIndicatorObject> indicatorsByServiceId = viewModel.getIndicatorsByServiceId(Objects.requireNonNull(viewModel.selectedReferralService.get()).getId());
            for (ReferralServiceIndicatorObject referralServiceIndicatorObject : indicatorsByServiceId) {
                NeatFormOption option = new NeatFormOption();
                option.name = referralServiceIndicatorObject.getId();
                option.text = referralServiceIndicatorObject.getNameEn();

                NeatFormMetaData metaData = new NeatFormMetaData();
                metaData.openmrs_entity = "";
                metaData.openmrs_entity_id = "";
                metaData.openmrs_entity_parent = "";
                option.neatFormMetaData = metaData;
                problemsOptions.add(option);
            }
            try {
                problems.put("options", new JSONArray(new Gson().toJson(problemsOptions)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void initializeHealthFacilitiesList(JSONObject form) {
        //observing viewModel to obtain health facility list that the CHW can refer a client to
        viewModel.getHealthFacilities().observe(this, locations -> {
            if (locations != null) {

                JSONArray fields = null;
                try {
                    fields = form.getJSONObject("steps").getJSONArray("fields");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject referralHealthFacilities = null;
                for (int i = 0; i < fields.length(); i++) {
                    try {
                        if (fields.getJSONObject(i).getString("name").equals("chw_referral_hf")) {
                            referralHealthFacilities = fields.getJSONObject(i);
                            return;
                        }
                    } catch (Exception e) {
                        Timber.e(e);
                    }
                }


                Timber.i("Referral facilities --> %s", new Gson().toJson(locations));
                List<NeatFormOption> healthFacilitiesOptions = new ArrayList<>();
                for (Location location : locations) {
                    NeatFormOption healthFacilityOption = new NeatFormOption();
                    healthFacilityOption.name = location.getId();
                    healthFacilityOption.text = location.getProperties().getName();

                    healthFacilitiesOptions.add(healthFacilityOption);
                }

                try {
                    referralHealthFacilities.put("options",new JSONArray(new Gson().toJson(healthFacilitiesOptions)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

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
}
