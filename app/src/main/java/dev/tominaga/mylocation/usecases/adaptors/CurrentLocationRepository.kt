package dev.tominaga.mylocation.usecases.adaptors

import dev.tominaga.mylocation.models.CurrentLocation

interface CurrentLocationRepository {
    fun put(currentLocation: CurrentLocation)
}
