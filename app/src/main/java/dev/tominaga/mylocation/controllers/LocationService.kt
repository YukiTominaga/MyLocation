package dev.tominaga.mylocation.controllers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.LocationResult
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import dev.tominaga.mylocation.firestore.CurrentLocationFirestoreRepository
import dev.tominaga.mylocation.MainActivity
import dev.tominaga.mylocation.usecases.UpdateCurrentLocationUseCase
import java.lang.Exception
import java.lang.StringBuilder

class LocationService : BroadcastReceiver() {
    companion object {
        val ACTION_PRECESS_UPDATE = "dev.tominaga.mylocation.UPDATE_LOCATION"
    }

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance()

    // Create a new user with a first, middle, and last name
    val user = User("Alan", "Mathison", "Turring", 1912)

    val currentLocationRepository = CurrentLocationFirestoreRepository(db)
    val updateCurrentLocationUseCase = UpdateCurrentLocationUseCase(currentLocationRepository)

    override fun onReceive(context: Context?, intent: Intent?) {
        // Controllerじゃん? なのにデータベースになにかを保存するのきもくない?
        // CleanArchitectureならControllerからメッセージを受け取ってドメインの操作をしてViewに返すUseCaseがあり...
        // UseCaseがモデルを使ってデータをあっちこっちに渡す
        // "自分の位置情報を取得"して"永続化する"
        // "考えることが違うからモデルを分ける"という視点   現在の最新の位置情報と、永続化された位置情報に分かれる

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference: DocumentReference? -> Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference!!.id}") }
            .addOnFailureListener { exception: Exception -> Log.w("TAG", "Error adding document", exception) }
        if (intent == null) return

        val action = intent!!.action
        if (!action.equals(ACTION_PRECESS_UPDATE)) return

        val result = LocationResult.extractResult(intent!!)
        if (result == null) return

        val location = result.lastLocation
        val locationString = StringBuilder(location.latitude.toString())
            .append("/").append(location.longitude.toString()).toString()
        try {
            updateCurrentLocationUseCase.execute(location.latitude, location.longitude)
            MainActivity.getMainInstance().updateTextView(locationString)
        } catch (e: Exception) {
            MainActivity.getMainInstance().updateTextView("Error")
            Toast.makeText(context, locationString, Toast.LENGTH_SHORT)
        }
    }

}

class User(val first: String, val middle: String, val last: String, val born: Int) {}