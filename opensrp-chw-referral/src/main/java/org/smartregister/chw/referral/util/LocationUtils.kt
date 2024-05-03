package org.smartregister.chw.referral.util

import org.smartregister.AllConstants
import org.smartregister.Context
import org.smartregister.domain.Location
import org.smartregister.repository.LocationRepository
import org.smartregister.repository.LocationTagRepository

/**
 * Utility class for location-related operations.
 */
object LocationUtils {
    /**
     * Recursively searches for the parent location ID that has a specific tag associated with it.
     *
     * @param locations A list of Location objects to search within.
     * @param locationId The ID of the current location being searched.
     * @param tagName The name of the tag to search for.
     * @return The ID of the parent location that has the specified tag, or null if not found.
     */
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
}
