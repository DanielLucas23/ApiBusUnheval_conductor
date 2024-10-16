package com.systemdk.apibusunheval_conductor.providers

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import org.imperiumlabs.geofirestore.GeoFirestore

class GeoProvider {

    val collection = FirebaseFirestore.getInstance().collection("Locations")
    val geoFirestore = GeoFirestore(collection)

    fun saveLocation(idConductor: String, position: LatLng) {
        geoFirestore.setLocation(idConductor, GeoPoint(position.latitude, position.longitude))
    }

    fun removeLocation(idConductor: String){
        collection.document(idConductor).delete()
    }

    fun getLocation(idConductor: String):Task<DocumentSnapshot>{
        return collection.document(idConductor).get().addOnFailureListener {exception->
            Log.d("FIRESTORE", "ERROR: ${exception.toString()}")
        }
    }
}