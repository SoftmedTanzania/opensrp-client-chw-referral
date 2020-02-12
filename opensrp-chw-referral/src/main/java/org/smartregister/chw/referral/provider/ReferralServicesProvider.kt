package org.smartregister.chw.referral.provider

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import org.smartregister.chw.referral.R
import org.smartregister.chw.referral.domain.ReferralServiceObject

class ReferralServicesProvider(
    private val context: Context, private val referralServices: List<ReferralServiceObject>
) : BaseAdapter(), SpinnerAdapter {

    override fun getCount() = referralServices.size

    override fun getItem(i: Int) = referralServices[i]

    override fun getItemId(i: Int) = i.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) view = View.inflate(context, R.layout.item_list_content, null)
        view?.findViewById<TextView>(R.id.content)
            ?.apply { text = referralServices[position].nameEn }
        return view!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) view =
            View.inflate(context, R.layout.item_spinner_dropdown_item, null)
        view?.findViewById<TextView>(R.id.text)
            ?.apply { text = referralServices[position].nameEn }
        return view!!
    }

}