package org.smartregister.chw.referral.util

import com.nerdstone.neatformcore.domain.model.NFormViewData
import org.apache.commons.lang3.StringUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.smartregister.chw.referral.ReferralLibrary
import org.smartregister.chw.referral.domain.ReferralTask
import org.smartregister.chw.referral.util.Constants.Tables
import org.smartregister.clientandeventmodel.Event
import org.smartregister.clientandeventmodel.Obs
import org.smartregister.domain.tag.FormTag
import org.smartregister.repository.AllSharedPreferences
import org.smartregister.util.FormUtils
import org.smartregister.util.JsonFormUtils
import java.util.*

const val METADATA = "metadata"

object JsonFormUtils : JsonFormUtils() {
    @JvmStatic
    fun processJsonForm(
        allSharedPreferences: AllSharedPreferences, entityId: String?,
        valuesHashMap: HashMap<String, NFormViewData>, jsonForm: JSONObject?, encounter_type: String
    ): ReferralTask {
        val bindType: String? = when (encounter_type) {
            Constants.EventType.REGISTRATION -> {
                Tables.REFERRAL
            }
            Constants.EventType.REFERRAL_FOLLOW_UP_VISIT -> {
                Tables.REFERRAL_FOLLOW_UP
            }
            else -> null
        }
        val event =
            createEvent(
                JSONArray(), getJSONObject(jsonForm, METADATA),
                formTag(allSharedPreferences), entityId, encounter_type, bindType
            ).also { it.obs = getObs(valuesHashMap) }
        return ReferralTask().also {
            it.event = event
        }
    }

    private fun formTag(allSharedPreferences: AllSharedPreferences): FormTag =
        FormTag().also {
            it.providerId = allSharedPreferences.fetchRegisteredANM()
            it.appVersion = ReferralLibrary.getInstance().applicationVersion
            it.databaseVersion = ReferralLibrary.getInstance().databaseVersion
        }


    fun tagEvent(allSharedPreferences: AllSharedPreferences, event: Event) {
        event.apply {
            providerId = allSharedPreferences.fetchRegisteredANM()
            locationId = locationId(allSharedPreferences)
            childLocationId = allSharedPreferences.fetchCurrentLocality()
            team = allSharedPreferences.fetchDefaultTeam(providerId)
            teamId = allSharedPreferences.fetchDefaultTeamId(providerId)
            clientApplicationVersion = ReferralLibrary.getInstance().applicationVersion
            clientDatabaseVersion = ReferralLibrary.getInstance().databaseVersion
        }
    }

    private fun locationId(allSharedPreferences: AllSharedPreferences): String {
        val providerId = allSharedPreferences.fetchRegisteredANM()
        return allSharedPreferences.fetchUserLocalityId(providerId).also {
            if (StringUtils.isBlank(it)) {
                return allSharedPreferences.fetchDefaultLocalityId(providerId)
            }
        }
    }

    @JvmStatic
    @Throws(JSONException::class)
    fun addFormMetadata(jsonObject: JSONObject, entityId: String?, currentLocationId: String?) {
        jsonObject.getJSONObject(METADATA)
            .put(ENCOUNTER_LOCATION, currentLocationId)
            .put(ENTITY_ID, entityId)
    }

    @JvmStatic
    @Throws(Exception::class)
    fun getFormAsJson(formName: String?): JSONObject {
        return FormUtils.getInstance(ReferralLibrary.getInstance().context().applicationContext())
            .getFormJson(formName)
    }

    private fun getObs(detailsHashMap: HashMap<String, NFormViewData>): List<Obs> {
        val obs = ArrayList<Obs>()
        detailsHashMap.keys.forEach { key ->
            val ob = Obs()
            detailsHashMap[key]?.also { viewData ->
                viewData.metadata?.also {
                    if (it.containsKey(OPENMRS_ENTITY))
                        ob.fieldType = it[OPENMRS_ENTITY].toString()
                    if (it.containsKey(OPENMRS_ENTITY_ID))
                        ob.fieldCode = it[OPENMRS_ENTITY_ID].toString()
                    if (it.containsKey(OPENMRS_ENTITY_PARENT))
                        ob.parentCode = it[OPENMRS_ENTITY_PARENT].toString()
                    when (viewData.value) {
                        is HashMap<*, *> -> {
                            val humanReadableValues = ArrayList<Any?>()
                            addHumanReadableValues(
                                ob, humanReadableValues, viewData.value as HashMap<*, *>?
                            )
                            if (humanReadableValues.isNotEmpty())
                                ob.humanReadableValues = humanReadableValues
                        }
                        is NFormViewData -> {
                            val humanReadableValues = ArrayList<Any?>()
                            saveValues(viewData.value as NFormViewData?, ob, humanReadableValues)
                            if (humanReadableValues.isNotEmpty())
                                ob.humanReadableValues = humanReadableValues
                        }
                        else -> {
                            ob.value = viewData.value
                        }
                    }
                }
            }
            ob.formSubmissionField = key
            obs.add(ob)
        }
        return obs
    }

    private fun addHumanReadableValues(
        obs: Obs, humanReadableValues: MutableList<Any?>, valuesHashMap: HashMap<*, *>?
    ) {
        valuesHashMap!!.keys.forEach { optionsValues ->
            if (valuesHashMap[optionsValues] is NFormViewData && valuesHashMap[optionsValues] != null) {
                saveValues(
                    (valuesHashMap[optionsValues] as NFormViewData), obs, humanReadableValues
                )
            } else {
                obs.value = valuesHashMap[optionsValues]
            }
        }
    }

    private fun saveValues(
        optionsNFormViewData: NFormViewData?, obs: Obs, humanReadableValues: MutableList<Any?>
    ) {
        optionsNFormViewData?.also { viewData ->
            viewData.metadata?.also {
                if (it.containsKey(OPENMRS_ENTITY_ID)) {
                    obs.value = it[OPENMRS_ENTITY_ID]
                    humanReadableValues.add(viewData.value)
                } else {
                    obs.value = optionsNFormViewData.value
                }
            }
        }
    }
}