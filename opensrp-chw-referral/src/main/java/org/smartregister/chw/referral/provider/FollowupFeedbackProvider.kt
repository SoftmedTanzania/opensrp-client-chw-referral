package org.smartregister.chw.referral.provider

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import org.smartregister.chw.referral.R
import org.smartregister.chw.referral.domain.FollowupFeedbackObject
import timber.log.Timber
import java.util.*

class FollowupFeedbackProvider(
    private val context: Context, private val followupFeedbackList: List<FollowupFeedbackObject>
) : BaseAdapter(), SpinnerAdapter {
    private val currentLocale: Locale = context.resources.configuration.locale

    override fun getCount() = followupFeedbackList.size

    override fun getItem(i: Int) = followupFeedbackList[i]

    override fun getItemId(i: Int) = i.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?) =
        convertView?.findViewById<TextView>(R.id.content)
            ?.apply { text = getFeedbackNameByLocale(followupFeedbackList[position]) } as View

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = View.inflate(context, R.layout.item_spinner_dropdown_item, null)
        }
        view?.findViewById<TextView>(R.id.text)
            ?.apply { text = getFeedbackNameByLocale(followupFeedbackList[position]) }
        return view!!
    }

    private fun getFeedbackNameByLocale(followupFeedbackObject: FollowupFeedbackObject): String {
        var feedbackJsonObject: JSONObject? = null
        try {
            feedbackJsonObject = JSONObject(Gson().toJson(followupFeedbackObject))
        } catch (e: JSONException) {
            Timber.e(e)
        }
        val key = "name" + "_" + currentLocale.language
        return if (feedbackJsonObject!!.has(key)) {
            try {
                feedbackJsonObject.getString(key)
            } catch (e: JSONException) {
                Timber.e(e)
                followupFeedbackObject.nameEn
            }
        } else {
            followupFeedbackObject.nameEn
        }
    }

}