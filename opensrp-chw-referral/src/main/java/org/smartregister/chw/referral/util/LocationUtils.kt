
package org.smartregister.chw.referral.util

import org.smartregister.AllConstants
import org.smartregister.Context
import org.smartregister.domain.Location
import org.smartregister.repository.LocationRepository
import org.smartregister.repository.LocationTagRepository
import java.util.Locale

/**
 * Utility class for location-related operations.
 */
@Suppress("unused")
object LocationUtils {

    private fun getParentLocationIdWithTags(
            locations: List<Location>,
            locationId: String,
            tagName: String
    ): String? {
        val locationTagReposity = LocationTagRepository()
        val allLocationTags = locationTagReposity.allLocationTags
        for (location in locations) {
            val locationTags = allLocationTags.filter { it.locationId == location.id }
            if (location.id == locationId) {
                if (locationTags.any { it.name.equals(tagName, ignoreCase = true) }) {
                    return location.id
                } else {
                    return getParentLocationIdWithTags(locations, location.properties.parentId, tagName)
                }
            }
        }
        return null
    }

    /**
     * Retrieves the Ward ID associated with the current location.
     *
     * @return The Ward ID, or null if not found.
     */
    fun getWardId(): String? {
        val locationRepository = LocationRepository()
        val locations = locationRepository.allLocations
        val locationId = Context.getInstance().allSharedPreferences()
                .getPreference(AllConstants.CURRENT_LOCATION_ID)
        return getParentLocationIdWithTags(locations, locationId, "Ward")
    }


    private fun String.isIn(haystack:String):Boolean{
        return haystack.toLowerCase(Locale.ROOT).contains(this.toLowerCase(Locale.ROOT))
    }

    /**
     * Get list of facilities with their keys and names
     */
    fun getFacilitiesKeyAndName(): Map<String, String> {
        return LocationRepository().allLocations
            .filter { loc->LocationTagRepository().allLocationTags.any{it.locationId==loc.id && "facility".isIn(it.name)} }
            .associate {loc->loc.id to loc.properties.name}
    }
}
