package com.systemdk.apibusunheval_conductor.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.systemdk.apibusunheval_conductor.databinding.ActivityProfileBinding
import com.systemdk.apibusunheval_conductor.models.Conductor
import com.systemdk.apibusunheval_conductor.providers.AuthProvider
import com.systemdk.apibusunheval_conductor.providers.ConductorProvider
import java.io.File

class ProfileActivity : AppCompatActivity() {

    //Variables
    private lateinit var binding: ActivityProfileBinding
    val conductorProvider = ConductorProvider()
    val authProvider = AuthProvider()
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getConductor()
        binding.imageViewBack.setOnClickListener { finish() }
        binding.btnUpdate.setOnClickListener { updateInfo() }
        binding.circleImageProfile.setOnClickListener { selectImage() }
    }

    private fun updateInfo(){
        val name = binding.textFieldName.text.toString()
        val phone = binding.textFieldPhone.text.toString()
        val carRut = binding.textRutCar.text.toString()
        val carPlate = binding.textCarPlate.text.toString()

        if (name.isEmpty() || phone.isEmpty() || carRut.isEmpty() || carPlate.isEmpty()) {
            Toast.makeText(this, "Todos los campos deben estar llenos", Toast.LENGTH_LONG).show()
            return
        }

        val conductor = Conductor(
            id = authProvider.getId(),
            names = name,
            phone = phone,
            rut = carRut,
            placa = carPlate
        )

        val conductorId = authProvider.getId()
        if (conductorId == null) {
            Toast.makeText(this, "No se pudo obtener el ID del conductor", Toast.LENGTH_LONG).show()
            return
        }

        if(imageFile != null){

            conductorProvider.uploadImage(authProvider.getId(), imageFile!!).addOnSuccessListener { taskSnapshot->
                conductorProvider.getImageUrl().addOnSuccessListener { url->

                    val imageUrl = url.toString()
                    conductor.image = imageUrl
                    conductorProvider.update(conductor).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(this@ProfileActivity, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(this@ProfileActivity, "No se pudo actualizar la información", Toast.LENGTH_LONG).show()
                        }
                    }
                    Log.d("STORAGE","URL: $imageUrl")

                }
            }

        }else{

            conductorProvider.update(conductor).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this@ProfileActivity, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@ProfileActivity, "No se pudo actualizar la información", Toast.LENGTH_LONG).show()
                }
            }

        }


    }

    private var starImageForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data

        if (resultCode == Activity.RESULT_OK){

            val fileUri = data?.data
            imageFile = File(fileUri?.path)
            binding.circleImageProfile.setImageURI(fileUri)

        }else if(resultCode == ImagePicker.RESULT_ERROR){
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "Tarea cancelada", Toast.LENGTH_LONG).show()
        }
    }

    private fun selectImage(){
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                starImageForResult.launch(intent)
            }
    }

    private fun getConductor(){
        conductorProvider.getConductor(authProvider.getId()).addOnSuccessListener { document ->

            if (document.exists()){
                val conductor = document.toObject(Conductor::class.java)
                binding.textViewEmail.text = conductor?.email ?: "Sin correo"
                binding.textFieldName.setText(conductor?.names ?: "")
                binding.textFieldPhone.setText(conductor?.phone ?: "")
                binding.textRutCar.setText(conductor?.rut ?: "")
                binding.textCarPlate.setText(conductor?.placa ?: "")

                conductor?.image?.let { imageUrl ->
                    if (imageUrl.isNotEmpty()) {
                        Glide.with(this).load(imageUrl).into(binding.circleImageProfile)
                    }
                }
            }

        }
    }


}