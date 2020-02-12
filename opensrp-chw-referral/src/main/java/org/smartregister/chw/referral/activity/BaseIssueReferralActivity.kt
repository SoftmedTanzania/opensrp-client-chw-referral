package org.smartregister.chw.referral.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.nerdstone.neatandroidstepper.core.domain.StepperActions
import com.nerdstone.neatandroidstepper.core.model.StepperModel
import com.nerdstone.neatandroidstepper.core.model.StepperModel.IndicatorType
import com.nerdstone.neatandroidstepper.core.stepper.Step
import com.nerdstone.neatandroidstepper.core.stepper.StepVerificationState
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.JsonFormStepBuilderModel
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import org.joda.time.DateTime
import org.joda.time.Period
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.ext.android.inject
import org.smartregister.AllConstants
import org.smartregister.chw.referral.R
import org.smartregister.chw.referral.ReferralLibrary
import org.smartregister.chw.referral.contract.BaseIssueReferralContract
import org.smartregister.chw.referral.domain.MemberObject
import org.smartregister.chw.referral.domain.NeatFormMetaData
import org.smartregister.chw.referral.domain.NeatFormOption
import org.smartregister.chw.referral.interactor.BaseIssueReferralInteractor
import org.smartregister.chw.referral.model.AbstractIssueReferralModel
import org.smartregister.chw.referral.model.BaseIssueReferralModel
import org.smartregister.chw.referral.presenter.BaseIssueReferralPresenter
import org.smartregister.chw.referral.util.Constants
import org.smartregister.chw.referral.util.JsonFormConstants
import org.smartregister.chw.referral.util.JsonFormUtils.addFormMetadata
import org.smartregister.chw.referral.util.JsonFormUtils.getFormAsJson
import org.smartregister.commonregistry.CommonPersonObjectClient
import org.smartregister.util.Utils
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cozej4 on 2019-10-07.
 *
 * @cozej4 https://github.com/cozej4
 */
/**
 * This is the activity for loading the Referral JSON forms. It implements [BaseIssueReferralContract.View]
 * and [StepperActions] (which is from the neat form library) that provides callback methods from the
 * form builder. It exposes a method to receiving the data from the views and exiting the activity
 */
open class BaseIssueReferralActivity : AppCompatActivity(), BaseIssueReferralContract.View,
    StepperActions {

    protected var presenter: BaseIssueReferralContract.Presenter? = null
    protected var baseEntityId: String? = null
    protected var serviceId: String? = null
    protected var formName: String? = null
    private var viewModel: AbstractIssueReferralModel? = null
    private var formBuilder: FormBuilder? = null
    private var jsonForm: JSONObject? = null
    val referralLibrary by inject<ReferralLibrary>()

    protected val locationID: String
        get() = org.smartregister.Context.getInstance().allSharedPreferences()
            .getPreference(AllConstants.CURRENT_LOCATION_ID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(this.intent) {
            baseEntityId = getStringExtra(Constants.ActivityPayload.BASE_ENTITY_ID)
            serviceId = getStringExtra(Constants.ActivityPayload.REFERRAL_SERVICE_IDS)
            action = getStringExtra(Constants.ActivityPayload.ACTION)
            formName = getStringExtra(Constants.ActivityPayload.REFERRAL_FORM_NAME)
            try {
                jsonForm = JSONObject(getStringExtra(Constants.ActivityPayload.JSON_FORM))
            } catch (e: JSONException) {
                Timber.e(e)
            }
        }

        presenter = presenter()
        viewModel =
            ViewModelProviders.of(this).get(presenter!!.getViewModel<AbstractIssueReferralModel>())
        setContentView(R.layout.activity_referral_registration)
        updateMemberObject()

        with(presenter) {
            this?.initializeMemberObject(viewModel?.memberObject!!)
            this?.fillClientData(viewModel?.memberObject!!)
        }

        createViewsFromJson()
    }

    private fun createViewsFromJson() {
        val formJsonObject: JSONObject? = jsonForm ?: getFormAsJson(formName, this)
        try {
            formJsonObject?.also {
                addFormMetadata(it, baseEntityId, locationID)
                with(viewModel?.memberObject!!) {
                    val age = Period(DateTime(this.age), DateTime()).years
                    it.put(
                        JsonFormConstants.FORM,
                        "${this.firstName} ${this.middleName} ${this.lastName}, $age"

                    )
                }

                initializeHealthFacilitiesList(it)
                if (serviceId != null) injectReferralProblems(it)

                val formLayout = findViewById<LinearLayout>(R.id.formLayout)
                val stepperModel = StepperModel.Builder()
                    .exitButtonDrawableResource(R.drawable.ic_arrow_back_white_24dp)
                    .indicatorType(IndicatorType.DOT_INDICATOR)
                    .toolbarColorResource(R.color.family_actionbar)
                    .build()

                val customLayouts = ArrayList<View>().also { list ->
                    list.add(layoutInflater.inflate(R.layout.referral_form_view, null))
                }

                formBuilder = JsonFormBuilder(it.toString(), this, formLayout)
                    .buildForm(
                        JsonFormStepBuilderModel.Builder(this, stepperModel).build(),
                        customLayouts
                    )
                formLayout.addView(formBuilder!!.neatStepperLayout)
            }

        } catch (ex: JSONException) {
            Timber.e(ex)
        }
    }

    override fun presenter() = BaseIssueReferralPresenter(
        baseEntityId!!, this, BaseIssueReferralModel::class.java, BaseIssueReferralInteractor()
    )


    override fun setProfileViewWithData() = Unit

    @Throws(JSONException::class)
    private fun injectReferralProblems(form: JSONObject) {
        val stepsJsonObject = form.getJSONArray(JsonFormConstants.STEPS).getJSONObject(0)
        val fields = stepsJsonObject.getJSONArray(JsonFormConstants.FIELDS)
        var problems: JSONObject? = null

        for (i in 0 until (fields?.length() ?: 0)) {
            if (fields!!.getJSONObject(i).getString(JsonFormConstants.NAME) == JsonFormConstants.PROBLEM) {
                problems = fields.getJSONObject(i)
                break
            }
        }

        val problemsOptions = ArrayList<NeatFormOption>()
        with(viewModel!!) {
            referralService = getReferralServicesList(serviceId!!)
            when (resources.configuration.locale.language) {
                Constants.EN -> {
                    stepsJsonObject.put(JsonFormConstants.TITLE, referralService?.nameEn)
                }
                Constants.SW -> {
                    stepsJsonObject.put(JsonFormConstants.TITLE, referralService?.nameSw)
                }
            }
            val indicatorsByServiceId = getIndicatorsByServiceId(serviceId!!)
            Timber.i("referral problems from DB = %s", Gson().toJson(indicatorsByServiceId))

            indicatorsByServiceId?.forEach { indicatorObject ->
                val option = NeatFormOption()
                when (resources.configuration.locale.language) {
                    Constants.EN -> {
                        option.name = indicatorObject.nameEn
                        option.text = indicatorObject.nameEn
                    }
                    Constants.SW -> {
                        option.name = indicatorObject.nameSw
                        option.text = indicatorObject.nameSw
                    }
                }
                option.neatFormMetaData = NeatFormMetaData().also {
                    it.openmrsEntity = JsonFormConstants.CONCEPT
                    it.openmrsEntityId = indicatorObject.id
                    it.openmrsEntityParent = ""
                }
                problemsOptions.add(option)
            }
            problems?.also {
                val optionsArray = JSONArray(Gson().toJson(problemsOptions))
                for (i in 0 until it.getJSONArray(JsonFormConstants.OPTIONS).length()) {
                    optionsArray.put(it.getJSONArray(JsonFormConstants.OPTIONS)[i])
                }
                it.put(JsonFormConstants.OPTIONS, optionsArray)
            }
        }
    }

    @Throws(JSONException::class)
    private fun initializeHealthFacilitiesList(form: JSONObject?) {
        val locations = viewModel!!.healthFacilities
        if (locations != null && form != null) {
            val fields = form.getJSONArray(JsonFormConstants.STEPS)
                .getJSONObject(0)
                .getJSONArray(JsonFormConstants.FIELDS)
            var referralHealthFacilities: JSONObject? = null
            for (i in 0 until (fields?.length() ?: 0)) {
                if (fields!!.getJSONObject(i).getString(JsonFormConstants.NAME) == JsonFormConstants.CHW_REFERRAL_HF
                ) {
                    referralHealthFacilities = fields.getJSONObject(i)
                    break
                }
            }
            Timber.i("Referral facilities --> %s", Gson().toJson(locations))
            val healthFacilitiesOptions = ArrayList<NeatFormOption>()
            locations.forEach { location ->
                val healthFacilityOption = NeatFormOption().also {
                    it.name = location.properties.name
                    it.text = location.properties.name
                    it.neatFormMetaData = NeatFormMetaData().apply {
                        this.openmrsEntity = "location_uuid"
                        this.openmrsEntityId = location.properties.uid
                    }
                }
                healthFacilitiesOptions.add(healthFacilityOption)
            }
            if (referralHealthFacilities != null) {
                val optionsArray = JSONArray()
                (0 until referralHealthFacilities.getJSONArray(JsonFormConstants.OPTIONS).length()).forEach { i ->
                    optionsArray.put(referralHealthFacilities.getJSONArray(JsonFormConstants.OPTIONS)[i])
                }
                referralHealthFacilities.put(
                    JsonFormConstants.OPTIONS, JSONArray(Gson().toJson(healthFacilitiesOptions))
                )
            }
        }
    }

    @Throws(Exception::class)
    private fun updateMemberObject() {
        with(presenter!!) {
            val query = viewModel!!.mainSelect(getMainTable(), getMainCondition())
            Timber.d("Query for the family member = %s", query)
            val commonRepository = referralLibrary.context.commonrepository(getMainTable())
            with(commonRepository.rawCustomQueryForAdapter(query)) {
                if (moveToFirst()) {
                    commonRepository.readAllcommonforCursorAdapter(this)
                        .also { commonPersonObject ->
                            CommonPersonObjectClient(
                                commonPersonObject.caseId, commonPersonObject.details, ""
                            ).apply {
                                this.columnmaps = commonPersonObject.columnmaps
                            }.also {
                                viewModel!!.memberObject = MemberObject(it)
                            }
                        }
                }

            }
        }
    }

    override fun onButtonNextClick(step: Step) = Unit

    override fun onButtonPreviousClick(step: Step) = Unit

    override fun onCompleteStepper() {
        val formData = formBuilder!!.getFormData()
        if (formData.isNotEmpty()) {
            try {
                val referralTaskFocus =
                    jsonForm!!.getString(JsonFormConstants.REFERRAL_TASK_FOCUS) ?: ""
                formData[JsonFormConstants.CHW_REFERRAL_SERVICE] =
                    NFormViewData().apply { value = referralTaskFocus }
            } catch (e: JSONException) {
                Timber.e(e)
            }

            //Saving referral Date
            formData[JsonFormConstants.REFERRAL_DATE] = NFormViewData().apply {
                value = Calendar.getInstance().timeInMillis
            }
            //Saving referral time
            val dateFormat: DateFormat =
                SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
            formData[JsonFormConstants.REFERRAL_TIME] = NFormViewData().apply {
                value = dateFormat.format(Date())
            }
            //Saving referral type
            formData[JsonFormConstants.REFERRAL_TYPE] = NFormViewData().apply {
                value = Constants.ReferralType.COMMUNITY_TO_FACILITY_REFERRAL
            }
            //Saving referral status
            formData[JsonFormConstants.REFERRAL_STATUS] = NFormViewData().apply {
                value = Constants.ReferralStatus.PENDING
            }
            presenter!!.saveForm(formData, jsonForm!!)
            Timber.i("Saved data = %s", Gson().toJson(formData))
            Utils.showToast(this, this.getString(R.string.referral_submitted))
            finish()
        }
    }

    override fun onExitStepper() {
        AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setTitle(getString(R.string.confirm_form_close))
            .setMessage(getString(R.string.confirm_form_close_explanation))
            .setNegativeButton(R.string.yes) { _: DialogInterface?, _: Int -> finish() }
            .setPositiveButton(R.string.no) { _: DialogInterface?, _: Int ->
                Timber.d("Do Nothing exit confirm dialog")
            }
            .create()
            .show()
    }

    override fun onStepComplete(step: Step) = Unit

    override fun onStepError(stepVerificationState: StepVerificationState) = Unit
}