package org.smartregister.chw.referral.sample.activity

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import org.smartregister.chw.referral.R

class IssueReferralActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.neat_referral_form_activity)
        JsonFormBuilder(
            this, "json.form/general_neat_referral_form.json",
            findViewById<LinearLayout>(R.id.formLayout)
        ).buildForm(null, null)
    }
}