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
import android.os.Build
import android.telephony.TelephonyManager
import android.text.Html
import android.text.Spanned
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import org.json.JSONObject
import org.smartregister.chw.referral.R
import org.smartregister.chw.referral.ReferralLibrary
import org.smartregister.chw.referral.contract.BaseReferralCallDialogContract
import org.smartregister.chw.referral.contract.BaseReferralCallDialogContract.Dialer
import org.smartregister.chw.referral.custom_views.ClipboardDialog
import org.smartregister.chw.referral.domain.ReferralServiceObject
import org.smartregister.chw.referral.repository.ReferralServiceRepository
import org.smartregister.clientandeventmodel.Event
import org.smartregister.repository.AllSharedPreferences
import org.smartregister.repository.BaseRepository
import org.smartregister.util.PermissionUtils
import org.smartregister.util.Utils
import timber.log.Timber
import java.util.*

object Util {

    private val syncHelper get() = ReferralLibrary.getInstance().ecSyncHelper

    private val clientProcessorForJava get() = ReferralLibrary.getInstance().clientProcessorForJava

    @JvmStatic
    @Throws(Exception::class)
    fun processEvent(allSharedPreferences: AllSharedPreferences, baseEvent: Event?) {
        if (baseEvent != null) {
            JsonFormUtils.tagEvent(allSharedPreferences, baseEvent)
            val eventJson =
                JSONObject(org.smartregister.util.JsonFormUtils.gson.toJson(baseEvent))
            syncHelper.addEvent(baseEvent.baseEntityId, eventJson)
            val lastSyncDate = Date(Utils.getAllSharedPreferences().fetchLastUpdatedAtDate(0))
            val eventClient = syncHelper.getEvents(lastSyncDate, BaseRepository.TYPE_Unsynced)
            Timber.i("EventClient = %s", Gson().toJson(eventClient))
            clientProcessorForJava.processClient(eventClient)
            Utils.getAllSharedPreferences().saveLastUpdatedAtDate(lastSyncDate.time)
        }
    }


    fun fromHtml(text: String?): Spanned {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
            }
            else -> {
                Html.fromHtml(text)
            }
        }
    }

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
                clipboard.primaryClip = clip
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

    val referralServicesList: List<ReferralServiceObject> = try {
        ReferralServiceRepository().referralServices
    } catch (e: Exception) {
        Timber.e(e)
        ArrayList()
    }
}