package org.smartregister.chw.referral.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.contract.BaseReferralHistoryContract;
import org.smartregister.chw.referral.domain.MemberObject;
import org.smartregister.chw.referral.presenter.BaseReferralHistoryPresenter;
import org.smartregister.chw.referral.util.Constants;

import java.util.Collection;
import java.util.Locale;

public class BaseReferralHistoryActivity extends AppCompatActivity implements BaseReferralHistoryContract.View {
    protected MemberObject memberObject;
    protected TextView textViewName;
    protected TextView textViewGender;
    protected TextView textViewLocation;
    protected TextView textViewUniqueID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_referral_history);
        memberObject = (MemberObject) getIntent().getSerializableExtra(Constants.ACTIVITY_PAYLOAD.MEMBER_OBJECT);
        BaseReferralHistoryContract.Presenter baseferralHistoryPresenter = presenter();
        setupViews();

        baseferralHistoryPresenter.fillClientData(memberObject);
    }

    public void setupViews() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(view -> finish());

        textViewName = findViewById(R.id.textview_name);
        textViewGender = findViewById(R.id.textview_gender);
        textViewLocation = findViewById(R.id.textview_address);
        textViewUniqueID = findViewById(R.id.textview_id);

        RecyclerView historyRecyclerView = findViewById(R.id.referral_history_recycler_view);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void setProfileViewWithData(MemberObject memberObject) {
        int age = new Period(new DateTime(memberObject.getAge()), new DateTime()).getYears();
        textViewName.setText(String.format(Locale.getDefault(), "%s %s %s, %d", memberObject.getFirstName(),
                memberObject.getMiddleName(), memberObject.getLastName(), age));
        textViewGender.setText(memberObject.getGender());
        textViewLocation.setText(memberObject.getAddress());
        textViewUniqueID.setText(memberObject.getUniqueId());
    }

    @Override
    public BaseReferralHistoryContract.Presenter presenter() {
        return new BaseReferralHistoryPresenter(memberObject, this);
    }

    @Override
    public Context getCurrentContext() {
        return BaseReferralHistoryActivity.this;
    }

    @Override
    public void setReferralHistoryData(Collection<Object> referrals) {
        //implement
    }
}
