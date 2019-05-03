package dev.tominaga.mylocation.usecases

import dev.tominaga.mylocation.usecases.adaptors.CurrentLocationRepository
import dev.tominaga.mylocation.models.CurrentLocation

class UpdateCurrentLocationUseCase(val currentLocationRepository: CurrentLocationRepository) {

    fun execute(latitude: Double, longitude: Double) {
        // CurrentLocationモデル作り
        val currentLocation = CurrentLocation(latitude, longitude)
        currentLocationRepository.put(currentLocation)
    }
}