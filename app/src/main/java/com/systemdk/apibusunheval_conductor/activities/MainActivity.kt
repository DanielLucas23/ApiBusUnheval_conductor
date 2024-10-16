package com.systemdk.apibusunheval_conductor.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.systemdk.apibusunheval_conductor.databinding.ActivityMainBinding
import com.systemdk.apibusunheval_conductor.providers.AuthProvider

class MainActivity : AppCompatActivity() {

    //Variables
    private lateinit var binding: ActivityMainBinding
    private val authProvider = AuthProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.textViewRegister.setOnClickListener {
            goToRegister()
        }
    }

    //Ir a la otra ventana para registrarse

    private fun goToRegister(){
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }

    // Capturar datos de los editText

    private fun login(){
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        if (isValidForm(email, password)){
            authProvider.login(email,password).addOnCompleteListener {
                if (it.isSuccessful){
                    goToMap()
                }else{
                    Toast.makeText(this@MainActivity, "Hubo un error al iniciar sesión ${it.exception.toString()}", Toast.LENGTH_LONG).show()
                    Log.d("FIREBASE", "Error: ${it.exception.toString()}")
                }
            }
        }
    }

    // Validar el formulario

    private fun isValidForm(email: String, password: String):Boolean {
        if (email.isEmpty()){
            Toast.makeText(this, "Ingrese su correo electronico", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()){
            Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show()
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

    //Si la sesion existe
    override fun onStart() {
        super.onStart()
        if (authProvider.existSession()){
            goToMap()
        }
    }

}