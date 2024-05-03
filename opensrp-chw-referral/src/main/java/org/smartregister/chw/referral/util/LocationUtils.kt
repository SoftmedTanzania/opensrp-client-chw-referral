package org.smartregister.chw.referral.util

import org.smartregister.AllConstants
import org.smartregister.Context
import org.smartregister.domain.Location
import org.smartregister.repository.LocationRepository

object LocationUtils {
    private fun getParentLocationIdWithTags(
        locations: List<Location>,
        locationId: String,
        tagName: String
    ): String? {
        for (location in locations) {
            val locationTags = location.locationTags
            if (location.id == locationId) {
                if (locationTags.iterator().next().name.equals(tagName, ignoreCase = true)) {
                    return location.id
                } else {
                    getParentLocationIdWithTags(locations, location.properties.parentId, tagName)
                }
            }
        }
        return null
    }

    fun getWardId(): String? {
        val locationRepository = LocationRepository()
        val locations = locationRepository.allLocations
        val locationId = Context.getInstance().allSharedPreferences()
            .getPreference(AllConstants.CURRENT_LOCATION_ID)
        return getParentLocationIdWithTags(locations, locationId, "Ward")
    }
}
