package dev.tominaga.mylocation.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dev.tominaga.mylocation.models.CurrentLocation
import dev.tominaga.mylocation.usecases.adaptors.CurrentLocationRepository

class CurrentLocationFirestoreRepository(val db: FirebaseFirestore) :
    CurrentLocationRepository {

    override fun put(currentLocation: CurrentLocation) {
        db.collection("roselia")
            .document("currentLocation")
            .set(currentLocation, SetOptions.merge())
    }
}