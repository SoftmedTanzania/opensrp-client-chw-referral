package org.smartregister.chw.referral.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.nerdstone.neatformcore.domain.builders.FormBuilder;
import com.nerdstone.neatformcore.domain.model.NForm;
import com.nerdstone.neatformcore.domain.model.NFormContent;
import com.nerdstone.neatformcore.form.json.JsonFormBuilder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.smartregister.chw.referral.R;

public class BaseNeatIssueReferralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.neat_referral_form_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout formLayout = findViewById(R.id.formLayout);
        JsonFormBuilder jsonFormBuilder = new JsonFormBuilder(this, "json.form/general_neat_referral_form.json", formLayout);
        NForm form = jsonFormBuilder.getForm();
        NFormContent content = form.steps.get(0);



        FormBuilder formBuilder = jsonFormBuilder.buildForm(null, null);

    }
}
