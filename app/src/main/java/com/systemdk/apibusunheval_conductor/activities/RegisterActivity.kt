package com.systemdk.apibusunheval_conductor.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.systemdk.apibusunheval_conductor.databinding.ActivityRegisterBinding
import com.systemdk.apibusunheval_conductor.models.Conductor
import com.systemdk.apibusunheval_conductor.providers.AuthProvider
import com.systemdk.apibusunheval_conductor.providers.ConductorProvider

class RegisterActivity : AppCompatActivity() {

    //Variables
    private lateinit var binding: ActivityRegisterBinding
    private val authProvider = AuthProvider()
    private val conductorProvider = ConductorProvider()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewIniciarSesion.setOnClickListener {
            goToLogin()
        }

        binding.btnRegister.setOnClickListener {
            register()
        }

    }

    //Ir a la otra ventana de Login

    private fun goToLogin(){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    //Registrarse

    private fun register(){
        val names = binding.editTextNames.text.toString()
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        val c_password = binding.editTextConfirmPassword.text.toString()

        if(isValidForm(names,email,password,c_password)){
            authProvider.register(email,password).addOnCompleteListener {
                if (it.isSuccessful){
                    val conductor = Conductor(
                        id = authProvider.getId(),
                        names = names,
                        email = email
                    )
                    conductorProvider.create(conductor).addOnCompleteListener {
                        if (it.isSuccessful){
                            goToMap()
                        }else{
                            Toast.makeText(this@RegisterActivity, "Hubo un error en almacenar los datos del usuario ${it.exception.toString()}", Toast.LENGTH_LONG).show()
                            Log.d("FIREBASE", "Error: ${it.exception.toString()}")
                        }
                    }
                }else{
                    Toast.makeText(this@RegisterActivity, "Registro fallido ${it.exception.toString()}", Toast.LENGTH_LONG).show()
                    Log.d("FIREBASE", "Error: ${it.exception.toString()}")
                }
            }
        }
    }

    private fun isValidForm(
        names: String,
        email:String,
        password:String,
        c_password:String
    ):  Boolean{
        if (names.isEmpty()){
            Toast.makeText(this, "Ingrese sus Nombres y Apellidos", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isEmpty()){
            Toast.makeText(this, "Ingrese su correo electronico", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()){
            Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        if (c_password.isEmpty()){
            Toast.makeText(this, "Ingrese su confirmación de contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != c_password){
            Toast.makeText(this, "La contraseñas deben coincidir", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length <= 5){
            Toast.makeText(this, "La contraseñas debe tener 6 digitos como mínimo", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    //Iniciar Sesion e ir al Mapa
    private fun goToMap(){
        val i = Intent(this, MapActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }
}