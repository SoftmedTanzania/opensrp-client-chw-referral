package org.smartregister.chw.referral.provider;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.domain.FollowupFeedbackObject;

import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class FollowupFeedbackProvider extends BaseAdapter implements SpinnerAdapter {
    private List<FollowupFeedbackObject> followuFeedbackList;
    private Context context;
    private Locale currentLocale;

    public FollowupFeedbackProvider(Context context, List<FollowupFeedbackObject> feedbackObjects) {
        this.context = context;
        this.followuFeedbackList = feedbackObjects;
        this.currentLocale = context.getResources().getConfiguration().locale;
    }

    @Override
    public int getCount() {
        return followuFeedbackList.size();
    }

    @Override
    public Object getItem(int i) {
        return followuFeedbackList.get(i);
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
        textView.setText(getFeedbackNameByLocale(followuFeedbackList.get(i)));
        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null)
            view = View.inflate(context, R.layout.item_spinner_dropdown_item, null);
        TextView textView = view.findViewById(R.id.text);
        textView.setText(getFeedbackNameByLocale(followuFeedbackList.get(position)));

        return view;
    }

    private String getFeedbackNameByLocale(FollowupFeedbackObject followupFeedbackObject) {
        JSONObject feedbackJsonObject = null;
        try {
            feedbackJsonObject = new JSONObject(new Gson().toJson(followupFeedbackObject));
        } catch (JSONException e) {
            Timber.e(e);
        }

        String key = "name" + "_" + currentLocale.getLanguage();
        if (feedbackJsonObject.has(key)) {
            try {
                return feedbackJsonObject.getString(key);
            } catch (JSONException e) {
                Timber.e(e);
                return followupFeedbackObject.getNameEn();
            }
        } else {
            return followupFeedbackObject.getNameEn();
        }

    }
}
