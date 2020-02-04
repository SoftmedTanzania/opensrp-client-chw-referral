package org.smartregister.chw.referral.provider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.smartregister.chw.referral.R
import org.smartregister.chw.referral.provider.ReferralHistoryProvider.HistoryViewHolder

class ReferralHistoryProvider(private val referrals: Collection<*>) :
    RecyclerView.Adapter<HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_referral_history_item, parent, false
            )
        )

    override fun onBindViewHolder(viewHolder: HistoryViewHolder, position: Int) = Unit

    override fun getItemCount() = referrals.size

    open inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var facilityName: TextView = itemView.findViewById(R.id.facility_titleview)
        var reasonForReferral: TextView = itemView.findViewById(R.id.reason_for_referral)
        var appointmentDate: TextView = itemView.findViewById(R.id.appointment_date)
    }
}