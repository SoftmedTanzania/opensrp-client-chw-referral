package org.smartregister.chw.referral.provider;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.domain.ReferralServiceObject;

import java.util.List;

public class ReferralServicesProvider extends BaseAdapter implements SpinnerAdapter {
    private List<ReferralServiceObject> referralServices;
    private Context context;

    public ReferralServicesProvider(Context context, List<ReferralServiceObject> referralServices) {
        this.context = context;
        this.referralServices = referralServices;
    }

    @Override
    public int getCount() {
        return referralServices.size();
    }

    @Override
    public Object getItem(int i) {
        return referralServices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null)
            v = View.inflate(context, R.layout.item_list_content, null);

        TextView textView = v.findViewById(R.id.content);
        textView.setText(referralServices.get(i).getNameEn());
        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = View.inflate(context, R.layout.item_spinner_dropdown_item, null);
        TextView textView = view.findViewById(R.id.text);
        textView.setText(referralServices.get(position).getNameEn());

        return view;
    }
}
