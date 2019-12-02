package org.smartregister.chw.referral.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.nerdstone.neatformcore.domain.builders.FormBuilder;
import com.nerdstone.neatformcore.form.json.JsonFormBuilder;

import org.smartregister.chw.referral.R;

public class BaseNeatIssueReferralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.neat_referral_form_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout formLayout = findViewById(R.id.formLayout);

        FormBuilder formBuilder = new JsonFormBuilder(this, "json.form/general_neat_referral_form.json", formLayout).buildForm(null, null);


    }
}
