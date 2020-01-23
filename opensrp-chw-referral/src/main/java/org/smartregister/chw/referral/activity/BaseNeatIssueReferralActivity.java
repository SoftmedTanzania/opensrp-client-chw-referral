package org.smartregister.chw.referral.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.nerdstone.neatformcore.form.json.JsonFormBuilder;

import org.smartregister.chw.referral.R;

public class BaseNeatIssueReferralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.neat_referral_form_activity);
        LinearLayout formLayout = findViewById(R.id.formLayout);
        new JsonFormBuilder(this, "json.form/general_neat_referral_form.json", formLayout)
                .buildForm(null, null);

    }
}
