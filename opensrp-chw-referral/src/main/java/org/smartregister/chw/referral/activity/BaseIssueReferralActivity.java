package org.smartregister.chw.referral.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.ReferralLibrary;
import org.smartregister.chw.referral.contract.BaseIssueReferralContract;
import org.smartregister.chw.referral.databinding.ActivityReferralRegistrationBinding;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.domain.ReferralServiceIndicatorObject;
import org.smartregister.chw.referral.interactor.BaseIssueReferralInteractor;
import org.smartregister.chw.referral.model.AbstractIssueReferralModel;
import org.smartregister.chw.referral.model.BaseIssueReferralModel;
import org.smartregister.chw.referral.presenter.BaseIssueReferralPresenter;
import org.smartregister.chw.referral.provider.ReferralServicesProvider;
import org.smartregister.chw.referral.util.Constants;
import org.smartregister.commonregistry.CommonPersonObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.domain.Location;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import fr.ganfra.materialspinner.MaterialSpinner;
import timber.log.Timber;

/**
 * Created by cozej4 on 2019-10-07.
 *
 * @cozej4 https://github.com/cozej4
 */

public class BaseIssueReferralActivity extends AppCompatActivity implements BaseIssueReferralContract.View {
    protected BaseIssueReferralContract.Presenter presenter;
    protected String baseEntityId;
    protected List<String> serviceIds;
    protected String action;
    protected String formName;
    protected TextView textViewName;
    protected TextView textViewGender;
    protected TextView textViewLocation;
    protected TextView textViewUniqueID;
    private AbstractIssueReferralModel viewModel;
    private MaterialSpinner spinnerService;
    private LinearLayout linearLayoutIndicators;
    private View viewIndicator;
    private MaterialEditText textViewAppointmentDate;
    private AutoCompleteTextView autoCompleteTextViewFacilityName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.baseEntityId = this.getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID);
        this.serviceIds = this.getIntent().getStringArrayListExtra(Constants.ACTIVITY_PAYLOAD.REFERRAL_SERVICE_IDS);
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
        initializeServices();
        initializeIndicators();
        initializeHealthFacilitiesList();
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

        viewIndicator = findViewById(R.id.indicators);
        linearLayoutIndicators = findViewById(R.id.indicators_layout);
        textViewName = findViewById(R.id.textview_name);
        textViewGender = findViewById(R.id.textview_gender);
        textViewLocation = findViewById(R.id.textview_address);
        textViewUniqueID = findViewById(R.id.textview_id);
        textViewAppointmentDate = findViewById(R.id.appointment_date);
        Button buttonSave = findViewById(R.id.referal_button);
        buttonSave.setOnClickListener(view -> {
            try {
                viewModel.saveDataToMemberObject();
                if (presenter.validateValues(viewModel.memberObject)) {
                    JSONObject jsonForm = viewModel.getFormWithValuesAsJson(formName, baseEntityId, getLocationID(), viewModel.memberObject);
                    jsonForm.put(Constants.FOCUS, "Testing Focus");
                    presenter.saveForm(jsonForm.toString());
                    Toast.makeText(this, getResources().getString(R.string.successful_issued_referral), Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (Exception e) {
                Timber.e(e);
            }
        });

        spinnerService = findViewById(R.id.spinnerService);

        autoCompleteTextViewFacilityName = findViewById(R.id.autocomplete_facility);
        autoCompleteTextViewFacilityName.setThreshold(1);

        textViewAppointmentDate.setOnClickListener(view -> {
            // pick date
            pickDate(R.id.appointment_date);
        });


        viewModel.getIsEmergency().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                try {
                    if (Objects.requireNonNull(viewModel.getIsEmergency().get())) {
                        textViewAppointmentDate.setText("");
                        viewModel.appointmentDateTimestamp = 0;
                        textViewAppointmentDate.setVisibility(View.GONE);
                    } else {
                        textViewAppointmentDate.setVisibility(View.VISIBLE);
                    }
                } catch (NullPointerException e) {
                    Timber.e(e);
                }
            }
        });

    }

    private void initializeServices() {
        Timber.i("Setup Services Called");

        //observing viewModel to obtain referral services list, this allows the ui to be updated if any changes are to be made to the viewModel live data
        viewModel.getReferralServicesList(serviceIds).observe(this, referralServiceObjects -> {
            //Initializing providers/adapters using the observed data
            ReferralServicesProvider referralServicesProvider = new ReferralServicesProvider(BaseIssueReferralActivity.this, Objects.requireNonNull(referralServiceObjects));
            spinnerService.setAdapter(referralServicesProvider);
            spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i >= 0) {
                        viewIndicator.setVisibility(View.VISIBLE);
                        try {
                            //checking whether to update viewModel or Not.
                            if (!referralServiceObjects.get(i).getId().equals(Objects.requireNonNull(viewModel.selectedReferralService.get()).getId())) {
                                viewModel.selectedReferralService.set(referralServiceObjects.get(i));
                            }
                        } catch (NullPointerException e) {
                            Timber.e(e);
                            //if viewModel selectedReferralService is NUll then update it
                            viewModel.selectedReferralService.set(referralServiceObjects.get(i));
                        } finally {
                            //this is to update the ui with data from the viewModel always ensuring the indicators UI list corresponds to the data stored within the viewModel
                            setIndicators();
                        }
                    } else {
                        viewIndicator.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    viewModel.selectedReferralService = null;

                }
            });

            try {
                if (referralServiceObjects.size() == 1) {
                    Timber.e("Setting spinner selection to 1 ");
                    spinnerService.setSelection(1);
                    spinnerService.setEnabled(false);
                    spinnerService.setClickable(false);
                }
            } catch (NullPointerException e) {
                Timber.e(e);
            }
        });
    }

    private void initializeIndicators() {
        //observing viewModel to obtain referral services indicators list, this allows the ui to be updated if any changes are to be made to the viewModel live data
        viewModel.selectedReferralService.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {

                //Updating view model indicator list
                viewModel.referralServiceIndicators = viewModel.getIndicatorsByServiceId(Objects.requireNonNull(viewModel.selectedReferralService.get()).getId());
                setIndicators();
            }
        });
    }

    @SuppressLint("InflateParams")
    private void setIndicators() {
        try {
            //removing all indicators and setting the correct indicators checkboxes
            linearLayoutIndicators.removeAllViewsInLayout();

            for (ReferralServiceIndicatorObject serviceIndicatorObject : Objects.requireNonNull(viewModel.referralServiceIndicators)) {
                View v = getLayoutInflater().inflate(R.layout.item_indicator, null);
                CheckBox indicatorName = v.findViewById(R.id.indicator_name);
                indicatorName.setText(serviceIndicatorObject.getNameEn());
                indicatorName.setPadding(0, 10, 10, 0);

                //this is used to refresh the checked status of the indicator from viewModel.
                indicatorName.setChecked(serviceIndicatorObject.isChecked());

                indicatorName.setOnCheckedChangeListener((compoundButton, b) -> serviceIndicatorObject.setChecked(b));

                linearLayoutIndicators.addView(v);
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (viewModel.referralServiceIndicators == null) {
                viewIndicator.setVisibility(View.GONE);
            }
        }
    }

    private void initializeHealthFacilitiesList() {
        //observing viewModel to obtain health facility list that the CHW can refer a client to
        viewModel.getHealthFacilities().observe(this, locations -> {

            if (locations != null) {
                Timber.i("Referral facilities --> %s", new Gson().toJson(locations));
                List<String> facilityNames = new ArrayList<>();
                for (Location location : locations) {
                    facilityNames.add(location.getProperties().getName());
                }

                ArrayAdapter<String> facilityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, facilityNames);
                autoCompleteTextViewFacilityName.setAdapter(facilityAdapter);
                autoCompleteTextViewFacilityName.setOnItemClickListener((parent, view, position, rowId) -> viewModel.referralFacilityUuid = locations.get(position).getProperties().getUid());
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

    private void pickDate(final int id) {
        // listener
        DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            GregorianCalendar pickedDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            if (id == R.id.appointment_date) {
                viewModel.appointmentDateTimestamp = pickedDate.getTimeInMillis();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                textViewAppointmentDate.setText(dateFormat.format(pickedDate.getTimeInMillis()));
            }
        };

        // dialog
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                onDateSetListener);

        datePickerDialog.setMinDate(Calendar.getInstance());

        datePickerDialog.setOkColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_light));
        datePickerDialog.setCancelColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light));

        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_1);
        datePickerDialog.setAccentColor(ContextCompat.getColor(getApplicationContext(), R.color.btn_blue));

        // show dialog
        datePickerDialog.show(this.getFragmentManager(), "DatePickerDialog");
    }
}
