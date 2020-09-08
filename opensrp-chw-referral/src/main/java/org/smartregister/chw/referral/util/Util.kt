package org.smartregister.chw.referral.util

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.nerdstone.neatformcore.domain.model.NFormViewData
import org.json.JSONObject
import org.koin.core.KoinComponent
import org.smartregister.chw.referral.R
import org.smartregister.chw.referral.ReferralLibrary
import org.smartregister.chw.referral.contract.BaseReferralCallDialogContract
import org.smartregister.chw.referral.contract.BaseReferralCallDialogContract.Dialer
import org.smartregister.chw.referral.custom_views.ClipboardDialog
import org.smartregister.clientandeventmodel.Event
import org.smartregister.domain.db.EventClient
import org.smartregister.repository.BaseRepository
import org.smartregister.util.PermissionUtils
import org.smartregister.util.Utils
import timber.log.Timber
import java.util.*

/**
 * This provides common utilities functions
 */
object Util : KoinComponent {

    /**
     * Uses the [referralLibrary] client processor to to save the given OpenSRP [baseEvent]
     */
    @JvmStatic
    @Throws(Exception::class)
    fun processEvent(referralLibrary: ReferralLibrary, baseEvent: Event?) {
        if (baseEvent != null) {
            JsonFormUtils.tagEvent(referralLibrary, baseEvent)
            val eventJson =
                JSONObject(org.smartregister.util.JsonFormUtils.gson.toJson(baseEvent))
            referralLibrary.syncHelper.addEvent(baseEvent.formSubmissionId, eventJson)
            val lastSyncDate =
                Date(referralLibrary.context.allSharedPreferences().fetchLastUpdatedAtDate(0))
            Timber.i(
                "EventClient = %s",
                Gson().toJson(
                    referralLibrary.syncHelper.getEvents(lastSyncDate, BaseRepository.TYPE_Unsynced)
                )
            )
            referralLibrary.clientProcessorForJava.processClient(
                referralLibrary.syncHelper.getEvents(mutableListOf(baseEvent.formSubmissionId))
            )
            Utils.getAllSharedPreferences().saveLastUpdatedAtDate(lastSyncDate.time)
        }
    }


    /**
     * Launches call dialer with [phoneNumber] using [activity] as the context from the [callView]
     */
    fun launchDialer(
        activity: Activity, callView: BaseReferralCallDialogContract.View?, phoneNumber: String?
    ): Boolean = when {
        ContextCompat.checkSelfPermission(
            activity as Context, Manifest.permission.READ_PHONE_STATE
        ) != PackageManager.PERMISSION_GRANTED
        -> { // set a pending call execution request
            if (callView != null) {
                callView.pendingCallRequest =
                    object : Dialer {
                        override fun callMe() {
                            launchDialer(activity, callView, phoneNumber)
                        }
                    }
            }
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.READ_PHONE_STATE),
                PermissionUtils.PHONE_STATE_PERMISSION_REQUEST_CODE
            )
            false
        }
        else -> {
            if ((activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).line1Number
                == null
            ) {
                Timber.i("No dial application so we launch copy to clipboard...")
                val clipboard =
                    activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(
                    activity.getText(R.string.copied_phone_number), phoneNumber
                )
                clipboard.setPrimaryClip(clip)
                ClipboardDialog(activity, R.style.ClipboardDialogStyle).also {
                    it.content = phoneNumber
                    it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    it.show()
                }
                // no phone
                Toast.makeText(
                    activity, activity.getText(R.string.copied_phone_number), Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent =
                    Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null))
                activity.startActivity(intent)
            }
            true
        }
    }

    @JvmStatic
    fun extractReferralProblems(valuesHashMap: HashMap<String, NFormViewData>): String? {
        val problemViewData = valuesHashMap[JsonFormConstants.PROBLEM]?.value as HashMap<*, *>?
        val problemOtherViewData = valuesHashMap[JsonFormConstants.PROBLEM_OTHER]?.value as String?
        val fpMethodViewData =
            valuesHashMap[JsonFormConstants.FAMILY_PLANNING_METHOD]?.value as HashMap<*, *>?
        //This should be a map with single item since this record is stored in a radio group layout that saves the option name against its label
        val fpMethod = (fpMethodViewData?.values?.elementAt(0) as NFormViewData?)?.value as String?
        val formattedFpMethod = fpMethod?.plus(if (problemViewData.isNullOrEmpty()) "" else ": ")
        val otherProblems =
            if (problemOtherViewData.isNullOrEmpty()) "" else "Other: $problemOtherViewData"
        val formattedOtherProblem =
            if (problemViewData.isNullOrEmpty()) otherProblems else ", $otherProblems"

        problemViewData?.also { problemValue ->
            val problemDescription = problemValue
                .filter { it.value != null }
                .map { (it.value as NFormViewData).value as String }
                .toList()
                .joinToString()
            return "${formattedFpMethod ?: ""}$problemDescription$formattedOtherProblem".trim(
                ',', ' '
            )
        }
        return "${formattedFpMethod ?: ""}$formattedOtherProblem".trim(',', ' ')
    }
}