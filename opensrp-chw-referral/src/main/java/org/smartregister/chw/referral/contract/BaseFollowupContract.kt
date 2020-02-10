package org.smartregister.chw.referral.contract

import androidx.lifecycle.ViewModel
import com.nerdstone.neatformcore.domain.model.NFormViewData
import org.json.JSONObject
import org.koin.core.KoinComponent
import org.smartregister.chw.referral.domain.FollowupFeedbackObject
import org.smartregister.chw.referral.domain.MemberObject
import java.util.*

/***
 * Defines the contract that must be implemented for Followup referrals
 */
interface BaseFollowupContract {

    /***
     * View for the followup referrals; it also extends [InteractorCallBack]
     */
    interface View : InteractorCallBack {

        /***
         * This function returns the presenter
         * @return [Presenter]
         */
        fun presenter(): Presenter

        /***
         * This method passes the profile data to the [View]
         */
        fun setProfileViewWithData()
    }

    /***
     * Presenter for followup referrals
     */
    interface Presenter {

        /**
         * This function returns the view model class
         * @return [Class] that extends [ViewModel] and implements [Model]
         */
        fun <T> getViewModel(): Class<T> where T : ViewModel, T : Model

        /***
         * This functions passes profile data to the view
         * [memberObject] entity with the client details
         */
        fun fillProfileData(memberObject: MemberObject?)

        /***
         * Returns followup referrals view
         * @return [View] the view used on referrals followup
         */
        fun getView(): View?

        /***
         * This function initializes the client entity
         * using the provided  [memberObject]
         */
        fun initializeMemberObject(memberObject: MemberObject)

        /***
         * This function is used to save data entered in the referral form
         * @param [valuesHashMap] map of values retrieved from the views generated from the json
         * @param [jsonObject] form json object
         */
        fun saveForm(valuesHashMap: HashMap<String, NFormViewData>, jsonObject: JSONObject)
    }

    /**
     * The model for Followup Referrals
     */
    interface Model {

        /***
         * This function returns the list
         * @return a list of [FollowupFeedbackObject]
         */
        fun followupFeedbackList(): List<FollowupFeedbackObject>?
    }

    /***
     * Interactor implementations for followup referrals ; it extends [KoinComponent] to allow for dependency injection
     */
    interface Interactor: KoinComponent {

        /**
         * Saves the followup referral
         * @param [baseEntityId] a unique UUID for the client whose record is being saved
         * @param [valuesHashMap] a map containing the data retrieved from the views
         * @param [jsonObject] form json object
         * @param [callBack] callback when the saving is done
         */
        fun saveFollowup(
            baseEntityId: String, valuesHashMap: HashMap<String, NFormViewData>,
            jsonObject: JSONObject, callBack: InteractorCallBack
        )

    }

    /**
     * Callback methods for followup referrals
     */
    interface InteractorCallBack {
        /***
         * The call back method called after the followup referral has been saved
         */
        fun onFollowupSaved()
    }
}