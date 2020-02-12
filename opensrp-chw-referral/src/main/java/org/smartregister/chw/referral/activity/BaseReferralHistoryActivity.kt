package org.smartregister.chw.referral.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.joda.time.DateTime
import org.joda.time.Period
import org.smartregister.chw.referral.R
import org.smartregister.chw.referral.contract.BaseReferralHistoryContract
import org.smartregister.chw.referral.domain.MemberObject
import org.smartregister.chw.referral.presenter.BaseReferralHistoryPresenter
import org.smartregister.chw.referral.util.Constants

/***
 * This class is for creating referral history page. It implements [BaseReferralHistoryContract.View]
 */
open class BaseReferralHistoryActivity : AppCompatActivity(),
    BaseReferralHistoryContract.View {

    protected var memberObject: MemberObject? = null
    private lateinit var textViewName: TextView
    private lateinit var textViewGender: TextView
    private lateinit var textViewLocation: TextView
    private lateinit var textViewUniqueID: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_referral_history)
        memberObject =
            intent.getSerializableExtra(Constants.ActivityPayload.MEMBER_OBJECT) as MemberObject
        setupViews()
        if (memberObject != null) presenter().fillClientData(memberObject!!)
    }

    private fun setupViews() {
        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        mToolbar.setNavigationOnClickListener { finish() }
        textViewName = findViewById(R.id.textview_name)
        textViewGender = findViewById(R.id.textview_gender)
        textViewLocation = findViewById(R.id.textview_address)
        textViewUniqueID = findViewById(R.id.textview_id)
        val historyRecyclerView = findViewById<RecyclerView>(R.id.referral_history_recycler_view)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.setHasFixedSize(true)
    }

    @SuppressLint("SetTextI18n")
    override fun setProfileViewWithData(memberObject: MemberObject) {
        memberObject.also {
            val age = Period(DateTime(it.age), DateTime()).years
            textViewName.text = " ${it.firstName} ${it.middleName} ${it.lastName}, $age"
            textViewGender.text = it.gender
            textViewLocation.text = it.address
            textViewUniqueID.text = it.uniqueId
        }
    }

    override fun presenter() = BaseReferralHistoryPresenter(memberObject!!, this)

}