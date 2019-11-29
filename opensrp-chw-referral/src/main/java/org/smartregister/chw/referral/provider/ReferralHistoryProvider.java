package org.smartregister.chw.referral.provider;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.smartregister.chw.referral.R;

import java.util.Collection;

public class ReferralHistoryProvider extends RecyclerView.Adapter<ReferralHistoryProvider.HistoryViewHolder> {
    private Collection<?> referrals;

    public ReferralHistoryProvider(Collection<?> referrals) {
        this.referrals = referrals;

    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_referral_history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder viewHolder, int position) {
        //To be implemented as the next feature, this is currently a place holder adapter

    }

    @Override
    public int getItemCount() {
        return referrals.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView facilityName;
        public TextView reasonForReferral;
        public TextView appointmentDate;

        public HistoryViewHolder(View itemView) {
            super(itemView);

            facilityName = itemView.findViewById(R.id.facility_titleview);
            reasonForReferral = itemView.findViewById(R.id.reason_for_referral);
            appointmentDate = itemView.findViewById(R.id.appointment_date);
        }
    }


}
