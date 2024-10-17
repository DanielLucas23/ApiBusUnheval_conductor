package com.systemdk.apibusunheval_conductor.providers

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.systemdk.apibusunheval_conductor.models.Conductor
import java.io.File

class ConductorProvider {

    val db = Firebase.firestore.collection("Conductores")
    var storage = FirebaseStorage.getInstance().getReference().child("profile")

    fun create(conductor: Conductor):Task<Void>{
        return db.document(conductor.id!!).set(conductor)
    }

    fun getConductor(idConductor: String): Task<DocumentSnapshot> {
        return db.document(idConductor).get()
    }

    fun getImageUrl(): Task<Uri> {
        return storage.downloadUrl
    }

    fun uploadImage(id: String,file: File): StorageTask<UploadTask.TaskSnapshot> {
        var fromFile = Uri.fromFile(file)
        val ref = storage.child("$id.jpg")
        storage = ref
        val uploadTask = ref.putFile(fromFile)

        return uploadTask.addOnFailureListener {
            Log.d("STORAGE","ERROR: ${it.message}")
        }
    }

    fun getRut(idConductor: String): Task<String?> {
        return db.document(idConductor).get().continueWith { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    document.getString("rut")
                } else {
                    null
                }
            } else {
                Log.e("Firestore", "Error al obtener el rut", task.exception)
                null
            }
        }
    }

    fun update(conductor: Conductor): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()

        conductor.names?.let { map["names"] = it }
        conductor.phone?.let { map["phone"] = it }
        conductor.rut?.let { map["rut"] = it }
        conductor.placa?.let { map["placa"] = it }
        conductor.image?.let { map["image"] = it }

        return conductor.id?.let { db.document(it).update(map) }
            ?: throw IllegalArgumentException("Conductor ID cannot be null")
    }


}