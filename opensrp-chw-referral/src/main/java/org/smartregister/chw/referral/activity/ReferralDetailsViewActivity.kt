package org.smartregister.chw.referral.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import org.apache.commons.lang3.StringUtils
import org.joda.time.DateTime
import org.joda.time.Period
import org.smartregister.chw.referral.R
import org.smartregister.chw.referral.domain.MemberObject
import org.smartregister.chw.referral.util.Constants
import org.smartregister.chw.referral.util.ReferralUtil
import org.smartregister.view.activity.SecuredActivity
import org.smartregister.view.customcontrols.CustomFontTextView
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

/***
 * This class is for displaying the referral details for the selected client It extends [SecuredActivity]
 */
open class ReferralDetailsViewActivity : SecuredActivity() {

    protected lateinit var appBarLayout: AppBarLayout
    private lateinit var clientName: CustomFontTextView
    private lateinit var careGiverName: CustomFontTextView
    private lateinit var careGiverPhone: CustomFontTextView
    private lateinit var clientReferralProblem: CustomFontTextView
    private lateinit var referralDate: CustomFontTextView
    private lateinit var referralFacility: CustomFontTextView
    private lateinit var preReferralManagement: CustomFontTextView
    private lateinit var referralType: CustomFontTextView
    private lateinit var problemLayout: ViewGroup
    private lateinit var preManagementServicesServices: ViewGroup
    val baseEntityId: String? = null
    var memberObject: MemberObject? = null

    override fun onCreateOptionsMenu(menu: Menu) = false

    override fun onCreation() {
        setContentView(R.layout.referral_details_activity)
        inflateToolbar()
        memberObject =
            intent.getSerializableExtra(Constants.ReferralMemberObject.MEMBER_OBJECT) as MemberObject
        setUpViews()
    }

    override fun onResumption() = Unit

    private fun inflateToolbar() {
        val toolbar =
            findViewById<Toolbar>(R.id.back_referrals_toolbar)
        val toolBarTextView: CustomFontTextView = toolbar.findViewById(R.id.toolbar_title)
        setSupportActionBar(toolbar)
        supportActionBar?.also {
            it.setDisplayHomeAsUpEnabled(true)
            val upArrow = resources.getDrawable(R.drawable.ic_arrow_back_white_24dp)
            upArrow.setColorFilter(resources.getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP)
            it.setHomeAsUpIndicator(upArrow)
            it.elevation = 0f
        }
        toolbar.setNavigationOnClickListener { finish() }
        toolBarTextView.setText(R.string.back_to_referrals)
        toolBarTextView.setOnClickListener { finish() }
        appBarLayout = findViewById(R.id.app_bar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) appBarLayout.outlineProvider =
            null
    }

    private fun setUpViews() {
        clientName = findViewById(R.id.client_name)
        careGiverName = findViewById(R.id.care_giver_name)
        careGiverPhone = findViewById(R.id.care_giver_phone)
        clientReferralProblem = findViewById(R.id.client_referral_problem)
        referralDate = findViewById(R.id.referral_date)
        referralFacility = findViewById(R.id.referral_facility)
        preReferralManagement = findViewById(R.id.pre_referral_management)
        referralType = findViewById(R.id.referral_type)
        problemLayout = findViewById(R.id.client_referral_problem_layout)
        preManagementServicesServices = findViewById(R.id.client_pre_referral_management_layout)
        obtainReferralDetails()
    }

    @SuppressLint("SetTextI18n")
    private fun obtainReferralDetails() {
        memberObject?.also {
            updateProblemDisplay()
            val clientAge = Period(DateTime(it.age), DateTime()).years.toString()
            clientName.text = "${it.firstName} ${it.middleName} ${it.lastName}, $clientAge"
            val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val referralDateCalendar = Calendar.getInstance()
            referralDateCalendar.timeInMillis = BigDecimal(it.chwReferralDate).toLong()
            referralDate.text = dateFormatter.format(referralDateCalendar.time)
            referralFacility.text = it.chwReferralHf
            referralType.text = ReferralUtil.getTranslatedReferralServiceType(this, it.chwReferralService!!)
            if (!it.primaryCareGiver.isNullOrEmpty() && clientAge.toInt() < 5)
                careGiverName.text = String.format("CG : %s", it.primaryCareGiver)
            else
                careGiverName.visibility = View.GONE
            careGiverPhone.text =
                if (familyMemberContacts!!.isEmpty() || familyMemberContacts == null) getString(
                    R.string.phone_not_provided
                ) else familyMemberContacts
            updateProblemDisplay()
            updatePreReferralServicesDisplay()
        }
    }

    private fun updateProblemDisplay() {
        memberObject?.run {
            when {
                problem != null -> {
                    if (problem?.startsWith("[")!! && problem?.endsWith("]")!!) {
                        clientReferralProblem.text = problem?.substring(1, problem!!.length - 1)
                    } else {
                        clientReferralProblem.text = problem
                    }
                    if (!StringUtils.isEmpty(problemOther)) {
                        clientReferralProblem.append(", $problemOther")
                    }
                }
                else -> {
                    clientReferralProblem.text = getString(R.string.empty_value)
                    problemLayout.visibility = View.GONE
                }
            }
        }
    }

    private fun updatePreReferralServicesDisplay() {
        memberObject?.run {
            when {
                servicesBeforeReferral != null -> {
                    if (servicesBeforeReferral?.startsWith("[")!! &&
                        servicesBeforeReferral?.endsWith("]")!!
                    ) {
                        preReferralManagement.text = servicesBeforeReferral
                            ?.substring(1, servicesBeforeReferral!!.length - 1)
                    } else {
                        preReferralManagement.text = servicesBeforeReferral
                    }
                    if (!StringUtils.isEmpty(servicesBeforeReferralOther)) {
                        preReferralManagement.append(", $servicesBeforeReferralOther")
                    }
                }
                else -> {
                    preReferralManagement.text = getString(R.string.empty_value)
                    preManagementServicesServices.visibility = View.GONE
                }
            }
        }
    }

    private val familyMemberContacts: String?
        get() {
            var phoneNumber: String? = ""
            val familyPhoneNumber = memberObject!!.phoneNumber
            val familyPhoneNumberOther = memberObject!!.otherPhoneNumber
            when {
                StringUtils.isNoneEmpty(familyPhoneNumber) -> {
                    phoneNumber = familyPhoneNumber
                }
                StringUtils.isEmpty(familyPhoneNumber) && StringUtils.isNoneEmpty(
                    familyPhoneNumberOther
                )
                -> {
                    phoneNumber = familyPhoneNumberOther
                }
                StringUtils.isNoneEmpty(familyPhoneNumber) && StringUtils.isNoneEmpty(
                    familyPhoneNumberOther
                )
                -> {
                    phoneNumber = "$familyPhoneNumber, $familyPhoneNumberOther"
                }
                StringUtils.isNoneEmpty(memberObject!!.familyHeadPhoneNumber) -> {
                    phoneNumber = memberObject!!.familyHeadPhoneNumber
                }
            }
            return phoneNumber
        }

    companion object {
        /**
         * This static method is used to launch [ReferralDetailsViewActivity] activity
         *
         * @param [activity] the activity that you want to launch [ReferralDetailsViewActivity] from
         * @param [memberObject] entity class for the client with all the required details
         */
        @JvmStatic
        fun startReferralDetailsViewActivity(activity: Activity, memberObject: MemberObject?) {
            activity.startActivity(
                Intent(activity, ReferralDetailsViewActivity::class.java).apply {
                    putExtra(Constants.ReferralMemberObject.MEMBER_OBJECT, memberObject)
                }
            )
        }
    }
}