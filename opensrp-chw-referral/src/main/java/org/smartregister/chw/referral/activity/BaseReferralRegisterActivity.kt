package org.smartregister.chw.referral.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import org.json.JSONObject
import org.smartregister.chw.referral.R
import org.smartregister.chw.referral.contract.BaseReferralRegisterContract
import org.smartregister.chw.referral.fragment.BaseReferralRegisterFragment
import org.smartregister.chw.referral.listener.ReferralBottomNavigationListener
import org.smartregister.chw.referral.model.BaseReferralRegisterModel
import org.smartregister.chw.referral.presenter.BaseReferralRegisterPresenter
import org.smartregister.chw.referral.util.Constants
import org.smartregister.helper.BottomNavigationHelper
import org.smartregister.listener.BottomNavigationListener
import org.smartregister.view.activity.BaseRegisterActivity

/***
 * This class is for displaying register for all the referred clients with their status i.e PENDING, CANCELED or COMPLETE
 * it implements [BaseReferralRegisterContract.View]
 */
open class BaseReferralRegisterActivity : BaseRegisterActivity(),
    BaseReferralRegisterContract.View {

    protected var baseEntityId: String? = null
    protected var formName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(this.intent) {
            baseEntityId = getStringExtra(Constants.ActivityPayload.BASE_ENTITY_ID)
            action = getStringExtra(Constants.ActivityPayload.ACTION)
            formName = getStringExtra(Constants.ActivityPayload.REFERRAL_FORM_NAME)
        }
    }

    override fun startFormActivity(p0: String?, p1: String?, p2: MutableMap<String, String>?) {
        TODO("Not yet implemented")
    }

    override fun startRegistration() = Unit

    override fun startFormActivity(formName: String?, entityId: String?, metaData: String?) = Unit

    override fun startFormActivity(jsonForm: JSONObject) {
        val intent = Intent(this, BaseReferralRegisterActivity::class.java)
            .putExtra(Constants.JsonFormExtra.JSON, jsonForm.toString())
        startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON)
    }

    override fun onActivityResultExtended(requestCode: Int, resultCode: Int, data: Intent?) = Unit

    override fun getViewIdentifiers(): List<String> = listOf(Constants.Configuration.ISSUE_REFERRAL)

    /**
     * Override this to subscribe to bottom navigation
     */
    override fun registerBottomNavigation() {
        bottomNavigationHelper = BottomNavigationHelper()
        bottomNavigationView =
            findViewById(org.smartregister.R.id.bottom_navigation)
        bottomNavigationView?.also {
            it.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
            it.menu.removeItem(org.smartregister.R.id.action_clients)
            it.menu.removeItem(R.id.action_register)
            it.menu.removeItem(org.smartregister.R.id.action_search)
            it.menu.removeItem(org.smartregister.R.id.action_library)
            it.inflateMenu(menuResource)
            bottomNavigationHelper.disableShiftMode(it)
            it.setOnNavigationItemSelectedListener(getBottomNavigation(this))
        }
    }

    @get:MenuRes
    val menuResource
        get() = R.menu.bottom_nav_referral_menu

    protected open fun getBottomNavigation(activity: Activity?): BottomNavigationListener =
        ReferralBottomNavigationListener(activity!!)

    override fun initializePresenter() {
        presenter = BaseReferralRegisterPresenter(this, BaseReferralRegisterModel())
    }

    override fun getRegisterFragment() = BaseReferralRegisterFragment()

    override fun getOtherFragments() = arrayOf(Fragment())

    override fun presenter() = presenter as BaseReferralRegisterContract.Presenter
}