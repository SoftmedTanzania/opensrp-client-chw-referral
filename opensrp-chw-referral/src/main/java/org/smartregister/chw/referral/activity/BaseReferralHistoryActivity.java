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
import org.smartregister.chw.referral.presenter.BaseferralHistoryPresenter;
import org.smartregister.chw.referral.util.Constants;

import java.util.Collection;
import java.util.Locale;

public class BaseReferralHistoryActivity extends AppCompatActivity implements BaseReferralHistoryContract.View {
    protected MemberObject MEMBER_OBJECT;
    protected TextView textViewName;
    protected TextView textViewGender;
    protected TextView textViewLocation;
    protected TextView textViewUniqueID;
    private RecyclerView historyRecyclerView;
    private BaseReferralHistoryContract.Presenter baseferralHistoryPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_referral_history);
        MEMBER_OBJECT = (MemberObject) getIntent().getSerializableExtra(Constants.ACTIVITY_PAYLOAD.MEMBER_OBJECT);
        baseferralHistoryPresenter = presenter();
        setupViews();

        baseferralHistoryPresenter.fillClientData(MEMBER_OBJECT);
    }

    public void setupViews() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(view -> finish());

        textViewName = findViewById(R.id.textview_name);
        textViewGender = findViewById(R.id.textview_gender);
        textViewLocation = findViewById(R.id.textview_address);
        textViewUniqueID = findViewById(R.id.textview_id);

        historyRecyclerView = findViewById(R.id.referral_history_recycler_view);
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
        return new BaseferralHistoryPresenter(MEMBER_OBJECT, this);
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
