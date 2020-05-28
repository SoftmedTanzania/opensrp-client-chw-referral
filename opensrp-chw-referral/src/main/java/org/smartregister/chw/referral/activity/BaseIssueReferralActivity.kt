package org.smartregister.chw.referral.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.nerdstone.neatandroidstepper.core.domain.StepperActions
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import com.nerdstone.neatformcore.form.json.JsonFormEmbedded
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
import timber.log.Timber
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
open class BaseIssueReferralActivity : AppCompatActivity(), BaseIssueReferralContract.View {

    protected var presenter: BaseIssueReferralContract.Presenter? = null
    protected var baseEntityId: String? = null
    protected var serviceId: String? = null
    protected var formName: String? = null
    private var viewModel: AbstractIssueReferralModel? = null
    private var formBuilder: FormBuilder? = null
    private var jsonForm: JSONObject? = null
    private lateinit var formLayout: LinearLayout
    private lateinit var mainLayout: LinearLayout
    private lateinit var sampleToolBar: Toolbar
    private lateinit var pageTitleTextView: TextView
    private lateinit var exitFormImageView: ImageView
    private lateinit var completeButton: ImageView
    val referralLibrary by inject<ReferralLibrary>()

    protected val locationID: String
        get() = org.smartregister.Context.getInstance().allSharedPreferences()
            .getPreference(AllConstants.CURRENT_LOCATION_ID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_referral_registration)
        mainLayout = findViewById(R.id.mainLayout)
        formLayout = findViewById(R.id.formLayout)
        sampleToolBar = findViewById(R.id.sampleToolBar)
        pageTitleTextView = findViewById(R.id.pageTitleTextView)
        exitFormImageView = findViewById(R.id.exitFormImageView)
        completeButton = findViewById(R.id.completeButton)

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
            presenter = presenter()
            viewModel = ViewModelProviders.of(this@BaseIssueReferralActivity)
                .get(presenter!!.getViewModel<AbstractIssueReferralModel>())

            updateMemberObject()

            with(presenter) {
                this?.initializeMemberObject(viewModel?.memberObject!!)
                this?.fillClientData(viewModel?.memberObject!!)
            }

            with(viewModel?.memberObject!!) {
                val age = Period(DateTime(this.age), DateTime()).years
                pageTitleTextView.text =
                    "${this.firstName} ${this.middleName} ${this.lastName}, $age"
            }

            exitFormImageView.setOnClickListener {
                if (it.id == R.id.exitFormImageView) {
                    AlertDialog.Builder(
                        this@BaseIssueReferralActivity,
                        R.style.AlertDialogTheme
                    )
                        .setTitle(getString(R.string.confirm_form_close))
                        .setMessage(getString(R.string.confirm_form_close_explanation))
                        .setNegativeButton(R.string.yes) { _: DialogInterface?, _: Int -> finish() }
                        .setPositiveButton(R.string.no) { _: DialogInterface?, _: Int ->
                            Timber.d("Do Nothing exit confirm dialog")
                        }
                        .create()
                        .show()
                }
            }

            completeButton.setOnClickListener {
                if (it.id == R.id.completeButton) {
                    if (formBuilder?.getFormDataAsJson() != "") {

                        val formData = formBuilder!!.getFormData()
                        if (formData.isNotEmpty()) {
                            val referralTaskFocus =
                                jsonForm!!.getString(JsonFormConstants.REFERRAL_TASK_FOCUS) ?: ""
                            formData[JsonFormConstants.CHW_REFERRAL_SERVICE] =
                                NFormViewData().apply { value = referralTaskFocus }
                            formData.put(JsonFormConstants.CHW_REFERRAL_SERVICE, NFormViewData().apply { value = referralTaskFocus })

                            presenter!!.saveForm(formData, jsonForm!!)

                            Toast.makeText(
                                applicationContext,
                                getString(R.string.successful_issued_referral),
                                Toast.LENGTH_LONG
                            ).show()
                            Timber.d("Saved Data = %s", formBuilder?.getFormDataAsJson())
                            val intent = Intent()
                            setResult(Activity.RESULT_OK, intent);
                            finish()
                        }

                        finish()
                    }
                }
            }
            createViewsFromJson()


        }
    }

    private fun createViewsFromJson() {
        try {
            jsonForm?.also {
                addFormMetadata(it, baseEntityId, locationID)
                initializeHealthFacilitiesList(it)

                val customLayouts = ArrayList<View>().also { list ->
                    list.add(layoutInflater.inflate(R.layout.referral_form_view, null))
                }
                Timber.e("Coze :: Loading form builder")
                Timber.e("Coze :: Loading json = "+it)
                formBuilder = JsonFormBuilder(it.toString(), this)
                JsonFormEmbedded(
                    formBuilder as JsonFormBuilder,
                    formLayout
                ).buildForm(null)
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
    private fun initializeHealthFacilitiesList(form: JSONObject?) {
        val locations = viewModel!!.healthFacilities
        if (locations != null && form != null) {
            val fields = form.getJSONArray(JsonFormConstants.STEPS)
                .getJSONObject(0)
                .getJSONArray(JsonFormConstants.FIELDS)
            var referralHealthFacilities: JSONObject? = null
            for (i in 0 until (fields?.length() ?: 0)) {
                if (fields!!.getJSONObject(i)
                        .getString(JsonFormConstants.NAME) == JsonFormConstants.CHW_REFERRAL_HF
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
                (0 until referralHealthFacilities.getJSONArray(JsonFormConstants.OPTIONS)
                    .length()).forEach { i ->
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

}